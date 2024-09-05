
package at.gv.e_government.reference.namespace.moa._20020822_;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für PDFSignedRepsonse complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="PDFSignedRepsonse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="SignatureID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;choice&gt;
 *           &lt;element name="PDFSignature" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/&gt;
 *           &lt;element ref="{http://reference.e-government.gv.at/namespace/moa/20020822#}ErrorResponse"/&gt;
 *         &lt;/choice&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
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
    @XmlElement(name = "PDFSignature")
    protected byte[] pdfSignature;
    @XmlElement(name = "ErrorResponse")
    protected ErrorResponseType errorResponse;

    /**
     * Ruft den Wert der signatureID-Eigenschaft ab.
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
     * Legt den Wert der signatureID-Eigenschaft fest.
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
     * Ruft den Wert der pdfSignature-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getPDFSignature() {
        return pdfSignature;
    }

    /**
     * Legt den Wert der pdfSignature-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setPDFSignature(byte[] value) {
        this.pdfSignature = value;
    }

    /**
     * Ruft den Wert der errorResponse-Eigenschaft ab.
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
     * Legt den Wert der errorResponse-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ErrorResponseType }
     *     
     */
    public void setErrorResponse(ErrorResponseType value) {
        this.errorResponse = value;
    }

}
