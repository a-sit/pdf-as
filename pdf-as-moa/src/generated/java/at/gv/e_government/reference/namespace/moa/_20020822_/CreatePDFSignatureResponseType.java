
package at.gv.e_government.reference.namespace.moa._20020822_;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CreatePDFSignatureResponseType complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="CreatePDFSignatureResponseType">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="PDFSignature" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}PDFSignedRepsonse" maxOccurs="unbounded"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
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
     * <p>This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the pdfSignature property.</p>
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * </p>
     * <pre>
     * getPDFSignature().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PDFSignedRepsonse }
     * </p>
     * 
     * 
     * @return
     *     The value of the pdfSignature property.
     */
    public List<PDFSignedRepsonse> getPDFSignature() {
        if (pdfSignature == null) {
            pdfSignature = new ArrayList<>();
        }
        return this.pdfSignature;
    }

}
