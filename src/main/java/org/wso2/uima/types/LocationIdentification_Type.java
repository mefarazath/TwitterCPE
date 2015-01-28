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

/* First created by JCasGen Wed Jan 07 09:20:13 IST 2015 */
package org.wso2.uima.types;

import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.tcas.Annotation_Type;

/**
 * Updated by JCasGen Wed Jan 07 09:20:13 IST 2015
 *
 * @generated
 */
public class LocationIdentification_Type extends Annotation_Type {
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
                    if (LocationIdentification_Type.this.useExistingInstance) {
                        // Return eq fs instance if already created
                        FeatureStructure fs = LocationIdentification_Type.this.jcas.getJfsFromCaddr(addr);
                        if (null == fs) {
                            fs = new LocationIdentification(addr, LocationIdentification_Type.this);
                            LocationIdentification_Type.this.jcas.putJfsFromCaddr(addr, fs);
                            return fs;
                        }
                        return fs;
                    } else return new LocationIdentification(addr, LocationIdentification_Type.this);
                }
            };
    /**
     * @generated
     */
    @SuppressWarnings("hiding")
    public final static int typeIndexID = LocationIdentification.typeIndexID;
    /**
     * @generated
     * @modifiable
     */
    @SuppressWarnings("hiding")
    public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.tutorial.LocationIdentification");


    /**
     * initialize variables to correspond with Cas Type and Features
     *
     * @param jcas    JCas
     * @param casType Type
     * @generated
     */
    public LocationIdentification_Type(JCas jcas, Type casType) {
        super(jcas, casType);
        casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl) this.casType, getFSGenerator());

    }
}



    