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

/* First created by JCasGen Sun Jan 18 00:11:37 IST 2015 */
package org.wso2.uima.types;

import org.apache.uima.cas.Feature;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.tcas.Annotation_Type;

/**
 * Updated by JCasGen Sun Jan 18 00:11:37 IST 2015
 *
 * @generated
 */
public class TrafficLevelIdentifier_Type extends Annotation_Type {
    /**
     * @return the generator for this type
     * @generated
     */
    @Override
    protected FSGenerator getFSGenerator() {
        return fsGenerator;
    }

    /**
     * @generated
     */
    private final FSGenerator fsGenerator =
            new FSGenerator() {
                public FeatureStructure createFS(int addr, CASImpl cas) {
                    if (TrafficLevelIdentifier_Type.this.useExistingInstance) {
                        // Return eq fs instance if already created
                        FeatureStructure fs = TrafficLevelIdentifier_Type.this.jcas.getJfsFromCaddr(addr);
                        if (null == fs) {
                            fs = new TrafficLevelIdentifier(addr, TrafficLevelIdentifier_Type.this);
                            TrafficLevelIdentifier_Type.this.jcas.putJfsFromCaddr(addr, fs);
                            return fs;
                        }
                        return fs;
                    } else return new TrafficLevelIdentifier(addr, TrafficLevelIdentifier_Type.this);
                }
            };
    /**
     * @generated
     */
    @SuppressWarnings("hiding")
    public final static int typeIndexID = TrafficLevelIdentifier.typeIndexID;
    /**
     * @generated
     * @modifiable
     */
    @SuppressWarnings("hiding")
    public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.tutorial.TrafficLevelIdentifier");

    /**
     * @generated
     */
    final Feature casFeat_TrafficLevel;
    /**
     * @generated
     */
    final int casFeatCode_TrafficLevel;

    /**
     * @param addr low level Feature Structure reference
     * @return the feature value
     * @generated
     */
    public String getTrafficLevel(int addr) {
        if (featOkTst && casFeat_TrafficLevel == null)
            jcas.throwFeatMissing("TrafficLevel", "org.apache.uima.tutorial.TrafficLevelIdentifier");
        return ll_cas.ll_getStringValue(addr, casFeatCode_TrafficLevel);
    }

    /**
     * @param addr low level Feature Structure reference
     * @param v    value to set
     * @generated
     */
    public void setTrafficLevel(int addr, String v) {
        if (featOkTst && casFeat_TrafficLevel == null)
            jcas.throwFeatMissing("TrafficLevel", "org.apache.uima.tutorial.TrafficLevelIdentifier");
        ll_cas.ll_setStringValue(addr, casFeatCode_TrafficLevel, v);
    }


    /**
     * initialize variables to correspond with Cas Type and Features
     *
     * @param jcas    JCas
     * @param casType Type
     * @generated
     */
    public TrafficLevelIdentifier_Type(JCas jcas, Type casType) {
        super(jcas, casType);
        casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl) this.casType, getFSGenerator());


        casFeat_TrafficLevel = jcas.getRequiredFeatureDE(casType, "TrafficLevel", "uima.cas.String", featOkTst);
        casFeatCode_TrafficLevel = (null == casFeat_TrafficLevel) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl) casFeat_TrafficLevel).getCode();

    }
}



    