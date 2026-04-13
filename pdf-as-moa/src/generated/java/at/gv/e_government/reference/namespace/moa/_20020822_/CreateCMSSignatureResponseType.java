
package at.gv.e_government.reference.namespace.moa._20020822_;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElements;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CreateCMSSignatureResponseType complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="CreateCMSSignatureResponseType">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <choice maxOccurs="unbounded">
 *         <element name="CMSSignature" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *         <element ref="{http://reference.e-government.gv.at/namespace/moa/20020822#}ErrorResponse"/>
 *       </choice>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CreateCMSSignatureResponseType", propOrder = {
    "cmsSignatureOrErrorResponse"
})
public class CreateCMSSignatureResponseType {

    /**
     * KardinalitÃ¤t 1..oo erlaubt die Antwort auf eine
     * 					Stapelsignatur-Anfrage
     * 
     */
    @XmlElements({
        @XmlElement(name = "CMSSignature", type = byte[].class),
        @XmlElement(name = "ErrorResponse", type = ErrorResponseType.class)
    })
    protected List<Object> cmsSignatureOrErrorResponse;

    /**
     * KardinalitÃ¤t 1..oo erlaubt die Antwort auf eine
     * 					Stapelsignatur-Anfrage
     * 
     * Gets the value of the cmsSignatureOrErrorResponse property.
     * 
     * <p>This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cmsSignatureOrErrorResponse property.</p>
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * </p>
     * <pre>
     * getCMSSignatureOrErrorResponse().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ErrorResponseType }
     * byte[]</p>
     * 
     * 
     * @return
     *     The value of the cmsSignatureOrErrorResponse property.
     */
    public List<Object> getCMSSignatureOrErrorResponse() {
        if (cmsSignatureOrErrorResponse == null) {
            cmsSignatureOrErrorResponse = new ArrayList<>();
        }
        return this.cmsSignatureOrErrorResponse;
    }

}
