
package at.gv.e_government.reference.namespace.moa._20020822_;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import org.w3._2000._09.xmldsig_.DigestMethodType;


/**
 * <p>Java-Klasse für TransformParameterType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="TransformParameterType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice minOccurs="0"&gt;
 *         &lt;element name="Base64Content" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/&gt;
 *         &lt;element name="Hash"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}DigestMethod"/&gt;
 *                   &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}DigestValue"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/choice&gt;
 *       &lt;attribute name="URI" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TransformParameterType", propOrder = {
    "base64Content",
    "hash"
})
public class TransformParameterType {

    @XmlElement(name = "Base64Content")
    protected byte[] base64Content;
    @XmlElement(name = "Hash")
    protected TransformParameterType.Hash hash;
    @XmlAttribute(name = "URI", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String uri;

    /**
     * Ruft den Wert der base64Content-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getBase64Content() {
        return base64Content;
    }

    /**
     * Legt den Wert der base64Content-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setBase64Content(byte[] value) {
        this.base64Content = value;
    }

    /**
     * Ruft den Wert der hash-Eigenschaft ab.
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
     * Legt den Wert der hash-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link TransformParameterType.Hash }
     *     
     */
    public void setHash(TransformParameterType.Hash value) {
        this.hash = value;
    }

    /**
     * Ruft den Wert der uri-Eigenschaft ab.
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
     * Legt den Wert der uri-Eigenschaft fest.
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
     * <p>Java-Klasse für anonymous complex type.
     * 
     * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}DigestMethod"/&gt;
     *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}DigestValue"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
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
         * Ruft den Wert der digestMethod-Eigenschaft ab.
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
         * Legt den Wert der digestMethod-Eigenschaft fest.
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
         * Ruft den Wert der digestValue-Eigenschaft ab.
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
         * Legt den Wert der digestValue-Eigenschaft fest.
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
