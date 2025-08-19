
package at.gv.e_government.reference.namespace.moa._20020822_;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ContentOptionalRefType complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="ContentOptionalRefType">
 *   <complexContent>
 *     <extension base="{http://reference.e-government.gv.at/namespace/moa/20020822#}ContentBaseType">
 *       <attribute name="Reference" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *     </extension>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ContentOptionalRefType")
@XmlSeeAlso({
    at.gv.e_government.reference.namespace.moa._20020822_.DataObjectInfoType.DataObject.class,
    CMSContentBaseType.class,
    ContentRequiredRefType.class
})
public class ContentOptionalRefType
    extends ContentBaseType
{

    @XmlAttribute(name = "Reference")
    @XmlSchemaType(name = "anyURI")
    protected String reference;

    /**
     * Gets the value of the reference property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReference() {
        return reference;
    }

    /**
     * Sets the value of the reference property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReference(String value) {
        this.reference = value;
    }

}
