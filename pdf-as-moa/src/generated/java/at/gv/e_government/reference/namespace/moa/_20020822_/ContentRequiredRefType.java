
package at.gv.e_government.reference.namespace.moa._20020822_;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für ContentRequiredRefType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ContentRequiredRefType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://reference.e-government.gv.at/namespace/moa/20020822#}ContentOptionalRefType"&gt;
 *       &lt;choice minOccurs="0"&gt;
 *         &lt;element name="Base64Content" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/&gt;
 *         &lt;element name="XMLContent" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}XMLContentType"/&gt;
 *         &lt;element name="LocRefContent" type="{http://www.w3.org/2001/XMLSchema}anyURI"/&gt;
 *       &lt;/choice&gt;
 *       &lt;attribute name="Reference" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ContentRequiredRefType")
public class ContentRequiredRefType
    extends ContentOptionalRefType
{


}
