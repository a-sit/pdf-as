
package at.gv.e_government.reference.namespace.moa._20020822_;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java-Klasse für VerifyXMLSignatureRequestType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="VerifyXMLSignatureRequestType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="DateTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="ExtendedValidation" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="VerifySignatureInfo"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="VerifySignatureEnvironment" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}ContentOptionalRefType"/&gt;
 *                   &lt;element name="VerifySignatureLocation" type="{http://www.w3.org/2001/XMLSchema}token"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;choice maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;element ref="{http://reference.e-government.gv.at/namespace/moa/20020822#}SupplementProfile"/&gt;
 *           &lt;element name="SupplementProfileID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;/choice&gt;
 *         &lt;element name="SignatureManifestCheckParams" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="ReferenceInfo" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}VerifyTransformsDataType" maxOccurs="unbounded"/&gt;
 *                 &lt;/sequence&gt;
 *                 &lt;attribute name="ReturnReferenceInputData" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" /&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="ReturnHashInputData" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/&gt;
 *         &lt;element name="TrustProfileID" type="{http://www.w3.org/2001/XMLSchema}token"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VerifyXMLSignatureRequestType", propOrder = {
    "dateTime",
    "extendedValidation",
    "verifySignatureInfo",
    "supplementProfileOrSupplementProfileID",
    "signatureManifestCheckParams",
    "returnHashInputData",
    "trustProfileID"
})
public class VerifyXMLSignatureRequestType {

