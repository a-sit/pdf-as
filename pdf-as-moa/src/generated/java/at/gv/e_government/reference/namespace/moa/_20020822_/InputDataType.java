
package at.gv.e_government.reference.namespace.moa._20020822_;

import java.math.BigInteger;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for InputDataType complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="InputDataType">
 *   <complexContent>
 *     <extension base="{http://reference.e-government.gv.at/namespace/moa/20020822#}ContentExLocRefBaseType">
 *       <attribute name="PartOf" default="SignedInfo">
 *         <simpleType>
 *           <restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *             <enumeration value="SignedInfo"/>
 *             <enumeration value="XMLDSIGManifest"/>
 *           </restriction>
 *         </simpleType>
 *       </attribute>
 *       <attribute name="ReferringSigReference" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" />
 *       <attribute name="HashAlgorithm" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     </extension>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InputDataType")
public class InputDataType
    extends ContentExLocRefBaseType
{

    @XmlAttribute(name = "PartOf")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String partOf;
    @XmlAttribute(name = "ReferringSigReference")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger referringSigReference;
    @XmlAttribute(name = "HashAlgorithm")
    protected String hashAlgorithm;

    /**
     * Gets the value of the partOf property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPartOf() {
        if (partOf == null) {
            return "SignedInfo";
        } else {
            return partOf;
        }
    }

    /**
     * Sets the value of the partOf property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPartOf(String value) {
        this.partOf = value;
    }

    /**
     * Gets the value of the referringSigReference property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getReferringSigReference() {
        return referringSigReference;
    }

    /**
     * Sets the value of the referringSigReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setReferringSigReference(BigInteger value) {
        this.referringSigReference = value;
    }

    /**
     * Gets the value of the hashAlgorithm property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHashAlgorithm() {
        return hashAlgorithm;
    }

    /**
     * Sets the value of the hashAlgorithm property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHashAlgorithm(String value) {
        this.hashAlgorithm = value;
    }

}
