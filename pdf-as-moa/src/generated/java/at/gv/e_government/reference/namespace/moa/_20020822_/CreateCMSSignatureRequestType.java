
package at.gv.e_government.reference.namespace.moa._20020822_;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CreateCMSSignatureRequestType complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="CreateCMSSignatureRequestType">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="KeyIdentifier" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}KeyIdentifierType"/>
 *         <element name="SingleSignatureInfo" maxOccurs="unbounded">
 *           <complexType>
 *             <complexContent>
 *               <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 <sequence>
 *                   <element name="DataObjectInfo">
 *                     <complexType>
 *                       <complexContent>
 *                         <extension base="{http://reference.e-government.gv.at/namespace/moa/20020822#}CMSDataObjectInfoType">
 *                         </extension>
 *                       </complexContent>
 *                     </complexType>
 *                   </element>
 *                 </sequence>
 *                 <attribute name="SecurityLayerConformity" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" />
 *                 <attribute name="PAdESConformity" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
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
@XmlType(name = "CreateCMSSignatureRequestType", propOrder = {
    "keyIdentifier",
    "singleSignatureInfo"
})
@XmlSeeAlso({
    CreateCMSSignatureRequest.class
})
public class CreateCMSSignatureRequestType {

    @XmlElement(name = "KeyIdentifier", required = true)
    protected String keyIdentifier;
    /**
     * ErmÃ¶glichung der Stapelsignatur durch
     * 						wiederholte Angabe dieses Elements
     * 
     */
    @XmlElement(name = "SingleSignatureInfo", required = true)
    protected List<CreateCMSSignatureRequestType.SingleSignatureInfo> singleSignatureInfo;

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
     * {@link CreateCMSSignatureRequestType.SingleSignatureInfo }
     * </p>
     * 
     * 
     * @return
     *     The value of the singleSignatureInfo property.
     */
    public List<CreateCMSSignatureRequestType.SingleSignatureInfo> getSingleSignatureInfo() {
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
     *         <element name="DataObjectInfo">
     *           <complexType>
     *             <complexContent>
     *               <extension base="{http://reference.e-government.gv.at/namespace/moa/20020822#}CMSDataObjectInfoType">
     *               </extension>
     *             </complexContent>
     *           </complexType>
     *         </element>
     *       </sequence>
     *       <attribute name="SecurityLayerConformity" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" />
     *       <attribute name="PAdESConformity" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
     *     </restriction>
     *   </complexContent>
     * </complexType>
     * }</pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "dataObjectInfo"
    })
    public static class SingleSignatureInfo {

        @XmlElement(name = "DataObjectInfo", required = true)
        protected CreateCMSSignatureRequestType.SingleSignatureInfo.DataObjectInfo dataObjectInfo;
        @XmlAttribute(name = "SecurityLayerConformity")
        protected Boolean securityLayerConformity;
        @XmlAttribute(name = "PAdESConformity")
        protected Boolean pAdESConformity;

        /**
         * Gets the value of the dataObjectInfo property.
         * 
         * @return
         *     possible object is
         *     {@link CreateCMSSignatureRequestType.SingleSignatureInfo.DataObjectInfo }
         *     
         */
        public CreateCMSSignatureRequestType.SingleSignatureInfo.DataObjectInfo getDataObjectInfo() {
            return dataObjectInfo;
        }

        /**
         * Sets the value of the dataObjectInfo property.
         * 
         * @param value
         *     allowed object is
         *     {@link CreateCMSSignatureRequestType.SingleSignatureInfo.DataObjectInfo }
         *     
         */
        public void setDataObjectInfo(CreateCMSSignatureRequestType.SingleSignatureInfo.DataObjectInfo value) {
            this.dataObjectInfo = value;
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
         * Gets the value of the pAdESConformity property.
         * 
         * @return
         *     possible object is
         *     {@link Boolean }
         *     
         */
        public boolean isPAdESConformity() {
            if (pAdESConformity == null) {
                return false;
            } else {
                return pAdESConformity;
            }
        }

        /**
         * Sets the value of the pAdESConformity property.
         * 
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *     
         */
        public void setPAdESConformity(Boolean value) {
            this.pAdESConformity = value;
        }


        /**
         * <p>Java class for anonymous complex type</p>.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.</p>
         * 
         * <pre>{@code
         * <complexType>
         *   <complexContent>
         *     <extension base="{http://reference.e-government.gv.at/namespace/moa/20020822#}CMSDataObjectInfoType">
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
            extends CMSDataObjectInfoType
        {


        }

    }

}
