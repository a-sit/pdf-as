
package at.gv.e_government.reference.namespace.moa._20020822_;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für CMSDataObjectInfoType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="CMSDataObjectInfoType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="DataObject"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;extension base="{http://reference.e-government.gv.at/namespace/moa/20020822#}CMSDataObjectRequiredMetaType"&gt;
 *               &lt;/extension&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
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
@XmlType(name = "CMSDataObjectInfoType", propOrder = {
    "dataObject"
})
@XmlSeeAlso({
    at.gv.e_government.reference.namespace.moa._20020822_.CreateCMSSignatureRequestType.SingleSignatureInfo.DataObjectInfo.class
})
public class CMSDataObjectInfoType {

    @XmlElement(name = "DataObject", required = true)
    protected CMSDataObjectInfoType.DataObject dataObject;
    @XmlAttribute(name = "Structure", required = true)
    protected String structure;

    /**
     * Ruft den Wert der dataObject-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CMSDataObjectInfoType.DataObject }
     *     
     */
    public CMSDataObjectInfoType.DataObject getDataObject() {
        return dataObject;
    }

    /**
     * Legt den Wert der dataObject-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CMSDataObjectInfoType.DataObject }
     *     
     */
    public void setDataObject(CMSDataObjectInfoType.DataObject value) {
        this.dataObject = value;
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
     *     &lt;extension base="{http://reference.e-government.gv.at/namespace/moa/20020822#}CMSDataObjectRequiredMetaType"&gt;
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
        extends CMSDataObjectRequiredMetaType
    {


    }

}
