
/* First created by JCasGen Wed Jan 14 11:33:14 IST 2015 */
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
 * Updated by JCasGen Wed Jan 14 11:33:24 IST 2015
 * @generated */
public class HashTag_Type extends Annotation_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {
	  return fsGenerator;
  }
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (HashTag_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = HashTag_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new HashTag(addr, HashTag_Type.this);
  			   HashTag_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new HashTag(addr, HashTag_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = HashTag.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.wso2.uima.types.HashTag");
 
  /** @generated */
  final Feature casFeat_hashtag;
  /** @generated */
  final int     casFeatCode_hashtag;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getHashtag(int addr) {
        if (featOkTst && casFeat_hashtag == null)
      jcas.throwFeatMissing("hashtag", "org.wso2.uima.types.HashTag");
    return ll_cas.ll_getStringValue(addr, casFeatCode_hashtag);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setHashtag(int addr, String v) {
        if (featOkTst && casFeat_hashtag == null)
      jcas.throwFeatMissing("hashtag", "org.wso2.uima.types.HashTag");
    ll_cas.ll_setStringValue(addr, casFeatCode_hashtag, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public HashTag_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_hashtag = jcas.getRequiredFeatureDE(casType, "hashtag", "uima.cas.String", featOkTst);
    casFeatCode_hashtag  = (null == casFeat_hashtag) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_hashtag).getCode();

  }
}



    