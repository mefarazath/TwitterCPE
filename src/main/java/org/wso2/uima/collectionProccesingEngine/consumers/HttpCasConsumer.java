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
 * Created by Supun on 1/19/15.
 */
public class HttpCasConsumer  extends CasConsumer_ImplBase {

    private HttpClient httpClient;

    // Config parameter names
    private static final String PARAM_HTTP_ENDPOINT = "httpEndPoint";
    private static final String PARAM_USERNAME = "username";
    private static final String PARAM_PASSWORD = "password";


    private String username;
    private String password;
    private String httpEndPoint;

    private static Logger logger = Logger.getLogger(HttpCasConsumer.class);

    public void  initialize() throws ResourceInitializationException {
    /*    SSLSocketFactory sf = null;
        try {
            sf = new SSLSocketFactory(
                    SSLContext.getDefault(),
                    SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        Scheme sch = new Scheme("https", 443, sf);*/
        httpClient = new SystemDefaultHttpClient();
      //  httpClient.getConnectionManager().getSchemeRegistry().register(sch);


        KeyStoreUtil.setTrustStoreParams();

        username = (String)getConfigParameterValue(PARAM_USERNAME);
        password = (String)getConfigParameterValue(PARAM_PASSWORD);
        httpEndPoint = (String)getConfigParameterValue(PARAM_HTTP_ENDPOINT);

    }
    @Override
    public void processCas(CAS cas) throws ResourceProcessException {

        JCas jCas;
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

            if(!locationString.contains(annotation.getCoveredText()))
                locationString = locationString + annotation.getCoveredText()+" ";
        }

        String trafficLevel = "";
        FSIndex trafficLevelIndex =
                jCas.getAnnotationIndex(TrafficLevelIdentifier.type);
        for(Iterator<TrafficLevelIdentifier> it = trafficLevelIndex.iterator(); it.hasNext();){
            TrafficLevelIdentifier level = it.next();
            trafficLevel = level.getTrafficLevel();
        }

        logger.debug("Annotated Location :  " + locationString.trim());
        logger.debug("Annotated Traffic :  " + trafficLevel);

       if (!locationString.equals(""))
            publish(tweetText, locationString, trafficLevel);
        //TODO write a Util class
    }

    public void publish(String tweetText, String locationString, String trafficLevel){

        Date date= new Date();
        String time=new Timestamp(date.getTime()).toString();

        try {
            HttpPost method = new HttpPost(httpEndPoint);

            if (httpClient != null) {
                String[] xmlElements = new String[]
                        {
                      "<events>"+
                            "<event>"+
                                "<metaData>"+
                                        "<Timestamp>"+time+"</Timestamp>"+
                                "</metaData>"+

                                "<payloadData>"+

                                    "<Traffic_Location>"+locationString+"</Traffic_Location>"+
                                    "<Traffic_Level>"+trafficLevel+"</Traffic_Level>"+
                                    "<Twitter_Text>"+tweetText+"</Twitter_Text>"+

                                "</payloadData>"+

                            "</event>"+
                        "</events>"
                        };

                try {
                    for (String xmlElement : xmlElements) {
                        StringEntity entity = new StringEntity(xmlElement);
                        method.setEntity(entity);
                        if (httpEndPoint.startsWith("https")) {
                            processAuthentication(method, username, password);
                        }
                        httpClient.execute(method).getEntity().getContent().close();
                        logger.info("Event Published Successfully to "+ httpEndPoint+"\n");
                    }
                } catch (Exception e) {
                    logger.error("Error While Sending Events to HTTP Endpoint : Connection Refused");
                   // e.printStackTrace();
                }

                Thread.sleep(500); // We need to wait some time for the message to be sent

            }
        } catch (Throwable t) {
            logger.error("Unable to Connect to HTTP endpoint");
            //TODO remove print stacktrace with logger,e
            //t.printStackTrace();
        }
    }

    private static void processAuthentication(HttpPost method, String username, String password) {
        if (username != null && username.trim().length() > 0) {
            method.setHeader("Authorization", "Basic " + Base64.encode(
                    (username + ":" + password).getBytes()));
        }
    }
}
