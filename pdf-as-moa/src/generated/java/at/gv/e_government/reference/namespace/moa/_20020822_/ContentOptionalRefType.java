
package at.gv.e_government.reference.namespace.moa._20020822_;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für ContentOptionalRefType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ContentOptionalRefType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://reference.e-government.gv.at/namespace/moa/20020822#}ContentBaseType"&gt;
 *       &lt;attribute name="Reference" type="{http://www.w3.org/2001/XMLSchema}anyURI" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
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
     * Ruft den Wert der reference-Eigenschaft ab.
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
     * Legt den Wert der reference-Eigenschaft fest.
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
