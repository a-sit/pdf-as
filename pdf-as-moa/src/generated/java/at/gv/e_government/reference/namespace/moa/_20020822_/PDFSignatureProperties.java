
package at.gv.e_government.reference.namespace.moa._20020822_;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PDFSignatureProperties complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="PDFSignatureProperties">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="SignatureCoversFullPDF" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         <element name="SignatureByteRange" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PDFSignatureProperties", propOrder = {
    "signatureCoversFullPDF",
    "signatureByteRange"
})
public class PDFSignatureProperties {

    @XmlElement(name = "SignatureCoversFullPDF")
    protected Boolean signatureCoversFullPDF;
    @XmlElement(name = "SignatureByteRange")
    protected String signatureByteRange;

    /**
     * Gets the value of the signatureCoversFullPDF property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSignatureCoversFullPDF() {
        return signatureCoversFullPDF;
    }

    /**
     * Sets the value of the signatureCoversFullPDF property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSignatureCoversFullPDF(Boolean value) {
        this.signatureCoversFullPDF = value;
    }

    /**
     * Gets the value of the signatureByteRange property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSignatureByteRange() {
        return signatureByteRange;
    }

    /**
     * Sets the value of the signatureByteRange property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSignatureByteRange(String value) {
        this.signatureByteRange = value;
    }

}
