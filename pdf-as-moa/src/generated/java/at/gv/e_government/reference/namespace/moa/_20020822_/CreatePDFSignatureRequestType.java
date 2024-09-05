
package at.gv.e_government.reference.namespace.moa._20020822_;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für CreatePDFSignatureRequestType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="CreatePDFSignatureRequestType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="KeyIdentifier" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}KeyIdentifierType"/&gt;
 *         &lt;element name="SingleSignatureInfo" maxOccurs="unbounded"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="PDFDocument" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/&gt;
 *                   &lt;element name="SignatureProfile" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="SignaturePosition" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                   &lt;element name="SignatureID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CreatePDFSignatureRequestType", propOrder = {
    "keyIdentifier",
    "singleSignatureInfo"
})
@XmlSeeAlso({
    CreatePDFSignatureRequest.class
})
public class CreatePDFSignatureRequestType {

    @XmlElement(name = "KeyIdentifier", required = true)
    protected String keyIdentifier;
    @XmlElement(name = "SingleSignatureInfo", required = true)
    protected List<CreatePDFSignatureRequestType.SingleSignatureInfo> singleSignatureInfo;

    /**
     * Ruft den Wert der keyIdentifier-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKeyIdentifier() {
        return keyIdentifier;
    }

    /**
     * Legt den Wert der keyIdentifier-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKeyIdentifier(String value) {
        this.keyIdentifier = value;
    }

    /**
     * Gets the value of the singleSignatureInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the singleSignatureInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSingleSignatureInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CreatePDFSignatureRequestType.SingleSignatureInfo }
     * 
     * 
     */
    public List<CreatePDFSignatureRequestType.SingleSignatureInfo> getSingleSignatureInfo() {
        if (singleSignatureInfo == null) {
            singleSignatureInfo = new ArrayList<CreatePDFSignatureRequestType.SingleSignatureInfo>();
        }
        return this.singleSignatureInfo;
    }


    /**
     * <p>Java-Klasse für anonymous complex type.
     * 
     * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="PDFDocument" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/&gt;
     *         &lt;element name="SignatureProfile" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="SignaturePosition" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="SignatureID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "pdfDocument",
        "signatureProfile",
        "signaturePosition",
        "signatureID"
    })
    public static class SingleSignatureInfo {

        @XmlElement(name = "PDFDocument", required = true)
        protected byte[] pdfDocument;
        @XmlElement(name = "SignatureProfile")
        protected String signatureProfile;
        @XmlElement(name = "SignaturePosition")
        protected String signaturePosition;
        @XmlElement(name = "SignatureID")
        protected String signatureID;

        /**
         * Ruft den Wert der pdfDocument-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     byte[]
         */
        public byte[] getPDFDocument() {
            return pdfDocument;
        }

        /**
         * Legt den Wert der pdfDocument-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     byte[]
         */
        public void setPDFDocument(byte[] value) {
            this.pdfDocument = value;
        }

        /**
         * Ruft den Wert der signatureProfile-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSignatureProfile() {
            return signatureProfile;
        }

        /**
         * Legt den Wert der signatureProfile-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSignatureProfile(String value) {
            this.signatureProfile = value;
        }

        /**
         * Ruft den Wert der signaturePosition-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSignaturePosition() {
            return signaturePosition;
        }

        /**
         * Legt den Wert der signaturePosition-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSignaturePosition(String value) {
            this.signaturePosition = value;
        }

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

    }

}
