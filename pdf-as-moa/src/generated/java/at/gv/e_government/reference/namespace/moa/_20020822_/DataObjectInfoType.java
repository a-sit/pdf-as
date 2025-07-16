
package at.gv.e_government.reference.namespace.moa._20020822_;

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
 * <p>Java class for DataObjectInfoType complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="DataObjectInfoType">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="DataObject">
 *           <complexType>
 *             <complexContent>
 *               <extension base="{http://reference.e-government.gv.at/namespace/moa/20020822#}ContentOptionalRefType">
 *               </extension>
 *             </complexContent>
 *           </complexType>
 *         </element>
 *         <choice>
 *           <element ref="{http://reference.e-government.gv.at/namespace/moa/20020822#}CreateTransformsInfoProfile"/>
 *           <element name="CreateTransformsInfoProfileID" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}ProfileIdentifierType"/>
 *         </choice>
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
     * Gets the value of the dataObject property.
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
     * Sets the value of the dataObject property.
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
     * Gets the value of the createTransformsInfoProfile property.
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
     * Sets the value of the createTransformsInfoProfile property.
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
     * Gets the value of the createTransformsInfoProfileID property.
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
     * Sets the value of the createTransformsInfoProfileID property.
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
     *     <extension base="{http://reference.e-government.gv.at/namespace/moa/20020822#}ContentOptionalRefType">
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
        extends ContentOptionalRefType
    {


    }

}
