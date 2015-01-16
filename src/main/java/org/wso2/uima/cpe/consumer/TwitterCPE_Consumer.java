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
    public static final String STREAM_NAME1 = "org.uima.twitterfeed.in";
    public static final String VERSION1 = "1.0.0";
    public static String streamID = "org.uima.twitterfeed.in:1.0.0";
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

        String annotationString1="", annotationString2=null;
	  
/*	  FSIndex index1 = output.getAnnotationIndex(NounPhraseAnnotation.type);
	  for (Iterator<NounPhraseAnnotation> it = index1.iterator(); it.hasNext();) {
	    NounPhraseAnnotation annotation = it.next();
	//    System.out.println("AN1...(" + annotation.getBegin() + "," + 
	//      annotation.getEnd() + "): " + 
	//      annotation.getCoveredText());
	    annotationString1 = annotationString1 + annotation.getCoveredText();
	  }	*/

        FSIndex index2 = output.getAnnotationIndex(LocationIdentification.type);
        for (Iterator<LocationIdentification> it = index2.iterator(); it.hasNext();) {
            LocationIdentification annotation = it.next();
            //    System.out.println("AN2...(" + annotation.getBegin() + "," +
            //      annotation.getEnd() + "): " +
            //      annotation.getCoveredText());

            annotationString2 = annotationString2 + annotation.getCoveredText()+" | ";
        }

        Logger.getLogger(TwitterCPE_Consumer.class).info("Annotated Text :  "+annotationString2);
        //Publish event for a valid stream
        if (!streamID.isEmpty()) {
            System.out.println("Stream ID: " + streamID+"  Published");

            try {
                twittercounter++;
                publishEvents(dataPublisher, streamID, annotationString2);
            } catch (AgentException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            //            System.out.println("Events published : " + sentEventCount);

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                //ignore
            }


        }

    }


    @Override
    public void initialize() throws ResourceInitializationException {

        String streamId1 = null;
        System.out.println("Starting Statistics Agent");

        KeyStoreUtil.setTrustStoreParams();

        String host = "localhost";
        String port = "7611";
        String username = "admin";
        String password = "admin";

        try {
            dataPublisher = new DataPublisher("tcp://localhost:7611", "admin", "admin");
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
            streamId1 = dataPublisher.findStream(STREAM_NAME1, VERSION1);
            System.out.println("Stream already defined");

        } catch (NoStreamDefinitionExistException | AgentException | StreamDefinitionException e) {
            try {
                streamId1 = dataPublisher.defineStream("{" +
                        "  'name':'" + STREAM_NAME1 + "'," +
                        "  'version':'" + VERSION1 + "'," +
                        "  'nickName': 'Statistics'," +
                        "  'description': 'Service statistics'," +
                        "  'metaData':[" +
                        "          {'name':'feed_count','type':'INT'}," +
                        "          {'name':'feed_timestamp','type':'STRING'}," +
                        "  ]," +
                        "  'payloadData':[" +
                        "          {'name':'traffic_location','type':'STRING'}" +
                      //  "          {'name':'traffic_level','type':'STRING'}," +
                        "  ]" +
                        "}");
                Logger.getLogger(TwitterCPE_Consumer.class).info("Stream was not found");
            } catch (AgentException | MalformedStreamDefinitionException
                    | StreamDefinitionException
                    | DifferentStreamDefinitionAlreadyDefinedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            System.out.println("Catch reached");
            streamID=streamId1;
        }

        System.out.println("/////////////////" + streamId1);
        super.initialize();

    }

    private static void publishEvents(DataPublisher dataPublisher, String streamId, String annotation2)
            throws AgentException {

        Date date= new Date();
        String time=new Timestamp(date.getTime()).toString();


        Object[] meta = new Object[]{
                twittercounter,
                time
        };

        Object[] payload = new Object[]{
             //   annotation1,
                annotation2
        };

        Object[] correlation = null;

        Event statisticsEvent = new Event(streamId, System.currentTimeMillis(),
                meta, correlation, payload);
        dataPublisher.publish(statisticsEvent);
    }

}