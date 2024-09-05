
package at.gv.e_government.reference.namespace.moa._20020822_;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java-Klasse für CreateXMLSignatureRequestType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="CreateXMLSignatureRequestType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="KeyIdentifier" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}KeyIdentifierType"/&gt;
 *         &lt;element name="SingleSignatureInfo" maxOccurs="unbounded"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="DataObjectInfo" maxOccurs="unbounded"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;extension base="{http://reference.e-government.gv.at/namespace/moa/20020822#}DataObjectInfoType"&gt;
 *                           &lt;attribute name="ChildOfManifest" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *                         &lt;/extension&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="CreateSignatureInfo" minOccurs="0"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="CreateSignatureEnvironment" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}ContentOptionalRefType"/&gt;
 *                             &lt;choice&gt;
 *                               &lt;element ref="{http://reference.e-government.gv.at/namespace/moa/20020822#}CreateSignatureEnvironmentProfile"/&gt;
 *                               &lt;element name="CreateSignatureEnvironmentProfileID" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}ProfileIdentifierType"/&gt;
 *                             &lt;/choice&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                 &lt;/sequence&gt;
 *                 &lt;attribute name="SecurityLayerConformity" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" /&gt;
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
@XmlType(name = "CreateXMLSignatureRequestType", propOrder = {
    "keyIdentifier",
    "singleSignatureInfo"
})
@XmlSeeAlso({
    CreateXMLSignatureRequest.class
})
public class CreateXMLSignatureRequestType {

