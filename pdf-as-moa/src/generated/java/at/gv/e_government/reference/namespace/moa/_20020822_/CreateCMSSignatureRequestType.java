
package at.gv.e_government.reference.namespace.moa._20020822_;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für CreateCMSSignatureRequestType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="CreateCMSSignatureRequestType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="KeyIdentifier" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}KeyIdentifierType"/&gt;
 *         &lt;element name="SingleSignatureInfo" maxOccurs="unbounded"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="DataObjectInfo"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;extension base="{http://reference.e-government.gv.at/namespace/moa/20020822#}CMSDataObjectInfoType"&gt;
 *                         &lt;/extension&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                 &lt;/sequence&gt;
 *                 &lt;attribute name="SecurityLayerConformity" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" /&gt;
 *                 &lt;attribute name="PAdESConformity" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
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
    @XmlElement(name = "SingleSignatureInfo", required = true)
    protected List<CreateCMSSignatureRequestType.SingleSignatureInfo> singleSignatureInfo;

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
     * {@link CreateCMSSignatureRequestType.SingleSignatureInfo }
     * 
     * 
     */
    public List<CreateCMSSignatureRequestType.SingleSignatureInfo> getSingleSignatureInfo() {
        if (singleSignatureInfo == null) {
            singleSignatureInfo = new ArrayList<CreateCMSSignatureRequestType.SingleSignatureInfo>();
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
     *         &lt;element name="DataObjectInfo"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;extension base="{http://reference.e-government.gv.at/namespace/moa/20020822#}CMSDataObjectInfoType"&gt;
     *               &lt;/extension&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *       &lt;/sequence&gt;
     *       &lt;attribute name="SecurityLayerConformity" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" /&gt;
     *       &lt;attribute name="PAdESConformity" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
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
         * Ruft den Wert der dataObjectInfo-Eigenschaft ab.
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
         * Legt den Wert der dataObjectInfo-Eigenschaft fest.
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
         * Ruft den Wert der pAdESConformity-Eigenschaft ab.
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
         * Legt den Wert der pAdESConformity-Eigenschaft fest.
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
         * <p>Java-Klasse für anonymous complex type.
         * 
         * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
         * 
         * <pre>
         * &lt;complexType&gt;
         *   &lt;complexContent&gt;
         *     &lt;extension base="{http://reference.e-government.gv.at/namespace/moa/20020822#}CMSDataObjectInfoType"&gt;
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
            extends CMSDataObjectInfoType
        {


        }

    }

}
