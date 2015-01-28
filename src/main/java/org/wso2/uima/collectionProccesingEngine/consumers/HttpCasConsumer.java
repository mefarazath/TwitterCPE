/*
 *
 * Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

import org.apache.axiom.om.util.Base64;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.SystemDefaultHttpClient;
import org.apache.log4j.Logger;
import org.apache.uima.cas.CAS;
import org.apache.uima.collection.CasConsumer_ImplBase;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceProcessException;
import org.wso2.uima.collectionProccesingEngine.consumers.util.KeyStoreUtil;
import org.wso2.uima.collectionProccesingEngine.consumers.util.CasConsumerUtil;

import java.sql.Timestamp;
import java.util.Date;

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

        String tweetText = CasConsumerUtil.getTweetText(cas);
        String locationString = CasConsumerUtil.getLocationString(cas);
        String trafficLevel = CasConsumerUtil.getTrafficLevel(cas);

        if(locationString.isEmpty()){
            return;
        }

        logger.debug("Annotated Location :  " + locationString.trim());
        logger.debug("Annotated Traffic :  " + trafficLevel);

       if (!locationString.equals(""))
            publish(tweetText, locationString, trafficLevel);

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
                }

                Thread.sleep(500); // We need to wait some time for the message to be sent

            }
        } catch (Throwable t) {
            logger.error("Unable to Connect to HTTP endpoint");
        }
    }

    private static void processAuthentication(HttpPost method, String username, String password) {
        if (username != null && username.trim().length() > 0) {
            method.setHeader("Authorization", "Basic " + Base64.encode(
                    (username + ":" + password).getBytes()));
        }
    }
}
