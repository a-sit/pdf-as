
package at.gv.e_government.reference.namespace.moa._20020822_;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.XmlValue;


/**
 * <p>Java class for ASICResultType complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="ASICResultType">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="signedFiles" maxOccurs="unbounded" minOccurs="0">
 *           <complexType>
 *             <simpleContent>
 *               <extension base="<http://www.w3.org/2001/XMLSchema>string">
 *                 <attribute name="hashAlgorithm" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               </extension>
 *             </simpleContent>
 *           </complexType>
 *         </element>
 *         <element name="XMLSignatureResult" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}VerifyASICXMLSignatureResponseType" maxOccurs="unbounded" minOccurs="0"/>
 *         <element name="CMSSignatureResult" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}VerifyASICCMSSignatureResponseType" maxOccurs="unbounded" minOccurs="0"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ASICResultType", propOrder = {
    "signedFiles",
    "xmlSignatureResult",
    "cmsSignatureResult"
})
public class ASICResultType {

    protected List<ASICResultType.SignedFiles> signedFiles;
    @XmlElement(name = "XMLSignatureResult")
    protected List<VerifyASICXMLSignatureResponseType> xmlSignatureResult;
    @XmlElement(name = "CMSSignatureResult")
    protected List<VerifyASICCMSSignatureResponseType> cmsSignatureResult;

    /**
     * Gets the value of the signedFiles property.
     * 
     * <p>This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the signedFiles property.</p>
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * </p>
     * <pre>
     * getSignedFiles().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ASICResultType.SignedFiles }
     * </p>
     * 
     * 
     * @return
     *     The value of the signedFiles property.
     */
    public List<ASICResultType.SignedFiles> getSignedFiles() {
        if (signedFiles == null) {
            signedFiles = new ArrayList<>();
        }
        return this.signedFiles;
    }

    /**
     * Gets the value of the xmlSignatureResult property.
     * 
     * <p>This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the xmlSignatureResult property.</p>
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * </p>
     * <pre>
     * getXMLSignatureResult().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link VerifyASICXMLSignatureResponseType }
     * </p>
     * 
     * 
     * @return
     *     The value of the xmlSignatureResult property.
     */
    public List<VerifyASICXMLSignatureResponseType> getXMLSignatureResult() {
        if (xmlSignatureResult == null) {
            xmlSignatureResult = new ArrayList<>();
        }
        return this.xmlSignatureResult;
    }

    /**
     * Gets the value of the cmsSignatureResult property.
     * 
     * <p>This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cmsSignatureResult property.</p>
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * </p>
     * <pre>
     * getCMSSignatureResult().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link VerifyASICCMSSignatureResponseType }
     * </p>
     * 
     * 
     * @return
     *     The value of the cmsSignatureResult property.
     */
    public List<VerifyASICCMSSignatureResponseType> getCMSSignatureResult() {
        if (cmsSignatureResult == null) {
            cmsSignatureResult = new ArrayList<>();
        }
        return this.cmsSignatureResult;
    }


    /**
     * <p>Java class for anonymous complex type</p>.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.</p>
     * 
     * <pre>{@code
     * <complexType>
     *   <simpleContent>
     *     <extension base="<http://www.w3.org/2001/XMLSchema>string">
     *       <attribute name="hashAlgorithm" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     </extension>
     *   </simpleContent>
     * </complexType>
     * }</pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "value"
    })
    public static class SignedFiles {

        @XmlValue
        protected String value;
        @XmlAttribute(name = "hashAlgorithm")
        protected String hashAlgorithm;

        /**
         * Gets the value of the value property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setValue(String value) {
            this.value = value;
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

}
