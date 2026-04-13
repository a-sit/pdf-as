
package at.gv.e_government.reference.namespace.moa._20020822_;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ReferencesCheckResultType complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="ReferencesCheckResultType">
 *   <complexContent>
 *     <restriction base="{http://reference.e-government.gv.at/namespace/moa/20020822#}CheckResultType">
 *       <sequence>
 *         <element name="Code" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger"/>
 *         <element name="Info" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}ReferencesCheckResultInfoType" minOccurs="0"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReferencesCheckResultType")
public class ReferencesCheckResultType
    extends CheckResultType
{


}
