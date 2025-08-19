
package at.gv.e_government.reference.namespace.moa._20020822_;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CMSDataObjectInfoType complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="CMSDataObjectInfoType">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="DataObject">
 *           <complexType>
 *             <complexContent>
 *               <extension base="{http://reference.e-government.gv.at/namespace/moa/20020822#}CMSDataObjectRequiredMetaType">
 *               </extension>
 *             </complexContent>
 *           </complexType>
 *         </element>
 *       </sequence>
 *       <attribute name="Structure" use="required">
 *         <simpleType>
 *           <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             <enumeration value="detached"/>
 *             <enumeration value="enveloping"/>
 *           </restriction>
 *         </simpleType>
 *       </attribute>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
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
     * Gets the value of the dataObject property.
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
     * Sets the value of the dataObject property.
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
     * Gets the value of the structure property.
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
     * Sets the value of the structure property.
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
     * <p>Java class for anonymous complex type</p>.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.</p>
     * 
     * <pre>{@code
     * <complexType>
     *   <complexContent>
     *     <extension base="{http://reference.e-government.gv.at/namespace/moa/20020822#}CMSDataObjectRequiredMetaType">
     *     </extension>
     *   </complexContent>
     * </complexType>
     * }</pre>
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
