
package at.gv.e_government.reference.namespace.moa._20020822_;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CreatePDFSignatureRequestType complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="CreatePDFSignatureRequestType">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="KeyIdentifier" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}KeyIdentifierType"/>
 *         <element name="SingleSignatureInfo" maxOccurs="unbounded">
 *           <complexType>
 *             <complexContent>
 *               <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 <sequence>
 *                   <element name="PDFDocument" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *                   <element name="SignatureProfile" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   <element name="SignaturePosition" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   <element name="SignatureID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                 </sequence>
 *               </restriction>
 *             </complexContent>
 *           </complexType>
 *         </element>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
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
    /**
     * ErmÃ¶glichung der Stapelsignatur durch
     * 						wiederholte Angabe dieses Elements
     * 
     */
    @XmlElement(name = "SingleSignatureInfo", required = true)
    protected List<CreatePDFSignatureRequestType.SingleSignatureInfo> singleSignatureInfo;

    /**
     * Gets the value of the keyIdentifier property.
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
     * Sets the value of the keyIdentifier property.
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
     * ErmÃ¶glichung der Stapelsignatur durch
     * 						wiederholte Angabe dieses Elements
     * 
     * Gets the value of the singleSignatureInfo property.
     * 
     * <p>This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the singleSignatureInfo property.</p>
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * </p>
     * <pre>
     * getSingleSignatureInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CreatePDFSignatureRequestType.SingleSignatureInfo }
     * </p>
     * 
     * 
     * @return
     *     The value of the singleSignatureInfo property.
     */
    public List<CreatePDFSignatureRequestType.SingleSignatureInfo> getSingleSignatureInfo() {
        if (singleSignatureInfo == null) {
            singleSignatureInfo = new ArrayList<>();
        }
        return this.singleSignatureInfo;
    }


    /**
     * <p>Java class for anonymous complex type</p>.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.</p>
     * 
     * <pre>{@code
     * <complexType>
     *   <complexContent>
     *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       <sequence>
     *         <element name="PDFDocument" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
     *         <element name="SignatureProfile" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         <element name="SignaturePosition" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         <element name="SignatureID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *       </sequence>
     *     </restriction>
     *   </complexContent>
     * </complexType>
     * }</pre>
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
         * Gets the value of the pdfDocument property.
         * 
         * @return
         *     possible object is
         *     byte[]
         */
        public byte[] getPDFDocument() {
            return pdfDocument;
        }

        /**
         * Sets the value of the pdfDocument property.
         * 
         * @param value
         *     allowed object is
         *     byte[]
         */
        public void setPDFDocument(byte[] value) {
            this.pdfDocument = value;
        }

        /**
         * Gets the value of the signatureProfile property.
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
         * Sets the value of the signatureProfile property.
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
         * Gets the value of the signaturePosition property.
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
         * Sets the value of the signaturePosition property.
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

    }

}
