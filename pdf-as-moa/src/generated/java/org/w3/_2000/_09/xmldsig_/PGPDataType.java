
package org.w3._2000._09.xmldsig_;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAnyElement;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlElementRefs;
import jakarta.xml.bind.annotation.XmlType;
import org.w3c.dom.Element;


/**
 * <p>Java class for PGPDataType complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="PGPDataType">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <choice>
 *         <sequence>
 *           <element name="PGPKeyID" type="{http://www.w3.org/2000/09/xmldsig#}CryptoBinary"/>
 *           <element name="PGPKeyPacket" type="{http://www.w3.org/2000/09/xmldsig#}CryptoBinary" minOccurs="0"/>
 *           <any processContents='lax' namespace='##other' maxOccurs="unbounded" minOccurs="0"/>
 *         </sequence>
 *         <sequence>
 *           <element name="PGPKeyPacket" type="{http://www.w3.org/2000/09/xmldsig#}CryptoBinary"/>
 *           <any processContents='lax' namespace='##other' maxOccurs="unbounded" minOccurs="0"/>
 *         </sequence>
 *       </choice>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PGPDataType", propOrder = {
    "content"
})
public class PGPDataType {

    /**
     * Gets the rest of the content model. 
     * 
     * <p>
     * You are getting this "catch-all" property because of the following reason: 
     * The field name "PGPKeyPacket" is used by two different parts of a schema. See: 
     * line 184 of file:/home/gpalfinger/Documents/pdf-as-4-kiro/pdf-as-moa/src/main/resources/wsdl/W3C-XMLDSig.xsd
     * line 180 of file:/home/gpalfinger/Documents/pdf-as-4-kiro/pdf-as-moa/src/main/resources/wsdl/W3C-XMLDSig.xsd
     * <p>
     * To get rid of this property, apply a property customization to one 
     * of both of the following declarations to change their names:
     * 
     */
    @XmlElementRefs({
        @XmlElementRef(name = "PGPKeyID", namespace = "http://www.w3.org/2000/09/xmldsig#", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "PGPKeyPacket", namespace = "http://www.w3.org/2000/09/xmldsig#", type = JAXBElement.class, required = false)
    })
    @XmlAnyElement(lax = true)
    protected List<Object> content;

    /**
     * Gets the rest of the content model. 
     * 
     * <p>
     * You are getting this "catch-all" property because of the following reason: 
     * The field name "PGPKeyPacket" is used by two different parts of a schema. See: 
     * line 184 of file:/home/gpalfinger/Documents/pdf-as-4-kiro/pdf-as-moa/src/main/resources/wsdl/W3C-XMLDSig.xsd
     * line 180 of file:/home/gpalfinger/Documents/pdf-as-4-kiro/pdf-as-moa/src/main/resources/wsdl/W3C-XMLDSig.xsd
     * <p>
     * To get rid of this property, apply a property customization to one 
     * of both of the following declarations to change their names:
     * 
     * Gets the value of the content property.
     * 
     * <p>This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the content property.</p>
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * </p>
     * <pre>
     * getContent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link Object }
     * {@link Element }
     * </p>
     * 
     * 
     * @return
     *     The value of the content property.
     */
    public List<Object> getContent() {
        if (content == null) {
            content = new ArrayList<>();
        }
        return this.content;
    }

}
