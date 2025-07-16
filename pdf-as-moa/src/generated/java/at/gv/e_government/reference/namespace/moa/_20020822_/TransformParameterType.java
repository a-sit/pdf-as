
package at.gv.e_government.reference.namespace.moa._20020822_;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import org.w3._2000._09.xmldsig_.DigestMethodType;


/**
 * <p>Java class for TransformParameterType complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="TransformParameterType">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <choice minOccurs="0">
 *         <element name="Base64Content" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *         <element name="Hash">
 *           <complexType>
 *             <complexContent>
 *               <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 <sequence>
 *                   <element ref="{http://www.w3.org/2000/09/xmldsig#}DigestMethod"/>
 *                   <element ref="{http://www.w3.org/2000/09/xmldsig#}DigestValue"/>
 *                 </sequence>
 *               </restriction>
 *             </complexContent>
 *           </complexType>
 *         </element>
 *       </choice>
 *       <attribute name="URI" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TransformParameterType", propOrder = {
    "base64Content",
    "hash"
})
public class TransformParameterType {

    /**
     * Der Transformationsparameter explizit angegeben.
     * 
     */
    @XmlElement(name = "Base64Content")
    protected byte[] base64Content;
    /**
     * Der Hashwert des Transformationsparameters.
     * 
     */
    @XmlElement(name = "Hash")
    protected TransformParameterType.Hash hash;
    @XmlAttribute(name = "URI", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String uri;

    /**
     * Der Transformationsparameter explizit angegeben.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getBase64Content() {
        return base64Content;
    }

    /**
     * Sets the value of the base64Content property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     * @see #getBase64Content()
     */
    public void setBase64Content(byte[] value) {
        this.base64Content = value;
    }

    /**
     * Der Hashwert des Transformationsparameters.
     * 
     * @return
     *     possible object is
     *     {@link TransformParameterType.Hash }
     *     
     */
    public TransformParameterType.Hash getHash() {
        return hash;
    }

    /**
     * Sets the value of the hash property.
     * 
     * @param value
     *     allowed object is
     *     {@link TransformParameterType.Hash }
     *     
     * @see #getHash()
     */
    public void setHash(TransformParameterType.Hash value) {
        this.hash = value;
    }

    /**
     * Gets the value of the uri property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getURI() {
        return uri;
    }

    /**
     * Sets the value of the uri property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setURI(String value) {
        this.uri = value;
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
     *         <element ref="{http://www.w3.org/2000/09/xmldsig#}DigestMethod"/>
     *         <element ref="{http://www.w3.org/2000/09/xmldsig#}DigestValue"/>
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
        "digestMethod",
        "digestValue"
    })
    public static class Hash {

        @XmlElement(name = "DigestMethod", namespace = "http://www.w3.org/2000/09/xmldsig#", required = true)
        protected DigestMethodType digestMethod;
        @XmlElement(name = "DigestValue", namespace = "http://www.w3.org/2000/09/xmldsig#", required = true)
        protected String digestValue;

        /**
         * Gets the value of the digestMethod property.
         * 
         * @return
         *     possible object is
         *     {@link DigestMethodType }
         *     
         */
        public DigestMethodType getDigestMethod() {
            return digestMethod;
        }

        /**
         * Sets the value of the digestMethod property.
         * 
         * @param value
         *     allowed object is
         *     {@link DigestMethodType }
         *     
         */
        public void setDigestMethod(DigestMethodType value) {
            this.digestMethod = value;
        }

        /**
         * Gets the value of the digestValue property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDigestValue() {
            return digestValue;
        }

        /**
         * Sets the value of the digestValue property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDigestValue(String value) {
            this.digestValue = value;
        }

    }

}
