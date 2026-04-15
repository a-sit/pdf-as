
package at.gv.e_government.reference.namespace.moa._20020822_;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAnyElement;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElements;
import jakarta.xml.bind.annotation.XmlType;
import org.w3c.dom.Element;


/**
 * <p>Java class for CreateXMLSignatureResponseType complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="CreateXMLSignatureResponseType">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <choice maxOccurs="unbounded">
 *         <element name="SignatureEnvironment">
 *           <complexType>
 *             <complexContent>
 *               <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 <sequence>
 *                   <any processContents='lax'/>
 *                 </sequence>
 *               </restriction>
 *             </complexContent>
 *           </complexType>
 *         </element>
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
@XmlType(name = "CreateXMLSignatureResponseType", propOrder = {
    "signatureEnvironmentOrErrorResponse"
})
public class CreateXMLSignatureResponseType {

    /**
     * KardinalitÃ¤t 1..oo erlaubt die Antwort auf eine
     * 					Stapelsignatur-Anfrage
     * 
     */
    @XmlElements({
        @XmlElement(name = "SignatureEnvironment", type = CreateXMLSignatureResponseType.SignatureEnvironment.class),
        @XmlElement(name = "ErrorResponse", type = ErrorResponseType.class)
    })
    protected List<Object> signatureEnvironmentOrErrorResponse;

    /**
     * KardinalitÃ¤t 1..oo erlaubt die Antwort auf eine
     * 					Stapelsignatur-Anfrage
     * 
     * Gets the value of the signatureEnvironmentOrErrorResponse property.
     * 
     * <p>This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the signatureEnvironmentOrErrorResponse property.</p>
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * </p>
     * <pre>
     * getSignatureEnvironmentOrErrorResponse().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CreateXMLSignatureResponseType.SignatureEnvironment }
     * {@link ErrorResponseType }
     * </p>
     * 
     * 
     * @return
     *     The value of the signatureEnvironmentOrErrorResponse property.
     */
    public List<Object> getSignatureEnvironmentOrErrorResponse() {
        if (signatureEnvironmentOrErrorResponse == null) {
            signatureEnvironmentOrErrorResponse = new ArrayList<>();
        }
        return this.signatureEnvironmentOrErrorResponse;
    }


    /**
     * <p>Java class for anonymous complex type</p>.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.</p>
     * 
     * <pre>{@code
     * <complexType>
     *   <complexContent>
     *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       <sequence>
     *         <any processContents='lax'/>
     *       </sequence>
     *     </restriction>
     *   </complexContent>
     * </complexType>
     * }</pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "any"
    })
    public static class SignatureEnvironment {

        @XmlAnyElement(lax = true)
        protected Object any;

        /**
         * Gets the value of the any property.
         * 
         * @return
         *     possible object is
         *     {@link Object }
         *     {@link Element }
         *     
         */
        public Object getAny() {
            return any;
        }

        /**
         * Sets the value of the any property.
         * 
         * @param value
         *     allowed object is
         *     {@link Object }
         *     {@link Element }
         *     
         */
        public void setAny(Object value) {
            this.any = value;
        }

    }

}
