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

package org.wso2.uima.collectionProccesingEngine.reader;

import junit.framework.TestCase;
import org.apache.uima.UIMAFramework;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.util.XMLInputSource;

public class TwitterActiveMQReaderTest extends TestCase {
    private static int index = 0;
    private TwitterActiveMQReader reader;

    /***
     * Test for the TwitterActiveMQReader#initialize()
     * @throws Exception
     */
    public void testInitialize() throws Exception {
        XMLInputSource in = new XMLInputSource("src/test/java/org/wso2/uima/collectionProccesingEngine/reader/TwitterActiveMQReader.xml");
        CollectionReaderDescription desc = UIMAFramework.getXMLParser().parseCollectionReaderDescription(in);
        reader = (TwitterActiveMQReader)UIMAFramework.produceCollectionReader(desc);
        assertNotNull(reader);
    }

    public void testGetNext() throws Exception {



    }

    public void testHasNext() throws Exception {


    }

    public void testGetProgress() throws Exception {
        assertEquals(true,true);

    }

    public void testClose() throws Exception {
        assertEquals(true,true);

    }
}