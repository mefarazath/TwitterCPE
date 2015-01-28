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

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;
import org.apache.uima.jcas.tcas.Annotation;


/**
 * Updated by JCasGen Sun Jan 18 00:11:37 IST 2015
 * XML source: /home/achintha/workspace/UIMAAnnotatorTest/desc/TrafficLevelAnalysisEngine.xml
 *
 * @generated
 */
public class TrafficLevelIdentifier extends Annotation {
    /**
     * @generated
     * @ordered
     */
    @SuppressWarnings("hiding")
    public final static int typeIndexID = JCasRegistry.register(TrafficLevelIdentifier.class);
    /**
     * @generated
     * @ordered
     */
    @SuppressWarnings("hiding")
    public final static int type = typeIndexID;

    /**
     * @return index of the type
     * @generated
     */
    @Override
    public int getTypeIndexID() {
        return typeIndexID;
    }

    /**
     * Never called.  Disable default constructor
     *
     * @generated
     */
    protected TrafficLevelIdentifier() {/* intentionally empty block */}

    /**
     * Internal - constructor used by generator
     *
     * @param addr low level Feature Structure reference
     * @param type the type of this Feature Structure
     * @generated
     */
    public TrafficLevelIdentifier(int addr, TOP_Type type) {
        super(addr, type);
        readObject();
    }

    /**
     * @param jcas JCas to which this Feature Structure belongs
     * @generated
     */
    public TrafficLevelIdentifier(JCas jcas) {
        super(jcas);
        readObject();
    }

    /**
     * @param jcas  JCas to which this Feature Structure belongs
     * @param begin offset to the begin spot in the SofA
     * @param end   offset to the end spot in the SofA
     * @generated
     */
    public TrafficLevelIdentifier(JCas jcas, int begin, int end) {
        super(jcas);
        setBegin(begin);
        setEnd(end);
        readObject();
    }

    /**
     * <!-- begin-user-doc -->
     * Write your own initialization here
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    private void readObject() {/*default - does nothing empty block */}


    //*--------------*
    //* Feature: TrafficLevel

    /**
     * getter for TrafficLevel - gets
     *
     * @return value of the feature
     * @generated
     */
    public String getTrafficLevel() {
        if (TrafficLevelIdentifier_Type.featOkTst && ((TrafficLevelIdentifier_Type) jcasType).casFeat_TrafficLevel == null)
            jcasType.jcas.throwFeatMissing("TrafficLevel", "org.apache.uima.tutorial.TrafficLevelIdentifier");
        return jcasType.ll_cas.ll_getStringValue(addr, ((TrafficLevelIdentifier_Type) jcasType).casFeatCode_TrafficLevel);
    }

    /**
     * setter for TrafficLevel - sets
     *
     * @param v value to set into the feature
     * @generated
     */
    public void setTrafficLevel(String v) {
        if (TrafficLevelIdentifier_Type.featOkTst && ((TrafficLevelIdentifier_Type) jcasType).casFeat_TrafficLevel == null)
            jcasType.jcas.throwFeatMissing("TrafficLevel", "org.apache.uima.tutorial.TrafficLevelIdentifier");
        jcasType.ll_cas.ll_setStringValue(addr, ((TrafficLevelIdentifier_Type) jcasType).casFeatCode_TrafficLevel, v);
    }
}

    