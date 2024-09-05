
package at.gv.e_government.reference.namespace.moa._20020822_;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für XMLDataObjectAssociationType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="XMLDataObjectAssociationType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="MetaInfo" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}MetaInfoType" minOccurs="0"/&gt;
 *         &lt;element name="Content" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}ContentRequiredRefType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "XMLDataObjectAssociationType", propOrder = {
    "metaInfo",
    "content"
})
public class XMLDataObjectAssociationType {

    @XmlElement(name = "MetaInfo")
    protected MetaInfoType metaInfo;
    @XmlElement(name = "Content", required = true)
    protected ContentRequiredRefType content;

    /**
     * Ruft den Wert der metaInfo-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link MetaInfoType }
     *     
     */
    public MetaInfoType getMetaInfo() {
        return metaInfo;
    }

    /**
     * Legt den Wert der metaInfo-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link MetaInfoType }
     *     
     */
    public void setMetaInfo(MetaInfoType value) {
        this.metaInfo = value;
    }

    /**
     * Ruft den Wert der content-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ContentRequiredRefType }
     *     
     */
    public ContentRequiredRefType getContent() {
        return content;
    }

    /**
     * Legt den Wert der content-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ContentRequiredRefType }
     *     
     */
    public void setContent(ContentRequiredRefType value) {
        this.content = value;
    }

}