    @XmlElement(name = "DateTime")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateTime;
    @XmlElement(name = "ExtendedValidation", defaultValue = "false")
    protected Boolean extendedValidation;
    @XmlElement(name = "VerifySignatureInfo", required = true)
    protected VerifyXMLSignatureRequestType.VerifySignatureInfo verifySignatureInfo;
    @XmlElements({
        @XmlElement(name = "SupplementProfile", type = XMLDataObjectAssociationType.class),
        @XmlElement(name = "SupplementProfileID", type = String.class)
    })
    protected List<Object> supplementProfileOrSupplementProfileID;
    @XmlElement(name = "SignatureManifestCheckParams")
    protected VerifyXMLSignatureRequestType.SignatureManifestCheckParams signatureManifestCheckParams;
    @XmlElement(name = "ReturnHashInputData")
    protected Object returnHashInputData;
    @XmlElement(name = "TrustProfileID", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String trustProfileID;

    /**
     * Ruft den Wert der dateTime-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateTime() {
        return dateTime;
    }

    /**
     * Legt den Wert der dateTime-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateTime(XMLGregorianCalendar value) {
        this.dateTime = value;
    }

    /**
     * Ruft den Wert der extendedValidation-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isExtendedValidation() {
        return extendedValidation;
    }

    /**
     * Legt den Wert der extendedValidation-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setExtendedValidation(Boolean value) {
        this.extendedValidation = value;
    }

    /**
     * Ruft den Wert der verifySignatureInfo-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link VerifyXMLSignatureRequestType.VerifySignatureInfo }
     *     
     */
    public VerifyXMLSignatureRequestType.VerifySignatureInfo getVerifySignatureInfo() {
        return verifySignatureInfo;
    }

    /**
     * Legt den Wert der verifySignatureInfo-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link VerifyXMLSignatureRequestType.VerifySignatureInfo }
     *     
     */
    public void setVerifySignatureInfo(VerifyXMLSignatureRequestType.VerifySignatureInfo value) {
        this.verifySignatureInfo = value;
    }

    /**
     * Gets the value of the supplementProfileOrSupplementProfileID property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the supplementProfileOrSupplementProfileID property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSupplementProfileOrSupplementProfileID().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XMLDataObjectAssociationType }
     * {@link String }
     * 
     * 
     */
    public List<Object> getSupplementProfileOrSupplementProfileID() {
        if (supplementProfileOrSupplementProfileID == null) {
            supplementProfileOrSupplementProfileID = new ArrayList<Object>();
        }
        return this.supplementProfileOrSupplementProfileID;
    }

    /**
     * Ruft den Wert der signatureManifestCheckParams-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link VerifyXMLSignatureRequestType.SignatureManifestCheckParams }
     *     
     */
    public VerifyXMLSignatureRequestType.SignatureManifestCheckParams getSignatureManifestCheckParams() {
        return signatureManifestCheckParams;
    }

    /**
     * Legt den Wert der signatureManifestCheckParams-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link VerifyXMLSignatureRequestType.SignatureManifestCheckParams }
     *     
     */
    public void setSignatureManifestCheckParams(VerifyXMLSignatureRequestType.SignatureManifestCheckParams value) {
        this.signatureManifestCheckParams = value;
    }

    /**
     * Ruft den Wert der returnHashInputData-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getReturnHashInputData() {
        return returnHashInputData;
    }

    /**
     * Legt den Wert der returnHashInputData-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setReturnHashInputData(Object value) {
        this.returnHashInputData = value;
    }

    /**
     * Ruft den Wert der trustProfileID-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTrustProfileID() {
        return trustProfileID;
    }

    /**
     * Legt den Wert der trustProfileID-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTrustProfileID(String value) {
        this.trustProfileID = value;
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
     *         &lt;element name="ReferenceInfo" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}VerifyTransformsDataType" maxOccurs="unbounded"/&gt;
     *       &lt;/sequence&gt;
     *       &lt;attribute name="ReturnReferenceInputData" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" /&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "referenceInfo"
    })
    public static class SignatureManifestCheckParams {

        @XmlElement(name = "ReferenceInfo", required = true)
        protected List<VerifyTransformsDataType> referenceInfo;
        @XmlAttribute(name = "ReturnReferenceInputData")
        protected Boolean returnReferenceInputData;

        /**
         * Gets the value of the referenceInfo property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the referenceInfo property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getReferenceInfo().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link VerifyTransformsDataType }
         * 
         * 
         */
        public List<VerifyTransformsDataType> getReferenceInfo() {
            if (referenceInfo == null) {
                referenceInfo = new ArrayList<VerifyTransformsDataType>();
            }
            return this.referenceInfo;
        }

        /**
         * Ruft den Wert der returnReferenceInputData-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link Boolean }
         *     
         */
        public boolean isReturnReferenceInputData() {
            if (returnReferenceInputData == null) {
                return true;
            } else {
                return returnReferenceInputData;
            }
        }

        /**
         * Legt den Wert der returnReferenceInputData-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *     
         */
        public void setReturnReferenceInputData(Boolean value) {
            this.returnReferenceInputData = value;
        }

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
     *         &lt;element name="VerifySignatureEnvironment" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}ContentOptionalRefType"/&gt;
     *         &lt;element name="VerifySignatureLocation" type="{http://www.w3.org/2001/XMLSchema}token"/&gt;
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
        "verifySignatureEnvironment",
        "verifySignatureLocation"
    })
    public static class VerifySignatureInfo {

        @XmlElement(name = "VerifySignatureEnvironment", required = true)
        protected ContentOptionalRefType verifySignatureEnvironment;
        @XmlElement(name = "VerifySignatureLocation", required = true)
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        @XmlSchemaType(name = "token")
        protected String verifySignatureLocation;

        /**
         * Ruft den Wert der verifySignatureEnvironment-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link ContentOptionalRefType }
         *     
         */
        public ContentOptionalRefType getVerifySignatureEnvironment() {
            return verifySignatureEnvironment;
        }

        /**
         * Legt den Wert der verifySignatureEnvironment-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link ContentOptionalRefType }
         *     
         */
        public void setVerifySignatureEnvironment(ContentOptionalRefType value) {
            this.verifySignatureEnvironment = value;
        }

        /**
         * Ruft den Wert der verifySignatureLocation-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getVerifySignatureLocation() {
            return verifySignatureLocation;
        }

        /**
         * Legt den Wert der verifySignatureLocation-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setVerifySignatureLocation(String value) {
            this.verifySignatureLocation = value;
        }

    }

}
