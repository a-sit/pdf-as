
package at.gv.e_government.reference.namespace.moa._20020822_;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für CreateCMSSignatureResponseType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="CreateCMSSignatureResponseType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice maxOccurs="unbounded"&gt;
 *         &lt;element name="CMSSignature" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/&gt;
 *         &lt;element ref="{http://reference.e-government.gv.at/namespace/moa/20020822#}ErrorResponse"/&gt;
 *       &lt;/choice&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CreateCMSSignatureResponseType", propOrder = {
    "cmsSignatureOrErrorResponse"
})
public class CreateCMSSignatureResponseType {

    @XmlElements({
        @XmlElement(name = "CMSSignature", type = byte[].class),
        @XmlElement(name = "ErrorResponse", type = ErrorResponseType.class)
    })
    protected List<Object> cmsSignatureOrErrorResponse;

    /**
     * Gets the value of the cmsSignatureOrErrorResponse property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cmsSignatureOrErrorResponse property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCMSSignatureOrErrorResponse().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ErrorResponseType }
     * byte[]
     * 
     */
    public List<Object> getCMSSignatureOrErrorResponse() {
        if (cmsSignatureOrErrorResponse == null) {
            cmsSignatureOrErrorResponse = new ArrayList<Object>();
        }
        return this.cmsSignatureOrErrorResponse;
    }

}
