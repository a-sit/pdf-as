
package at.gv.e_government.reference.namespace.moa._20020822_;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * <p>Java-Klasse für ASICResultType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ASICResultType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="signedFiles" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;simpleContent&gt;
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;string"&gt;
 *                 &lt;attribute name="hashAlgorithm" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *               &lt;/extension&gt;
 *             &lt;/simpleContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="XMLSignatureResult" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}VerifyASICXMLSignatureResponseType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="CMSSignatureResult" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}VerifyASICCMSSignatureResponseType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
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
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the signedFiles property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSignedFiles().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ASICResultType.SignedFiles }
     * 
     * 
     */
    public List<ASICResultType.SignedFiles> getSignedFiles() {
        if (signedFiles == null) {
            signedFiles = new ArrayList<ASICResultType.SignedFiles>();
        }
        return this.signedFiles;
    }

    /**
     * Gets the value of the xmlSignatureResult property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the xmlSignatureResult property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getXMLSignatureResult().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link VerifyASICXMLSignatureResponseType }
     * 
     * 
     */
    public List<VerifyASICXMLSignatureResponseType> getXMLSignatureResult() {
        if (xmlSignatureResult == null) {
            xmlSignatureResult = new ArrayList<VerifyASICXMLSignatureResponseType>();
        }
        return this.xmlSignatureResult;
    }

    /**
     * Gets the value of the cmsSignatureResult property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cmsSignatureResult property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCMSSignatureResult().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link VerifyASICCMSSignatureResponseType }
     * 
     * 
     */
    public List<VerifyASICCMSSignatureResponseType> getCMSSignatureResult() {
        if (cmsSignatureResult == null) {
            cmsSignatureResult = new ArrayList<VerifyASICCMSSignatureResponseType>();
        }
        return this.cmsSignatureResult;
    }


    /**
     * <p>Java-Klasse für anonymous complex type.
     * 
     * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;simpleContent&gt;
     *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;string"&gt;
     *       &lt;attribute name="hashAlgorithm" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *     &lt;/extension&gt;
     *   &lt;/simpleContent&gt;
     * &lt;/complexType&gt;
     * </pre>
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
         * Ruft den Wert der value-Eigenschaft ab.
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
         * Legt den Wert der value-Eigenschaft fest.
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

}
