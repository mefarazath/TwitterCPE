package org.wso2.uima.main;

import org.apache.log4j.Logger;
import org.apache.uima.UIMAFramework;
import org.apache.uima.cas.CAS;
import org.apache.uima.collection.CollectionProcessingEngine;
import org.apache.uima.collection.EntityProcessStatus;
import org.apache.uima.collection.StatusCallbackListener;
import org.apache.uima.collection.metadata.CpeDescription;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLInputSource;

import java.io.IOException;

import static org.apache.log4j.Logger.*;

/**
 * Created by farazath on 1/22/15.
 */
public class CEPwithActiveMQ {
    private static Logger logger = getLogger(CEPwithActiveMQ.class);

    public static void main(String[] args) throws IOException, InvalidXMLException, InterruptedException {

        org.apache.log4j.PropertyConfigurator.configure("conf/log4j.properties");

        XMLInputSource in = new XMLInputSource("descriptors/collection-processing-engine/twitterCPE.xml");
        CpeDescription cpe_desc = UIMAFramework.getXMLParser().parseCpeDescription(in);

        CollectionProcessingEngine cpe = null;

        logger.info("Application Initiated");
        Thread.sleep(5000);

        while(true){

            try {

                cpe = UIMAFramework.produceCollectionProcessingEngine(cpe_desc);
                cpe.process();

                Thread.sleep(60000);

                if(!cpe.isProcessing()) {
                    logger.info("\n******** Performance Report *********\n"+cpe.getPerformanceReport().toString());
                }

            } catch (ResourceInitializationException e) {
                e.printStackTrace();
                break;
            }

        }

    }

}

class StatusCallBackCPE implements StatusCallbackListener {

    public StatusCallBackCPE(){
        org.apache.log4j.PropertyConfigurator.configure("conf/log4j.properties");

    }
    @Override
    public void aborted() {
        // TODO Auto-generated method stub
        getLogger(CEPwithActiveMQ.class).info("CPE aborted");
        System.exit(1);
    }

    @Override
    public void batchProcessComplete() {
        // TODO Auto-generated method stub

    }

    @Override
    public void collectionProcessComplete() {
        // TODO Auto-generated method stub
        getLogger(CEPwithActiveMQ.class).info("CPE Processing Completed");

    }

    @Override
    public void initializationComplete() {
        // TODO Auto-generated method stub
        getLogger(CEPwithActiveMQ.class).info("CPE Initialization Completed");
    }

    @Override
    public void paused() {
        // TODO Auto-generated method stub
        System.out.println("CPE is paused");
    }

    @Override
    public void resumed() {
        // TODO Auto-generated method stub
        System.out.println("CPE is resumed");
    }

    @Override
    public void entityProcessComplete(CAS arg0, EntityProcessStatus arg1) {
        // TODO Auto-generated method stub

    }
}
