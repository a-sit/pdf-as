
package at.gv.e_government.reference.namespace.moa._20020822_;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for XMLDataObjectAssociationType complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="XMLDataObjectAssociationType">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="MetaInfo" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}MetaInfoType" minOccurs="0"/>
 *         <element name="Content" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}ContentRequiredRefType"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
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
     * Gets the value of the metaInfo property.
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
     * Sets the value of the metaInfo property.
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
     * Gets the value of the content property.
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
     * Sets the value of the content property.
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
