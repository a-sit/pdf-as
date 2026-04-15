
package at.gv.e_government.reference.namespace.moa._20020822_;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ManifestRefsCheckResultInfoType complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="ManifestRefsCheckResultInfoType">
 *   <complexContent>
 *     <restriction base="{http://reference.e-government.gv.at/namespace/moa/20020822#}AnyChildrenType">
 *       <sequence>
 *         <any processContents='lax' namespace='##other' maxOccurs="unbounded" minOccurs="0"/>
 *         <element name="FailedReference" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" maxOccurs="unbounded" minOccurs="0"/>
 *         <element name="ReferringSigReference" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ManifestRefsCheckResultInfoType")
public class ManifestRefsCheckResultInfoType
    extends AnyChildrenType
{


}
