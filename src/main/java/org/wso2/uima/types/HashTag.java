

/* First created by JCasGen Wed Jan 14 11:33:14 IST 2015 */
package org.wso2.uima.types;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;
import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Wed Jan 14 11:33:24 IST 2015
 * XML source: /home/farazath/eclipse_workspace/TwitterCPE/descriptors/analysis-engine/HashTagDescriptor.xml
 * @generated */
public class HashTag extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(HashTag.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated
   * @return index of the type  
   */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected HashTag() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public HashTag(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public HashTag(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public HashTag(JCas jcas, int begin, int end) {
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
  //* Feature: hashtag

  /** getter for hashtag - gets 
   * @generated
   * @return value of the feature 
   */
  public String getHashtag() {
    if (HashTag_Type.featOkTst && ((HashTag_Type)jcasType).casFeat_hashtag == null)
      jcasType.jcas.throwFeatMissing("hashtag", "org.wso2.uima.types.HashTag");
    return jcasType.ll_cas.ll_getStringValue(addr, ((HashTag_Type)jcasType).casFeatCode_hashtag);}
    
  /** setter for hashtag - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setHashtag(String v) {
    if (HashTag_Type.featOkTst && ((HashTag_Type)jcasType).casFeat_hashtag == null)
      jcasType.jcas.throwFeatMissing("hashtag", "org.wso2.uima.types.HashTag");
    jcasType.ll_cas.ll_setStringValue(addr, ((HashTag_Type)jcasType).casFeatCode_hashtag, v);}
  }

    