    @XmlElement(name = "KeyIdentifier", required = true)
    protected String keyIdentifier;
    @XmlElement(name = "SingleSignatureInfo", required = true)
    protected List<CreateXMLSignatureRequestType.SingleSignatureInfo> singleSignatureInfo;

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
     * {@link CreateXMLSignatureRequestType.SingleSignatureInfo }
     * 
     * 
     */
    public List<CreateXMLSignatureRequestType.SingleSignatureInfo> getSingleSignatureInfo() {
        if (singleSignatureInfo == null) {
            singleSignatureInfo = new ArrayList<CreateXMLSignatureRequestType.SingleSignatureInfo>();
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
     *         &lt;element name="DataObjectInfo" maxOccurs="unbounded"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;extension base="{http://reference.e-government.gv.at/namespace/moa/20020822#}DataObjectInfoType"&gt;
     *                 &lt;attribute name="ChildOfManifest" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
     *               &lt;/extension&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="CreateSignatureInfo" minOccurs="0"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="CreateSignatureEnvironment" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}ContentOptionalRefType"/&gt;
     *                   &lt;choice&gt;
     *                     &lt;element ref="{http://reference.e-government.gv.at/namespace/moa/20020822#}CreateSignatureEnvironmentProfile"/&gt;
     *                     &lt;element name="CreateSignatureEnvironmentProfileID" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}ProfileIdentifierType"/&gt;
     *                   &lt;/choice&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *       &lt;/sequence&gt;
     *       &lt;attribute name="SecurityLayerConformity" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" /&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "dataObjectInfo",
        "createSignatureInfo"
    })
    public static class SingleSignatureInfo {

        @XmlElement(name = "DataObjectInfo", required = true)
        protected List<CreateXMLSignatureRequestType.SingleSignatureInfo.DataObjectInfo> dataObjectInfo;
        @XmlElement(name = "CreateSignatureInfo")
        protected CreateXMLSignatureRequestType.SingleSignatureInfo.CreateSignatureInfo createSignatureInfo;
        @XmlAttribute(name = "SecurityLayerConformity")
        protected Boolean securityLayerConformity;

        /**
         * Gets the value of the dataObjectInfo property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the dataObjectInfo property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getDataObjectInfo().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link CreateXMLSignatureRequestType.SingleSignatureInfo.DataObjectInfo }
         * 
         * 
         */
        public List<CreateXMLSignatureRequestType.SingleSignatureInfo.DataObjectInfo> getDataObjectInfo() {
            if (dataObjectInfo == null) {
                dataObjectInfo = new ArrayList<CreateXMLSignatureRequestType.SingleSignatureInfo.DataObjectInfo>();
            }
            return this.dataObjectInfo;
        }

        /**
         * Ruft den Wert der createSignatureInfo-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link CreateXMLSignatureRequestType.SingleSignatureInfo.CreateSignatureInfo }
         *     
         */
        public CreateXMLSignatureRequestType.SingleSignatureInfo.CreateSignatureInfo getCreateSignatureInfo() {
            return createSignatureInfo;
        }

        /**
         * Legt den Wert der createSignatureInfo-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link CreateXMLSignatureRequestType.SingleSignatureInfo.CreateSignatureInfo }
         *     
         */
        public void setCreateSignatureInfo(CreateXMLSignatureRequestType.SingleSignatureInfo.CreateSignatureInfo value) {
            this.createSignatureInfo = value;
        }

        /**
         * Ruft den Wert der securityLayerConformity-Eigenschaft ab.
         * 
         * @return
         *     possible object is
         *     {@link Boolean }
         *     
         */
        public boolean isSecurityLayerConformity() {
            if (securityLayerConformity == null) {
                return true;
            } else {
                return securityLayerConformity;
            }
        }

        /**
         * Legt den Wert der securityLayerConformity-Eigenschaft fest.
         * 
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *     
         */
        public void setSecurityLayerConformity(Boolean value) {
            this.securityLayerConformity = value;
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
         *         &lt;element name="CreateSignatureEnvironment" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}ContentOptionalRefType"/&gt;
         *         &lt;choice&gt;
         *           &lt;element ref="{http://reference.e-government.gv.at/namespace/moa/20020822#}CreateSignatureEnvironmentProfile"/&gt;
         *           &lt;element name="CreateSignatureEnvironmentProfileID" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}ProfileIdentifierType"/&gt;
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
        @XmlType(name = "", propOrder = {
            "createSignatureEnvironment",
            "createSignatureEnvironmentProfile",
            "createSignatureEnvironmentProfileID"
        })
        public static class CreateSignatureInfo {

            @XmlElement(name = "CreateSignatureEnvironment", required = true)
            protected ContentOptionalRefType createSignatureEnvironment;
            @XmlElement(name = "CreateSignatureEnvironmentProfile")
            protected CreateSignatureEnvironmentProfile createSignatureEnvironmentProfile;
            @XmlElement(name = "CreateSignatureEnvironmentProfileID")
            @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
            @XmlSchemaType(name = "token")
            protected String createSignatureEnvironmentProfileID;

            /**
             * Ruft den Wert der createSignatureEnvironment-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link ContentOptionalRefType }
             *     
             */
            public ContentOptionalRefType getCreateSignatureEnvironment() {
                return createSignatureEnvironment;
            }

            /**
             * Legt den Wert der createSignatureEnvironment-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link ContentOptionalRefType }
             *     
             */
            public void setCreateSignatureEnvironment(ContentOptionalRefType value) {
                this.createSignatureEnvironment = value;
            }

            /**
             * Ruft den Wert der createSignatureEnvironmentProfile-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link CreateSignatureEnvironmentProfile }
             *     
             */
            public CreateSignatureEnvironmentProfile getCreateSignatureEnvironmentProfile() {
                return createSignatureEnvironmentProfile;
            }

            /**
             * Legt den Wert der createSignatureEnvironmentProfile-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link CreateSignatureEnvironmentProfile }
             *     
             */
            public void setCreateSignatureEnvironmentProfile(CreateSignatureEnvironmentProfile value) {
                this.createSignatureEnvironmentProfile = value;
            }

            /**
             * Ruft den Wert der createSignatureEnvironmentProfileID-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCreateSignatureEnvironmentProfileID() {
                return createSignatureEnvironmentProfileID;
            }

            /**
             * Legt den Wert der createSignatureEnvironmentProfileID-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setCreateSignatureEnvironmentProfileID(String value) {
                this.createSignatureEnvironmentProfileID = value;
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
         *     &lt;extension base="{http://reference.e-government.gv.at/namespace/moa/20020822#}DataObjectInfoType"&gt;
         *       &lt;attribute name="ChildOfManifest" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
         *     &lt;/extension&gt;
         *   &lt;/complexContent&gt;
         * &lt;/complexType&gt;
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class DataObjectInfo
            extends DataObjectInfoType
        {

            @XmlAttribute(name = "ChildOfManifest")
            protected Boolean childOfManifest;

            /**
             * Ruft den Wert der childOfManifest-Eigenschaft ab.
             * 
             * @return
             *     possible object is
             *     {@link Boolean }
             *     
             */
            public boolean isChildOfManifest() {
                if (childOfManifest == null) {
                    return false;
                } else {
                    return childOfManifest;
                }
            }

            /**
             * Legt den Wert der childOfManifest-Eigenschaft fest.
             * 
             * @param value
             *     allowed object is
             *     {@link Boolean }
             *     
             */
            public void setChildOfManifest(Boolean value) {
                this.childOfManifest = value;
            }

        }

    }

}
