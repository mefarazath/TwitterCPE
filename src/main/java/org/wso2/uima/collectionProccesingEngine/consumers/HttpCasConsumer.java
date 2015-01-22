package org.wso2.uima.collectionProccesingEngine.consumers;

import org.apache.axiom.om.util.Base64;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.SystemDefaultHttpClient;
import org.apache.log4j.Logger;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.collection.CasConsumer_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceProcessException;
import org.wso2.uima.collectionProccesingEngine.consumers.util.KeyStoreUtil;
import org.wso2.uima.types.LocationIdentification;
import org.wso2.uima.types.TrafficLevelIdentifier;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;


/**
 * Created by supun on 1/19/15.
 */
public class HttpCasConsumer  extends CasConsumer_ImplBase {

    private HttpClient httpClient;
    private JCas jCas;
    String username;
    String password;
    String url;
    private static Logger logger = Logger.getLogger(HttpCasConsumer.class);

    public void  initialize() throws ResourceInitializationException {
        httpClient = new SystemDefaultHttpClient();
        KeyStoreUtil.setTrustStoreParams();

        username = "admin";
        password = "admin";
        url = "https://localhost:9443/endpoints/HttpAdapter/TrafficUpdate";

    }
    @Override
    public void processCas(CAS cas) throws ResourceProcessException {

        try {
            jCas = cas.getJCas();
        } catch (CASException e) {
            throw new ResourceProcessException(e);
        }

        String tweetText = jCas.getDocumentText();
        String locationString="";

        FSIndex locationIndex = jCas.getAnnotationIndex(LocationIdentification.type);
        for (Iterator<LocationIdentification> it = locationIndex.iterator(); it.hasNext();) {
            LocationIdentification annotation = it.next();
            //    System.out.println("AN2...(" + annotation.getBegin() + "," +
            //      annotation.getEnd() + "): " +
            //      annotation.getCoveredText());

            locationString = locationString + annotation.getCoveredText()+"|";
        }

        String trafficLevel = "";
        FSIndex trafficLevelIndex =
                jCas.getAnnotationIndex(TrafficLevelIdentifier.type);
        for(Iterator<TrafficLevelIdentifier> it = trafficLevelIndex.iterator(); it.hasNext();){
            TrafficLevelIdentifier level = it.next();
            trafficLevel = level.getTrafficLevel();
        }

        Logger.getLogger(DataBridgeCasConsumer.class).info("Annotated Text :  "+locationString.trim());
        Logger.getLogger(DataBridgeCasConsumer.class).info("Annotated Text :  "+trafficLevel);

       // if (!locationString.equals("") && !trafficLevel.equals("Random") )
            publish(tweetText, locationString, trafficLevel);

    }

    public void publish(String tweetText, String locationString, String trafficLevel){

        Date date= new Date();
        String time=new Timestamp(date.getTime()).toString();

        try {
            HttpPost method = new HttpPost(url);

            if (httpClient != null) {
                String[] xmlElements = new String[]
                        {
                      " <events>"+
                           " <event>"+
                                "<metaData>"+
                                    "<TimeStamp>"+time+"</TimeStamp>"+
                                "</metaData>"+
                                "<payloadData>"+
                                    "<Location>"+locationString+"</Location>"+
                                    "<TrafficLevel>"+trafficLevel+"</TrafficLevel>"+
                                    "<TweetText>"+tweetText+"</TweetText>"+
                         "</payloadData>"+
              "</event>"+
                "</events>"
                        };

                try {
                    for (String xmlElement : xmlElements) {
                        StringEntity entity = new StringEntity(xmlElement);
                        method.setEntity(entity);
                        if (url.startsWith("https")) {
                            processAuthentication(method, username, password);
                        }
                        httpClient.execute(method).getEntity().getContent().close();
                    }
                } catch (Exception e) {
                    System.out.println("Caught here");
                    e.printStackTrace();
                }
                System.out.println("Sending Success via HTTP ");
                Thread.sleep(500); // We need to wait some time for the message to be sent

            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private static void processAuthentication(HttpPost method, String username, String password) {
        if (username != null && username.trim().length() > 0) {
            method.setHeader("Authorization", "Basic " + Base64.encode(
                    (username + ":" + password).getBytes()));
        }
    }
}
