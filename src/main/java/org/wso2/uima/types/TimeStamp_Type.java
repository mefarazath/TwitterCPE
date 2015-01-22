
/* First created by JCasGen Thu Jan 22 13:35:59 IST 2015 */
package org.wso2.uima.types;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.tcas.Annotation_Type;

/** 
 * Updated by JCasGen Thu Jan 22 13:43:30 IST 2015
 * @generated */
public class TimeStamp_Type extends Annotation_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (TimeStamp_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = TimeStamp_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new TimeStamp(addr, TimeStamp_Type.this);
  			   TimeStamp_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new TimeStamp(addr, TimeStamp_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = TimeStamp.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.wso2.uima.types.TimeStamp");
 
  /** @generated */
  final Feature casFeat_TimeStamp;
  /** @generated */
  final int     casFeatCode_TimeStamp;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public long getTimeStamp(int addr) {
        if (featOkTst && casFeat_TimeStamp == null)
      jcas.throwFeatMissing("TimeStamp", "org.wso2.uima.types.TimeStamp");
    return ll_cas.ll_getLongValue(addr, casFeatCode_TimeStamp);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setTimeStamp(int addr, long v) {
        if (featOkTst && casFeat_TimeStamp == null)
      jcas.throwFeatMissing("TimeStamp", "org.wso2.uima.types.TimeStamp");
    ll_cas.ll_setLongValue(addr, casFeatCode_TimeStamp, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public TimeStamp_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_TimeStamp = jcas.getRequiredFeatureDE(casType, "TimeStamp", "uima.cas.Long", featOkTst);
    casFeatCode_TimeStamp  = (null == casFeat_TimeStamp) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_TimeStamp).getCode();

  }
}



    