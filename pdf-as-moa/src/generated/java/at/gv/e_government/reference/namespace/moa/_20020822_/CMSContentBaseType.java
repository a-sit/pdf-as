
package at.gv.e_government.reference.namespace.moa._20020822_;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CMSContentBaseType complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="CMSContentBaseType">
 *   <complexContent>
 *     <restriction base="{http://reference.e-government.gv.at/namespace/moa/20020822#}ContentOptionalRefType">
 *       <choice minOccurs="0">
 *         <element name="Base64Content" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *       </choice>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CMSContentBaseType")
public class CMSContentBaseType
    extends ContentOptionalRefType
{


}
