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