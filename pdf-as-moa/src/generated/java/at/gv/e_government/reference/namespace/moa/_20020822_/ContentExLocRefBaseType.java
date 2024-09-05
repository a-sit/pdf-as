
package at.gv.e_government.reference.namespace.moa._20020822_;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für ContentExLocRefBaseType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ContentExLocRefBaseType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://reference.e-government.gv.at/namespace/moa/20020822#}ContentBaseType"&gt;
 *       &lt;choice minOccurs="0"&gt;
 *         &lt;element name="Base64Content" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/&gt;
 *         &lt;element name="XMLContent" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}XMLContentType"/&gt;
 *       &lt;/choice&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ContentExLocRefBaseType")
@XmlSeeAlso({
    InputDataType.class
})
public class ContentExLocRefBaseType
    extends ContentBaseType
{


}
