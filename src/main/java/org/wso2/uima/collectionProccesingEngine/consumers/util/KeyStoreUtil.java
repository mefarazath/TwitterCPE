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
package org.wso2.uima.collectionProccesingEngine.consumers.util;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.io.File;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 *
 */

public class KeyStoreUtil {

    static File filePath = new File("conf/security");

    public static void setTrustStoreParams() {
        String trustStore = filePath.getAbsolutePath();
        System.setProperty("javax.net.ssl.trustStore", trustStore + "/client-truststore.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");

        SSLContext ctx;
        try {
            ctx = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e) {
            // TODO Handle the exception with a log message
            throw new NullPointerException("No Algorithm 'TLS' found for SSLContext object");
        }

        try {
            ctx.init(new KeyManager[0], new TrustManager[]{new DefaultTrustManager()}, new SecureRandom());
        } catch (KeyManagementException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        SSLContext.setDefault(ctx);

    }

}
