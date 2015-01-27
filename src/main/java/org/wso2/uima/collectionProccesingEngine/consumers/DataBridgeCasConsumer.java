/*
*Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*WSO2 Inc. licenses this file to you under the Apache License,
*Version 2.0 (the "License"); you may not use this file except
*in compliance with the License.
*You may obtain a copy of the License at
*
*http://www.apache.org/licenses/LICENSE-2.0
*
*Unless required by applicable law or agreed to in writing,
*software distributed under the License is distributed on an
*"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*KIND, either express or implied. See the License for the
*specific language governing permissions and limitations
*under the License.
*/

package org.wso2.uima.collectionProccesingEngine.consumers;
import org.apache.log4j.Logger;
import org.apache.uima.cas.CAS;
import org.apache.uima.collection.CasConsumer_ImplBase;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceProcessException;
import org.wso2.carbon.databridge.agent.thrift.DataPublisher;
import org.wso2.carbon.databridge.agent.thrift.exception.AgentException;
import org.wso2.carbon.databridge.commons.Event;
import org.wso2.carbon.databridge.commons.StreamDefinition;
import org.wso2.carbon.databridge.commons.exception.*;
import org.wso2.uima.collectionProccesingEngine.consumers.util.KeyStoreUtil;
import org.wso2.uima.collectionProccesingEngine.consumers.util.TweetScanner;

import java.net.MalformedURLException;
import java.sql.Timestamp;
import java.util.Date;

public class DataBridgeCasConsumer extends CasConsumer_ImplBase {
    private static final String STREAM_NAME = "org.wso2.uima.TwitterExtractedInputFeed";
    private static final String VERSION = "1.0.0";
    private static final String PARAM_SERVER_URL = "serverURL";
    private static final String PARAM_USERNAME = "username";
    private static final String PARAM_PASSWORD = "password";
    private static String streamID = null;
    private static DataPublisher dataPublisher;
    private static Logger logger = Logger.getLogger(DataBridgeCasConsumer.class);
    private String url;
    private String username;
    private String password;
    @Override
    public void processCas(CAS cas) throws ResourceProcessException {

        String tweetText = TweetScanner.getTweetText(cas);
        String locationString = TweetScanner.getLocationString(cas);
        String trafficLevel = TweetScanner.getTrafficLevel(cas);

        if(locationString.isEmpty()){
            return;
        }

        logger.info("Annotated Location : " + locationString.trim());
        logger.info("Annotated Traffic : " + trafficLevel);
        if (streamID != null && !locationString.equals("")) {
            try {
                publishEvents(
                        dataPublisher,
                        streamID,
                        locationString.trim(),
                        trafficLevel,
                        tweetText
                );
            } catch (AgentException e) {
                logger.error("Unable to publish events due to errors in the data bridge", e);
            }
        }
    }
    @Override
    public void initialize() throws ResourceInitializationException {
        KeyStoreUtil.setTrustStoreParams();
        url = (String) getConfigParameterValue(PARAM_SERVER_URL);
        username = (String) getConfigParameterValue(PARAM_USERNAME);
        password = (String) getConfigParameterValue(PARAM_PASSWORD);
        try {
            dataPublisher = new DataPublisher(url, username, password);
            logger.debug("Data Publisher Created");
        } catch (MalformedURLException e) {
            logger.error("Unable to create the data publisher ", e);
        } catch (AgentException e) {
            logger.error("Unable to create the data publisher ", e);
        } catch (AuthenticationException e) {
            logger.error("Unable to create the data publisher ", e);
        } catch (TransportException e) {
            logger.error("Unable to create the data publisher ", e);
        }
        try {
            streamID = dataPublisher.findStream(STREAM_NAME, VERSION);
            logger.info("Stream Definition Already Exists");
        } catch (NoStreamDefinitionExistException | AgentException | StreamDefinitionException e) {
            try {
                StreamDefinition streamDef = new StreamDefinition(VERSION);
                streamDef.setNickName("TwitterCEP");
                streamDef.setDescription("Extracted Data Feed from Tweets");
                streamDef.addTag("UIMA");
                streamDef.addTag("CEP");
                streamID = dataPublisher.defineStream("{" +
                        " 'name':'" + STREAM_NAME + "'," +
                        " 'version':'" + VERSION + "'," +
                        " 'nickName': 'TwitterCEP'," +
                        " 'description': 'Some Desc'," +
                        " 'tags':['UIMA', 'CEP']," +
                        " 'metaData':[" +
                        " {'name':'timeStamp','type':'STRING'}" +
                        " ]," +
                        " 'payloadData':[" +
                        " {'name':'Location','type':'STRING'}," +
                        " {'name':'TrafficLevel','type':'STRING'}," +
                        " {'name':'TweetText','type':'STRING'}" +
                        " ]" +
                        "}");
                logger.debug("Stream ID : " + streamID);
                logger.debug("Stream was not found and defined successfully");
            } catch (AgentException | MalformedStreamDefinitionException
                    | StreamDefinitionException
                    | DifferentStreamDefinitionAlreadyDefinedException e1) {

                logger.debug("Stream Definition Failed");
            }
        }

    }

    private void publishEvents(DataPublisher dataPublisher, String streamId, String... payloadArgs)
            throws AgentException {
        Date date = new Date();
        String time = new Timestamp(date.getTime()).toString();
        Object[] meta = new Object[]{
                time
        };
        Object[] payload = payloadArgs;
// TODO ADD NULL
        Object[] correlation = new Object[]{};
        Event statisticsEvent = new Event(streamId, System.currentTimeMillis(),
                meta, correlation, payload);
        dataPublisher.publish(statisticsEvent);
        logger.info("Event Published Via DataBridge Successfully to " + url);
    }
}