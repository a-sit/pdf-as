
package at.gv.e_government.reference.namespace.moa._20020822_;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VerifyPDFSignatureResponseType complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="VerifyPDFSignatureResponseType">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence maxOccurs="unbounded">
 *         <element name="SignatureResult" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}PDFSignatureResultType"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VerifyPDFSignatureResponseType", propOrder = {
    "signatureResult"
})
public class VerifyPDFSignatureResponseType {

    @XmlElement(name = "SignatureResult", required = true)
    protected List<PDFSignatureResultType> signatureResult;

    /**
     * Gets the value of the signatureResult property.
     * 
     * <p>This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the signatureResult property.</p>
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * </p>
     * <pre>
     * getSignatureResult().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PDFSignatureResultType }
     * </p>
     * 
     * 
     * @return
     *     The value of the signatureResult property.
     */
    public List<PDFSignatureResultType> getSignatureResult() {
        if (signatureResult == null) {
            signatureResult = new ArrayList<>();
        }
        return this.signatureResult;
    }

}
