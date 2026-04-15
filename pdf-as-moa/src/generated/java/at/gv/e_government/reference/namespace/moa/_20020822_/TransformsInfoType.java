
package at.gv.e_government.reference.namespace.moa._20020822_;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import org.w3._2000._09.xmldsig_.TransformsType;


/**
 * <p>Java class for TransformsInfoType complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="TransformsInfoType">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element ref="{http://www.w3.org/2000/09/xmldsig#}Transforms" minOccurs="0"/>
 *         <element name="FinalDataMetaInfo" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}FinalDataMetaInfoType"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TransformsInfoType", propOrder = {
    "transforms",
    "finalDataMetaInfo"
})
public class TransformsInfoType {

    @XmlElement(name = "Transforms", namespace = "http://www.w3.org/2000/09/xmldsig#")
    protected TransformsType transforms;
    @XmlElement(name = "FinalDataMetaInfo", required = true)
    protected FinalDataMetaInfoType finalDataMetaInfo;

    /**
     * Gets the value of the transforms property.
     * 
     * @return
     *     possible object is
     *     {@link TransformsType }
     *     
     */
    public TransformsType getTransforms() {
        return transforms;
    }

    /**
     * Sets the value of the transforms property.
     * 
     * @param value
     *     allowed object is
     *     {@link TransformsType }
     *     
     */
    public void setTransforms(TransformsType value) {
        this.transforms = value;
    }

    /**
     * Gets the value of the finalDataMetaInfo property.
     * 
     * @return
     *     possible object is
     *     {@link FinalDataMetaInfoType }
     *     
     */
    public FinalDataMetaInfoType getFinalDataMetaInfo() {
        return finalDataMetaInfo;
    }

    /**
     * Sets the value of the finalDataMetaInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link FinalDataMetaInfoType }
     *     
     */
    public void setFinalDataMetaInfo(FinalDataMetaInfoType value) {
        this.finalDataMetaInfo = value;
    }

}
