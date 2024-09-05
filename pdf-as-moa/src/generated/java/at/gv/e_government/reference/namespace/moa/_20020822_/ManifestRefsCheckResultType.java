
package at.gv.e_government.reference.namespace.moa._20020822_;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für ManifestRefsCheckResultType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ManifestRefsCheckResultType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://reference.e-government.gv.at/namespace/moa/20020822#}CheckResultType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Code" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger"/&gt;
 *         &lt;element name="Info" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}ManifestRefsCheckResultInfoType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ManifestRefsCheckResultType")
public class ManifestRefsCheckResultType
    extends CheckResultType
{


}
