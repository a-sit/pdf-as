
package at.gv.e_government.reference.namespace.moa._20020822_;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für PDFSignatureProperties complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="PDFSignatureProperties"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="SignatureCoversFullPDF" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="SignatureByteRange" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PDFSignatureProperties", propOrder = {
    "signatureCoversFullPDF",
    "signatureByteRange"
})
public class PDFSignatureProperties {

    @XmlElement(name = "SignatureCoversFullPDF")
    protected Boolean signatureCoversFullPDF;
    @XmlElement(name = "SignatureByteRange")
    protected String signatureByteRange;

    /**
     * Ruft den Wert der signatureCoversFullPDF-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSignatureCoversFullPDF() {
        return signatureCoversFullPDF;
    }

    /**
     * Legt den Wert der signatureCoversFullPDF-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSignatureCoversFullPDF(Boolean value) {
        this.signatureCoversFullPDF = value;
    }

    /**
     * Ruft den Wert der signatureByteRange-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSignatureByteRange() {
        return signatureByteRange;
    }

    /**
     * Legt den Wert der signatureByteRange-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSignatureByteRange(String value) {
        this.signatureByteRange = value;
    }

}
