
package at.gv.e_government.reference.namespace.moa._20020822_;

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
 * <p>Java-Klasse für DataObjectInfoType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="DataObjectInfoType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="DataObject"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;extension base="{http://reference.e-government.gv.at/namespace/moa/20020822#}ContentOptionalRefType"&gt;
 *               &lt;/extension&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;choice&gt;
 *           &lt;element ref="{http://reference.e-government.gv.at/namespace/moa/20020822#}CreateTransformsInfoProfile"/&gt;
 *           &lt;element name="CreateTransformsInfoProfileID" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}ProfileIdentifierType"/&gt;
 *         &lt;/choice&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="Structure" use="required"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *             &lt;enumeration value="detached"/&gt;
 *             &lt;enumeration value="enveloping"/&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DataObjectInfoType", propOrder = {
    "dataObject",
    "createTransformsInfoProfile",
    "createTransformsInfoProfileID"
})
@XmlSeeAlso({
    at.gv.e_government.reference.namespace.moa._20020822_.CreateXMLSignatureRequestType.SingleSignatureInfo.DataObjectInfo.class
})
public class DataObjectInfoType {

    @XmlElement(name = "DataObject", required = true)
    protected DataObjectInfoType.DataObject dataObject;
    @XmlElement(name = "CreateTransformsInfoProfile")
    protected CreateTransformsInfoProfile createTransformsInfoProfile;
    @XmlElement(name = "CreateTransformsInfoProfileID")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String createTransformsInfoProfileID;
    @XmlAttribute(name = "Structure", required = true)
    protected String structure;

    /**
     * Ruft den Wert der dataObject-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DataObjectInfoType.DataObject }
     *     
     */
    public DataObjectInfoType.DataObject getDataObject() {
        return dataObject;
    }

    /**
     * Legt den Wert der dataObject-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DataObjectInfoType.DataObject }
     *     
     */
    public void setDataObject(DataObjectInfoType.DataObject value) {
        this.dataObject = value;
    }

    /**
     * Ruft den Wert der createTransformsInfoProfile-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CreateTransformsInfoProfile }
     *     
     */
    public CreateTransformsInfoProfile getCreateTransformsInfoProfile() {
        return createTransformsInfoProfile;
    }

    /**
     * Legt den Wert der createTransformsInfoProfile-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CreateTransformsInfoProfile }
     *     
     */
    public void setCreateTransformsInfoProfile(CreateTransformsInfoProfile value) {
        this.createTransformsInfoProfile = value;
    }

    /**
     * Ruft den Wert der createTransformsInfoProfileID-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreateTransformsInfoProfileID() {
        return createTransformsInfoProfileID;
    }

    /**
     * Legt den Wert der createTransformsInfoProfileID-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreateTransformsInfoProfileID(String value) {
        this.createTransformsInfoProfileID = value;
    }

    /**
     * Ruft den Wert der structure-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStructure() {
        return structure;
    }

    /**
     * Legt den Wert der structure-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStructure(String value) {
        this.structure = value;
    }


    /**
     * <p>Java-Klasse für anonymous complex type.
     * 
     * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;extension base="{http://reference.e-government.gv.at/namespace/moa/20020822#}ContentOptionalRefType"&gt;
     *     &lt;/extension&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class DataObject
        extends ContentOptionalRefType
    {


    }

}
