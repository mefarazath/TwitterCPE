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

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.wso2.uima.types.LocationIdentification;

import java.io.InputStream;

/**
 * Annotate noun phrases in sentences from within blocks of text (marked up with
 * TextAnnotation) from either HTML or plain text documents. Using the OpenNLP
 * library and models, the incoming text is tokenized into sentences, then each
 * sentence is tokenized to words and POS tagged, and finally tokens are grouped
 * together into chunks. Of these chunks, only the noun phrases are annotated.
 */
public class LocationIdentifier extends JCasAnnotator_ImplBase {

	private SentenceDetectorME sentenceDetector;
	private TokenizerME tokenizer;
	private NameFinderME locationFinder;
	private ChunkerME chunker;

	private static Logger logger = Logger.getLogger(LocationIdentifier.class);

	@Override
	public void initialize(UimaContext ctx)
			throws ResourceInitializationException {
		super.initialize(ctx);
		InputStream smis = null;
		InputStream tmis = null;
		InputStream pmis = null;
		InputStream cmis = null;
		try {
			smis = getContext().getResourceAsStream("SentenceModel");
			SentenceModel smodel = new SentenceModel(smis);
			sentenceDetector = new SentenceDetectorME(smodel);
			smis.close();
			tmis = getContext().getResourceAsStream("TokenizerModel");
			TokenizerModel tmodel = new TokenizerModel(tmis);
			tokenizer = new TokenizerME(tmodel);
			tmis.close();
			pmis = getContext().getResourceAsStream("TokenNameFinderModel");
			TokenNameFinderModel pmodel = new TokenNameFinderModel(pmis);
			locationFinder = new NameFinderME(pmodel);
			pmis.close();
			cmis = getContext().getResourceAsStream("ChunkerModel");
			ChunkerModel cmodel = new ChunkerModel(cmis);
			chunker = new ChunkerME(cmodel);
			cmis.close();
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		} finally {
			IOUtils.closeQuietly(cmis);
			IOUtils.closeQuietly(pmis);
			IOUtils.closeQuietly(tmis);
			IOUtils.closeQuietly(smis);
		}
	}

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		String text = jcas.getDocumentText();
		Span[] sentSpans = sentenceDetector.sentPosDetect(jcas
				.getDocumentText());

		for (Span sentSpan : sentSpans) {
			String sentence = sentSpan.getCoveredText(text).toString();
			int start = sentSpan.getStart();
			Span[] tokSpans = tokenizer.tokenizePos(sentence);
			String[] tokens = new String[tokSpans.length];
			for (int i = 0; i < tokens.length; i++) {
				tokens[i] = tokSpans[i].getCoveredText(sentence).toString();
			}

			//System.out.println();
			logger.info("Tweet Text: "+jcas.getDocumentText());
			Span locationSpans[] = locationFinder.find(tokens);
			LocationIdentification annotation = new LocationIdentification(jcas);
			for (Span location: locationSpans) {
				annotation.setBegin(start + tokSpans[location.getStart()].getStart());
				annotation.setEnd(start + tokSpans[location.getEnd() - 1].getEnd());
				annotation.addToIndexes(jcas);
				logger.info("Location Detected : "+annotation.getCoveredText());
			}

			if(locationSpans.length == 0){
				logger.info("Location Unable to be Detected");
			}


		}
	}
}
