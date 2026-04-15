
package at.gv.e_government.reference.namespace.moa._20020822_;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ExtendedCertificateCheckResultType complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="ExtendedCertificateCheckResultType">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="Major" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}IndicationResultType"/>
 *         <element name="Minor" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}IndicationResultType" minOccurs="0"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
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
     * Gets the value of the major property.
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
     * Sets the value of the major property.
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
     * Gets the value of the minor property.
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
     * Sets the value of the minor property.
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
