
package at.gv.e_government.reference.namespace.moa._20020822_;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für ExtendedCertificateCheckResultType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ExtendedCertificateCheckResultType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Major" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}IndicationResultType"/&gt;
 *         &lt;element name="Minor" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}IndicationResultType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ExtendedCertificateCheckResultType", propOrder = {
    "major",
    "minor"
})
public class ExtendedCertificateCheckResultType {

    @XmlElement(name = "Major", required = true)
    protected IndicationResultType major;
    @XmlElement(name = "Minor")
    protected IndicationResultType minor;

    /**
     * Ruft den Wert der major-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link IndicationResultType }
     *     
     */
    public IndicationResultType getMajor() {
        return major;
    }

    /**
     * Legt den Wert der major-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link IndicationResultType }
     *     
     */
    public void setMajor(IndicationResultType value) {
        this.major = value;
    }

    /**
     * Ruft den Wert der minor-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link IndicationResultType }
     *     
     */
    public IndicationResultType getMinor() {
        return minor;
    }

    /**
     * Legt den Wert der minor-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link IndicationResultType }
     *     
     */
    public void setMinor(IndicationResultType value) {
        this.minor = value;
    }

}
