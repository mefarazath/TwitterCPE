/*
 *
 * Copyright (c) 2005-2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * /
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
import org.wso2.uima.collectionProccesingEngine.consumers.util.CasConsumerUtil;

import java.net.MalformedURLException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Send the info extracted from CAS object and send it to the CEP via DataBridge.
 */

public class DataBridgeCasConsumer extends CasConsumer_ImplBase {

    private static final String PARAM_SERVER_URL = "serverURL";
    private static final String PARAM_USERNAME = "username";
    private static final String PARAM_PASSWORD = "password";
    private static final String PARAM_STREAM_NAME = "streamName";
    private static final String PARAM_STREAM_VERSION = "streamVersion";


    private String streamID = null;
    private DataPublisher dataPublisher;
    private static Logger logger = Logger.getLogger(DataBridgeCasConsumer.class);

    private String url;
    private String username;
    private String password;
    private String streamName;
    private String streamVersion;

    @Override
    public void processCas(CAS cas) throws ResourceProcessException {

        String tweetText = CasConsumerUtil.getTweetText(cas);
        String locationString = CasConsumerUtil.getLocationString(cas);
        String trafficLevel = CasConsumerUtil.getTrafficLevel(cas);

        if (locationString.isEmpty()) {
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
                logger.error("Unable to publish events to the data bridge to "+url, e);
            }
        }
    }

    @Override
    public void initialize() throws ResourceInitializationException {
        KeyStoreUtil.setTrustStoreParams();
        url = (String) getConfigParameterValue(PARAM_SERVER_URL);
        username = (String) getConfigParameterValue(PARAM_USERNAME);
        password = (String) getConfigParameterValue(PARAM_PASSWORD);
        streamName = (String)getConfigParameterValue(PARAM_STREAM_NAME);
        streamVersion = (String)getConfigParameterValue(PARAM_STREAM_VERSION);

        try {
            dataPublisher = new DataPublisher(url, username, password);
            logger.debug("Data Publisher Created");
        } catch (MalformedURLException e) {
            logger.error("Unable to create the data publisher to url: "+url, e);
        } catch (AgentException e) {
            logger.error("Unable to create the data publisher to url: "+url, e);
        } catch (AuthenticationException e) {
            logger.error("Unable to create the data publisher using username: "+username+" password: "+password+" to "+url, e);
        } catch (TransportException e) {
            logger.error("Unable to create the data publisher to url: "+url, e);
        }

        try {
                StreamDefinition streamDef = new StreamDefinition(streamVersion);
                streamDef.setNickName("TwitterCEP");
                streamDef.setDescription("Extracted Data Feed from Tweets");
                streamDef.addTag("UIMA");
                streamDef.addTag("CEP");

                streamID = dataPublisher.defineStream("{" +
                        " 'name':'" + streamName + "'," +
                        " 'version':'" + streamVersion + "'," +
                        " 'nickName': 'twitter Input Stream'," +
                        " 'description': 'Input stream to recieve the extracted details from the twitter feed into the CEP'," +
                        " 'tags':['UIMA', 'CEP']," +
                        " 'metaData':[" +
                        " {'name':'Timestamp','type':'STRING'}" +
                        " ]," +
                        " 'payloadData':[" +
                        " {'name':'Traffic_Location','type':'STRING'}," +
                        " {'name':'Traffic_Level','type':'STRING'}," +
                        " {'name':'Twitter_Text','type':'STRING'}" +
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

    /***
     *
     * @param dataPublisher gives the data publisher specified by username, password, url that used to sends the data.
     * @param streamId gives the stream id that is available with the dataPublisher.
     * @param payloadArgs the info that needs to be sent.
     * @throws AgentException
     */

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