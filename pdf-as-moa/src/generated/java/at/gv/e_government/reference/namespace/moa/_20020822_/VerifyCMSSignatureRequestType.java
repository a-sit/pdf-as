
package at.gv.e_government.reference.namespace.moa._20020822_;

import javax.xml.datatype.XMLGregorianCalendar;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for VerifyCMSSignatureRequestType complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="VerifyCMSSignatureRequestType">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="DateTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         <element name="ExtendedValidation" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         <element name="CMSSignature" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *         <element name="DataObject" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}CMSDataObjectOptionalMetaType" minOccurs="0"/>
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
@XmlType(name = "VerifyCMSSignatureRequestType", propOrder = {
    "dateTime",
    "extendedValidation",
    "cmsSignature",
    "dataObject",
    "trustProfileID"
})
@XmlSeeAlso({
    VerifyCMSSignatureRequest.class
})
public class VerifyCMSSignatureRequestType {

    @XmlElement(name = "DateTime")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateTime;
    @XmlElement(name = "ExtendedValidation", defaultValue = "false")
    protected Boolean extendedValidation;
    @XmlElement(name = "CMSSignature", required = true)
    protected byte[] cmsSignature;
    @XmlElement(name = "DataObject")
    protected CMSDataObjectOptionalMetaType dataObject;
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
     * Gets the value of the cmsSignature property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getCMSSignature() {
        return cmsSignature;
    }

    /**
     * Sets the value of the cmsSignature property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setCMSSignature(byte[] value) {
        this.cmsSignature = value;
    }

    /**
     * Gets the value of the dataObject property.
     * 
     * @return
     *     possible object is
     *     {@link CMSDataObjectOptionalMetaType }
     *     
     */
    public CMSDataObjectOptionalMetaType getDataObject() {
        return dataObject;
    }

    /**
     * Sets the value of the dataObject property.
     * 
     * @param value
     *     allowed object is
     *     {@link CMSDataObjectOptionalMetaType }
     *     
     */
    public void setDataObject(CMSDataObjectOptionalMetaType value) {
        this.dataObject = value;
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

}
