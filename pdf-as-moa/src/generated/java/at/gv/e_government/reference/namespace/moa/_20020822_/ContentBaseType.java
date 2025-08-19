
package at.gv.e_government.reference.namespace.moa._20020822_;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ContentBaseType complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="ContentBaseType">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <choice minOccurs="0">
 *         <element name="Base64Content" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *         <element name="XMLContent" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}XMLContentType"/>
 *         <element name="LocRefContent" type="{http://www.w3.org/2001/XMLSchema}anyURI"/>
 *       </choice>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
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
     * Gets the value of the base64Content property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getBase64Content() {
        return base64Content;
    }

    /**
     * Sets the value of the base64Content property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setBase64Content(byte[] value) {
        this.base64Content = value;
    }

    /**
     * Gets the value of the xmlContent property.
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
     * Sets the value of the xmlContent property.
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
     * Gets the value of the locRefContent property.
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
     * Sets the value of the locRefContent property.
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
