
package at.gv.e_government.reference.namespace.moa._20020822_;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PDFSignedRepsonse complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="PDFSignedRepsonse">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="SignatureID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <choice>
 *           <element name="PDFSignature" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *           <element ref="{http://reference.e-government.gv.at/namespace/moa/20020822#}ErrorResponse"/>
 *         </choice>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PDFSignedRepsonse", propOrder = {
    "signatureID",
    "pdfSignature",
    "errorResponse"
})
public class PDFSignedRepsonse {

    @XmlElement(name = "SignatureID")
    protected String signatureID;
    /**
     * Resultat, falls die Signaturerstellung
     * 							erfolgreich war
     * 
     */
    @XmlElement(name = "PDFSignature")
    protected byte[] pdfSignature;
    /**
     * Resultat, falls die Signaturerstellung gescheitert
     * 				ist
     * 
     */
    @XmlElement(name = "ErrorResponse")
    protected ErrorResponseType errorResponse;

    /**
     * Gets the value of the signatureID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSignatureID() {
        return signatureID;
    }

    /**
     * Sets the value of the signatureID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSignatureID(String value) {
        this.signatureID = value;
    }

    /**
     * Resultat, falls die Signaturerstellung
     * 							erfolgreich war
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getPDFSignature() {
        return pdfSignature;
    }

    /**
     * Sets the value of the pdfSignature property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     * @see #getPDFSignature()
     */
    public void setPDFSignature(byte[] value) {
        this.pdfSignature = value;
    }

    /**
     * Resultat, falls die Signaturerstellung gescheitert
     * 				ist
     * 
     * @return
     *     possible object is
     *     {@link ErrorResponseType }
     *     
     */
    public ErrorResponseType getErrorResponse() {
        return errorResponse;
    }

    /**
     * Sets the value of the errorResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link ErrorResponseType }
     *     
     * @see #getErrorResponse()
     */
    public void setErrorResponse(ErrorResponseType value) {
        this.errorResponse = value;
    }

}
