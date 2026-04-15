
package at.gv.e_government.reference.namespace.moa._20020822_;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CMSDataObjectRequiredMetaType complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="CMSDataObjectRequiredMetaType">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="MetaInfo" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}MetaInfoType"/>
 *         <element name="Content" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}CMSContentBaseType"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CMSDataObjectRequiredMetaType", propOrder = {
    "metaInfo",
    "content"
})
@XmlSeeAlso({
    at.gv.e_government.reference.namespace.moa._20020822_.CMSDataObjectInfoType.DataObject.class
})
public class CMSDataObjectRequiredMetaType {

    @XmlElement(name = "MetaInfo", required = true)
    protected MetaInfoType metaInfo;
    @XmlElement(name = "Content", required = true)
    protected CMSContentBaseType content;

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
     *     {@link CMSContentBaseType }
     *     
     */
    public CMSContentBaseType getContent() {
        return content;
    }

    /**
     * Sets the value of the content property.
     * 
     * @param value
     *     allowed object is
     *     {@link CMSContentBaseType }
     *     
     */
    public void setContent(CMSContentBaseType value) {
        this.content = value;
    }

}
