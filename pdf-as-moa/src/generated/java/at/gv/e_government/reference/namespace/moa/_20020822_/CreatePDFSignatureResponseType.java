
package at.gv.e_government.reference.namespace.moa._20020822_;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für CreatePDFSignatureResponseType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="CreatePDFSignatureResponseType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="PDFSignature" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}PDFSignedRepsonse" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CreatePDFSignatureResponseType", propOrder = {
    "pdfSignature"
})
public class CreatePDFSignatureResponseType {

    @XmlElement(name = "PDFSignature", required = true)
    protected List<PDFSignedRepsonse> pdfSignature;

    /**
     * Gets the value of the pdfSignature property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the pdfSignature property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPDFSignature().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PDFSignedRepsonse }
     * 
     * 
     */
    public List<PDFSignedRepsonse> getPDFSignature() {
        if (pdfSignature == null) {
            pdfSignature = new ArrayList<PDFSignedRepsonse>();
        }
        return this.pdfSignature;
    }

}
