
package at.gv.e_government.reference.namespace.moa._20020822_;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java-Klasse für InputDataType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="InputDataType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://reference.e-government.gv.at/namespace/moa/20020822#}ContentExLocRefBaseType"&gt;
 *       &lt;attribute name="PartOf" default="SignedInfo"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token"&gt;
 *             &lt;enumeration value="SignedInfo"/&gt;
 *             &lt;enumeration value="XMLDSIGManifest"/&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *       &lt;attribute name="ReferringSigReference" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" /&gt;
 *       &lt;attribute name="HashAlgorithm" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
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
     * Ruft den Wert der partOf-Eigenschaft ab.
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
     * Legt den Wert der partOf-Eigenschaft fest.
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
     * Ruft den Wert der referringSigReference-Eigenschaft ab.
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
     * Legt den Wert der referringSigReference-Eigenschaft fest.
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
     * Ruft den Wert der hashAlgorithm-Eigenschaft ab.
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
     * Legt den Wert der hashAlgorithm-Eigenschaft fest.
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
