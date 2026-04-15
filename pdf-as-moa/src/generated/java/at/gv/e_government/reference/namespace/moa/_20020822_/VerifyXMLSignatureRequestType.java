
package at.gv.e_government.reference.namespace.moa._20020822_;

import java.util.ArrayList;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElements;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for VerifyXMLSignatureRequestType complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="VerifyXMLSignatureRequestType">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="DateTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         <element name="ExtendedValidation" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         <element name="VerifySignatureInfo">
 *           <complexType>
 *             <complexContent>
 *               <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 <sequence>
 *                   <element name="VerifySignatureEnvironment" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}ContentOptionalRefType"/>
 *                   <element name="VerifySignatureLocation" type="{http://www.w3.org/2001/XMLSchema}token"/>
 *                 </sequence>
 *               </restriction>
 *             </complexContent>
 *           </complexType>
 *         </element>
 *         <choice maxOccurs="unbounded" minOccurs="0">
 *           <element ref="{http://reference.e-government.gv.at/namespace/moa/20020822#}SupplementProfile"/>
 *           <element name="SupplementProfileID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         </choice>
 *         <element name="SignatureManifestCheckParams" minOccurs="0">
 *           <complexType>
 *             <complexContent>
 *               <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 <sequence>
 *                   <element name="ReferenceInfo" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}VerifyTransformsDataType" maxOccurs="unbounded"/>
 *                 </sequence>
 *                 <attribute name="ReturnReferenceInputData" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" />
 *               </restriction>
 *             </complexContent>
 *           </complexType>
 *         </element>
 *         <element name="ReturnHashInputData" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
 *         <element name="TrustProfileID" type="{http://www.w3.org/2001/XMLSchema}token"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
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
    /**
     * mit diesem Profil wird eine Menge von
     * 						vertrauenswÃ¼rdigen Wurzelzertifikaten spezifiziert
     * 
     */
    @XmlElement(name = "TrustProfileID", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String trustProfileID;

    /**
     * Gets the value of the dateTime property.
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
     * Sets the value of the dateTime property.
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
     * Gets the value of the extendedValidation property.
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
     * Sets the value of the extendedValidation property.
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
     * Gets the value of the verifySignatureInfo property.
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
     * Sets the value of the verifySignatureInfo property.
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
     * <p>This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the supplementProfileOrSupplementProfileID property.</p>
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * </p>
     * <pre>
     * getSupplementProfileOrSupplementProfileID().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XMLDataObjectAssociationType }
     * {@link String }
     * </p>
     * 
     * 
     * @return
     *     The value of the supplementProfileOrSupplementProfileID property.
     */
    public List<Object> getSupplementProfileOrSupplementProfileID() {
        if (supplementProfileOrSupplementProfileID == null) {
            supplementProfileOrSupplementProfileID = new ArrayList<>();
        }
        return this.supplementProfileOrSupplementProfileID;
    }

    /**
     * Gets the value of the signatureManifestCheckParams property.
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
     * Sets the value of the signatureManifestCheckParams property.
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
     * Gets the value of the returnHashInputData property.
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
     * Sets the value of the returnHashInputData property.
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
     * mit diesem Profil wird eine Menge von
     * 						vertrauenswÃ¼rdigen Wurzelzertifikaten spezifiziert
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
     * Sets the value of the trustProfileID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     * @see #getTrustProfileID()
     */
    public void setTrustProfileID(String value) {
        this.trustProfileID = value;
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
     *         <element name="ReferenceInfo" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}VerifyTransformsDataType" maxOccurs="unbounded"/>
     *       </sequence>
     *       <attribute name="ReturnReferenceInputData" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" />
     *     </restriction>
     *   </complexContent>
     * </complexType>
     * }</pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "referenceInfo"
    })
    public static class SignatureManifestCheckParams {

        /**
         * Pro dsig:Reference-Element in der zu
         * 									Ã¼berprÃ¼fenden XML-Signatur muss hier ein
         * 									ReferenceInfo-Element erscheinen. Die Reihenfolge der einzelnen
         * 									ReferenceInfo Elemente entspricht jener der dsig:Reference
         * 									Elemente in der XML-Signatur.
         * 
         */
        @XmlElement(name = "ReferenceInfo", required = true)
        protected List<VerifyTransformsDataType> referenceInfo;
        @XmlAttribute(name = "ReturnReferenceInputData")
        protected Boolean returnReferenceInputData;

        /**
         * Pro dsig:Reference-Element in der zu
         * 									Ã¼berprÃ¼fenden XML-Signatur muss hier ein
         * 									ReferenceInfo-Element erscheinen. Die Reihenfolge der einzelnen
         * 									ReferenceInfo Elemente entspricht jener der dsig:Reference
         * 									Elemente in der XML-Signatur.
         * 
         * Gets the value of the referenceInfo property.
         * 
         * <p>This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the referenceInfo property.</p>
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * </p>
         * <pre>
         * getReferenceInfo().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link VerifyTransformsDataType }
         * </p>
         * 
         * 
         * @return
         *     The value of the referenceInfo property.
         */
        public List<VerifyTransformsDataType> getReferenceInfo() {
            if (referenceInfo == null) {
                referenceInfo = new ArrayList<>();
            }
            return this.referenceInfo;
        }

        /**
         * Gets the value of the returnReferenceInputData property.
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
         * Sets the value of the returnReferenceInputData property.
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
     * <p>Java class for anonymous complex type</p>.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.</p>
     * 
     * <pre>{@code
     * <complexType>
     *   <complexContent>
     *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       <sequence>
     *         <element name="VerifySignatureEnvironment" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}ContentOptionalRefType"/>
     *         <element name="VerifySignatureLocation" type="{http://www.w3.org/2001/XMLSchema}token"/>
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
         * Gets the value of the verifySignatureEnvironment property.
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
         * Sets the value of the verifySignatureEnvironment property.
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
         * Gets the value of the verifySignatureLocation property.
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
         * Sets the value of the verifySignatureLocation property.
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
