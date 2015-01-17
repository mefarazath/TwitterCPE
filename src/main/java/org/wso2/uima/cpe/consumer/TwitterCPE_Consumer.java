package org.wso2.uima.cpe.consumer;

import com.sun.istack.internal.logging.Logger;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.collection.CasConsumer_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceProcessException;
import org.wso2.carbon.databridge.agent.thrift.DataPublisher;
import org.wso2.carbon.databridge.agent.thrift.exception.AgentException;
import org.wso2.carbon.databridge.commons.Event;
import org.wso2.carbon.databridge.commons.exception.*;
import org.wso2.uima.types.LocationIdentification;

import java.net.MalformedURLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;

public class TwitterCPE_Consumer extends CasConsumer_ImplBase{

    public static int twittercounter= 0;

    public static final String STREAM_NAME = "org.wso2.uima.TwitterExtractedFeed";
    public static final String VERSION = "1.0.0";
    public static String streamID = null;
    public static DataPublisher dataPublisher;

    @Override
    public void processCas(CAS cas) throws ResourceProcessException {

        // run the sample document through the pipeline
        JCas output= null;
        try {
            output = cas.getJCas();
        } catch (CASException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }

        String locationString="";

        FSIndex index2 = output.getAnnotationIndex(LocationIdentification.type);
        for (Iterator<LocationIdentification> it = index2.iterator(); it.hasNext();) {
            LocationIdentification annotation = it.next();
            //    System.out.println("AN2...(" + annotation.getBegin() + "," +
            //      annotation.getEnd() + "): " +
            //      annotation.getCoveredText());

            locationString = locationString + annotation.getCoveredText()+" ";
        }

        Logger.getLogger(TwitterCPE_Consumer.class).info("Annotated Text :  "+locationString.trim());

        //Publish event for a valid stream
        if (streamID != null && !locationString.isEmpty()) {
            System.out.println("Stream ID: " + streamID+"  to be Published");

            try {

                twittercounter++;
                publishEvents(dataPublisher, streamID, locationString.trim());

            } catch (AgentException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                //ignore
            }


        }

    }


    @Override
    public void initialize() throws ResourceInitializationException {
      // String streamId1 = null;
      //  System.out.println("Starting Statistics Agent");

        KeyStoreUtil.setTrustStoreParams();

        String host = "localhost";
        String port = "7611";
        String username = "admin";
        String password = "admin";

       String url = "tcp://"+host+":"+port;

        try {

            dataPublisher = new DataPublisher(url,username,password);
            twitter4j.Logger.getLogger(TwitterCPE_Consumer.class).debug("Data Publisher Created");

        } catch (MalformedURLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (AgentException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (AuthenticationException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (TransportException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }


        try {
            streamID = dataPublisher.findStream(STREAM_NAME, VERSION);
            twitter4j.Logger.getLogger( TwitterCPE_Consumer.class).debug("Stream Definition Already Exists");

        } catch (NoStreamDefinitionExistException | AgentException | StreamDefinitionException e) {
            try {
                 streamID = dataPublisher.defineStream("{" +
                        " 'name':'"+STREAM_NAME+"'," +
                        " 'version':'"+ VERSION +"'," +
                        " 'nickName': 'TwitterCEP'," +
                        " 'description': 'Some Desc'," +
                        " 'tags':['UIMA', 'CEP']," +
                        " 'metaData':[" +
                        "       {'name':'timeStamp','type':'STRING'}" +
                        " ]," +
                        " 'payloadData':[" +
                        "       {'name':'Location','type':'STRING'}" +
                        " ]" +
                        "}");

                twitter4j.Logger.getLogger(TwitterCPE_Consumer.class).info("Stream ID : "+streamID);
                twitter4j.Logger.getLogger(TwitterCPE_Consumer.class).debug("Stream was not found and defined successfully");

            } catch (AgentException | MalformedStreamDefinitionException
                    | StreamDefinitionException
                    | DifferentStreamDefinitionAlreadyDefinedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                twitter4j.Logger.getLogger(TwitterCPE_Consumer.class).debug("Stream Definition Failed");
            }


        }

//      /  System.out.println("/////////////////   " + streamId1);
        super.initialize();

    }

    private static void publishEvents(DataPublisher dataPublisher, String streamId, String annotation)
            throws AgentException {

        Date date= new Date();
        String time=new Timestamp(date.getTime()).toString();


        Object[] meta = new Object[]{
                time
        };

        Object[] payload = new Object[]{
                annotation
        };

        Object[] correlation = new Object[]{};

        Event statisticsEvent = new Event(streamId, System.currentTimeMillis(),
                meta, correlation, payload);

        dataPublisher.publish(statisticsEvent);
        twitter4j.Logger.getLogger(TwitterCPE_Consumer.class).info("Event Published Successfully");
    }

}