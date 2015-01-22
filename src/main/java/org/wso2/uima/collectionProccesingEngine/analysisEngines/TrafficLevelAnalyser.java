package org.wso2.uima.collectionProccesingEngine.analysisEngines;

import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import org.apache.commons.io.IOUtils;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.wso2.uima.types.TrafficLevelIdentifier;

import java.io.InputStream;

public class TrafficLevelAnalyser extends JCasAnnotator_ImplBase {

    private DocumentCategorizerME documentCategorizer;

    @Override
    public void process(JCas jcas) throws AnalysisEngineProcessException {

        String documentContent = jcas.getDocumentText();
        double[] classDistribution = documentCategorizer.categorize(documentContent);

        // get the predicted model
        String predictedCategory = documentCategorizer.getBestCategory(classDistribution);

        TrafficLevelIdentifier annotation = new TrafficLevelIdentifier(jcas);
        annotation.addToIndexes(jcas);
        annotation.setTrafficLevel(predictedCategory);
        //System.out.println("Annotation : "+annotation.getTrafficLevel());
    }

    public void initialize(UimaContext ctx)
            throws ResourceInitializationException {
        super.initialize(ctx);
        InputStream dmis = null;
        try {

            dmis = getContext().getResourceAsStream("DoccatModel");
            DoccatModel dmodel = new DoccatModel(dmis);
            documentCategorizer = new DocumentCategorizerME(dmodel);
            dmis.close();

        } catch (Exception e) {
            throw new ResourceInitializationException(e);
        } finally {
            IOUtils.closeQuietly(dmis);
        }
    }

}
