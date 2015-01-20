package org.wso2.uima.cpe.consumer;

/**
 * Created by vidura on 1/19/15.
 */

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.SystemDefaultHttpClient;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.collection.CasConsumer_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceProcessException;
import org.eclipse.osgi.internal.signedcontent.Base64;
import org.wso2.uima.types.LocationIdentification;
import org.wso2.uima.types.TrafficLevelIdentifier;
import twitter4j.Logger;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;

class SoapConsumer extends CasConsumer_ImplBase{

    private String xmlElement;
    private HttpClient httpClient;
    String username;
    String password;
    String url;

    @Override
    public void processCas(CAS cas) throws ResourceProcessException {
        // run the sample document through the pipeline
        JCas output = null;
        try {
            output = cas.getJCas();
        } catch (CASException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }

        //properties
        String tweetText = "\n" + output.getDocumentText();
        String locationString = "\n";
        String trafficLevel = "";

        Date date = new Date();
        String time = new Timestamp(date.getTime()).toString();

        FSIndex locationIndex = output.getAnnotationIndex(LocationIdentification.type);
        for (Iterator<LocationIdentification> it = locationIndex.iterator(); it.hasNext(); ) {
            LocationIdentification annotation = it.next();
            locationString = locationString + annotation.getCoveredText() + "|";
        }

        FSIndex trafficLevelIndex =
                output.getAnnotationIndex(TrafficLevelIdentifier.type);
        for (Iterator<TrafficLevelIdentifier> it = trafficLevelIndex.iterator(); it.hasNext(); ) {
            TrafficLevelIdentifier level = it.next();
            trafficLevel = level.getTrafficLevel();
        }

        Logger.getLogger(TwitterCPE_Consumer.class).info("Annotated Text :  " + locationString.trim());
        Logger.getLogger(TwitterCPE_Consumer.class).info("Annotated Text :  " + trafficLevel);

        xmlElement = "<events>" +
                " <event>" +
                " <metaData>" +
                " <timeStamp>" + time + "</timeStamp>" +
                " </metaData>" +
                " <payloadData>" +
                " <Location>" + locationString + "</Location>" +
                " <TrafficLevel>" + trafficLevel + "</TrafficLevel>" +
                " <TweetText>" + tweetText + "</TweetText>" +
                " </payloadData>" +
                " </event>" +
                " </events>";

        try {
            HttpPost method = new HttpPost(url);

            StringEntity entity = new StringEntity(xmlElement);
            method.setEntity(entity);
            if (url.startsWith("https")) {
                processAuthentication(method, username, password);
            }
            httpClient.execute(method).getEntity().getContent().close();

            Thread.sleep(500); // We need to wait some time for the message to be sent

        } catch (Throwable t) {
            t.printStackTrace();
        }

        /*
        xmlElement = "<httpConsumer:org.wso2.uima.TwitterExtractedFeed xmlns:httpConsumer=\"http://samples.wso2.org/\">\n" +
                    " <httpConsumer:trafficUpdate>" +
                    " <httpConsumer:meta_timeStamp>" + time + "</httpConsumer:meta_timeStamp>\n" +
                    " <httpConsumer:Location>" + locationString + "</httpConsumer:Location>\n" +
                    " <httpConsumer:TrafficLevel>" + trafficLevel + "</httpConsumer:TrafficLevel>\n" +
                    " <httpConsumer:TweetText>" + tweetText + "</httpConsumer:TweetText>\n" +
                    " </httpConsumer:trafficUpdate>\n" +
                    " </httpConsumer:org.wso2.uima.TwitterExtractedFeed>";
        */
    }


    @Override
    public void initialize() throws ResourceInitializationException {

        KeyStoreUtil.setTrustStoreParams();

        username = "admin";
        password = "admin";
        url = "https://10.100.4.12:9443/services/SoapAdaptor/trafficUpdate";

        httpClient = new SystemDefaultHttpClient();

    }


    private static void processAuthentication(HttpPost method, String username, String password) {
        if (username != null && username.trim().length() > 0) {
            method.setHeader("Authorization", "Basic " + Base64.encode(
                    (username + ":" + password).getBytes()));
        }
    }

}
