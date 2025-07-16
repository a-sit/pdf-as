
package at.gv.e_government.reference.namespace.moa._20020822_;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for CreateXMLSignatureRequestType complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="CreateXMLSignatureRequestType">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="KeyIdentifier" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}KeyIdentifierType"/>
 *         <element name="SingleSignatureInfo" maxOccurs="unbounded">
 *           <complexType>
 *             <complexContent>
 *               <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 <sequence>
 *                   <element name="DataObjectInfo" maxOccurs="unbounded">
 *                     <complexType>
 *                       <complexContent>
 *                         <extension base="{http://reference.e-government.gv.at/namespace/moa/20020822#}DataObjectInfoType">
 *                           <attribute name="ChildOfManifest" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *                         </extension>
 *                       </complexContent>
 *                     </complexType>
 *                   </element>
 *                   <element name="CreateSignatureInfo" minOccurs="0">
 *                     <complexType>
 *                       <complexContent>
 *                         <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           <sequence>
 *                             <element name="CreateSignatureEnvironment" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}ContentOptionalRefType"/>
 *                             <choice>
 *                               <element ref="{http://reference.e-government.gv.at/namespace/moa/20020822#}CreateSignatureEnvironmentProfile"/>
 *                               <element name="CreateSignatureEnvironmentProfileID" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}ProfileIdentifierType"/>
 *                             </choice>
 *                           </sequence>
 *                         </restriction>
 *                       </complexContent>
 *                     </complexType>
 *                   </element>
 *                 </sequence>
 *                 <attribute name="SecurityLayerConformity" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" />
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
    /**
     * ErmÃ¶glichung der Stapelsignatur durch
     * 						wiederholte Angabe dieses Elements
     * 
     */
    @XmlElement(name = "SingleSignatureInfo", required = true)
    protected List<CreateXMLSignatureRequestType.SingleSignatureInfo> singleSignatureInfo;

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
     * {@link CreateXMLSignatureRequestType.SingleSignatureInfo }
     * </p>
     * 
     * 
     * @return
     *     The value of the singleSignatureInfo property.
     */
    public List<CreateXMLSignatureRequestType.SingleSignatureInfo> getSingleSignatureInfo() {
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
     *         <element name="DataObjectInfo" maxOccurs="unbounded">
     *           <complexType>
     *             <complexContent>
     *               <extension base="{http://reference.e-government.gv.at/namespace/moa/20020822#}DataObjectInfoType">
     *                 <attribute name="ChildOfManifest" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
     *               </extension>
     *             </complexContent>
     *           </complexType>
     *         </element>
     *         <element name="CreateSignatureInfo" minOccurs="0">
     *           <complexType>
     *             <complexContent>
     *               <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 <sequence>
     *                   <element name="CreateSignatureEnvironment" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}ContentOptionalRefType"/>
     *                   <choice>
     *                     <element ref="{http://reference.e-government.gv.at/namespace/moa/20020822#}CreateSignatureEnvironmentProfile"/>
     *                     <element name="CreateSignatureEnvironmentProfileID" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}ProfileIdentifierType"/>
     *                   </choice>
     *                 </sequence>
     *               </restriction>
     *             </complexContent>
     *           </complexType>
     *         </element>
     *       </sequence>
     *       <attribute name="SecurityLayerConformity" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" />
     *     </restriction>
     *   </complexContent>
     * </complexType>
     * }</pre>
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
         * <p>This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the dataObjectInfo property.</p>
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * </p>
         * <pre>
         * getDataObjectInfo().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link CreateXMLSignatureRequestType.SingleSignatureInfo.DataObjectInfo }
         * </p>
         * 
         * 
         * @return
         *     The value of the dataObjectInfo property.
         */
        public List<CreateXMLSignatureRequestType.SingleSignatureInfo.DataObjectInfo> getDataObjectInfo() {
            if (dataObjectInfo == null) {
                dataObjectInfo = new ArrayList<>();
            }
            return this.dataObjectInfo;
        }

        /**
         * Gets the value of the createSignatureInfo property.
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
         * Sets the value of the createSignatureInfo property.
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
         * Gets the value of the securityLayerConformity property.
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
         * Sets the value of the securityLayerConformity property.
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
         * <p>Java class for anonymous complex type</p>.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.</p>
         * 
         * <pre>{@code
         * <complexType>
         *   <complexContent>
         *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       <sequence>
         *         <element name="CreateSignatureEnvironment" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}ContentOptionalRefType"/>
         *         <choice>
         *           <element ref="{http://reference.e-government.gv.at/namespace/moa/20020822#}CreateSignatureEnvironmentProfile"/>
         *           <element name="CreateSignatureEnvironmentProfileID" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}ProfileIdentifierType"/>
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
             * Gets the value of the createSignatureEnvironment property.
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
             * Sets the value of the createSignatureEnvironment property.
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
             * Gets the value of the createSignatureEnvironmentProfile property.
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
             * Sets the value of the createSignatureEnvironmentProfile property.
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
             * Gets the value of the createSignatureEnvironmentProfileID property.
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
             * Sets the value of the createSignatureEnvironmentProfileID property.
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
         * <p>Java class for anonymous complex type</p>.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.</p>
         * 
         * <pre>{@code
         * <complexType>
         *   <complexContent>
         *     <extension base="{http://reference.e-government.gv.at/namespace/moa/20020822#}DataObjectInfoType">
         *       <attribute name="ChildOfManifest" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
         *     </extension>
         *   </complexContent>
         * </complexType>
         * }</pre>
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
             * Gets the value of the childOfManifest property.
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
             * Sets the value of the childOfManifest property.
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
