
package at.gv.e_government.reference.namespace.moa._20020822_;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für ContentBaseType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ContentBaseType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice minOccurs="0"&gt;
 *         &lt;element name="Base64Content" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/&gt;
 *         &lt;element name="XMLContent" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}XMLContentType"/&gt;
 *         &lt;element name="LocRefContent" type="{http://www.w3.org/2001/XMLSchema}anyURI"/&gt;
 *       &lt;/choice&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ContentBaseType", propOrder = {
    "base64Content",
    "xmlContent",
    "locRefContent"
})
@XmlSeeAlso({
    ContentExLocRefBaseType.class,
    ContentOptionalRefType.class
})
public class ContentBaseType {

    @XmlElement(name = "Base64Content")
    protected byte[] base64Content;
    @XmlElement(name = "XMLContent")
    protected XMLContentType xmlContent;
    @XmlElement(name = "LocRefContent")
    @XmlSchemaType(name = "anyURI")
    protected String locRefContent;

    /**
     * Ruft den Wert der base64Content-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getBase64Content() {
        return base64Content;
    }

    /**
     * Legt den Wert der base64Content-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setBase64Content(byte[] value) {
        this.base64Content = value;
    }

    /**
     * Ruft den Wert der xmlContent-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLContentType }
     *     
     */
    public XMLContentType getXMLContent() {
        return xmlContent;
    }

    /**
     * Legt den Wert der xmlContent-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLContentType }
     *     
     */
    public void setXMLContent(XMLContentType value) {
        this.xmlContent = value;
    }

    /**
     * Ruft den Wert der locRefContent-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocRefContent() {
        return locRefContent;
    }

    /**
     * Legt den Wert der locRefContent-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocRefContent(String value) {
        this.locRefContent = value;
    }

}
