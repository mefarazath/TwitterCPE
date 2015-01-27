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

package org.wso2.uima.collectionProccesingEngine.analysisEngines;

import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.wso2.uima.types.TrafficLevelIdentifier;

import java.io.InputStream;

public class TrafficLevelAnalyser extends JCasAnnotator_ImplBase {

    private DocumentCategorizerME documentCategorizer;
    private static Logger logger = Logger.getLogger(TrafficLevelAnalyser.class);

    @Override
    public void process(JCas jcas) throws AnalysisEngineProcessException {

        String documentContent = jcas.getDocumentText();
        double[] classDistribution = documentCategorizer.categorize(documentContent);

        // get the predicted model
        String predictedCategory = documentCategorizer.getBestCategory(classDistribution);

        TrafficLevelIdentifier annotation = new TrafficLevelIdentifier(jcas);
        annotation.addToIndexes(jcas);
        annotation.setTrafficLevel(predictedCategory);
        logger.info("Traffic Level Annotated : " + annotation.getTrafficLevel() + "\n");

    }

    @Override
    public void initialize(UimaContext ctx)
            throws ResourceInitializationException {
        super.initialize(ctx);
        InputStream docStream = null;
        try {

            docStream = getContext().getResourceAsStream("DoccatModel");
            DoccatModel doccatModel = new DoccatModel(docStream);
            documentCategorizer = new DocumentCategorizerME(doccatModel);
            docStream.close();

        } catch (Exception e) {
            throw new ResourceInitializationException(e);
        } finally {
            IOUtils.closeQuietly(docStream);
        }
    }

}
