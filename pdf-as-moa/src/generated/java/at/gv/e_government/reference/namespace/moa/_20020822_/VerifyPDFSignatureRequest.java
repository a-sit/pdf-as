
package at.gv.e_government.reference.namespace.moa._20020822_;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType>
 *   <complexContent>
 *     <extension base="{http://reference.e-government.gv.at/namespace/moa/20020822#}VerifyPDFSignatureRequestType">
 *       <attribute name="Signatories" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}SignatoriesType" default="1" />
 *     </extension>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "VerifyPDFSignatureRequest")
public class VerifyPDFSignatureRequest
    extends VerifyPDFSignatureRequestType
{

    @XmlAttribute(name = "Signatories")
    protected List<String> signatories;

    /**
     * Gets the value of the signatories property.
     * 
     * <p>This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the signatories property.</p>
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * </p>
     * <pre>
     * getSignatories().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * </p>
     * 
     * 
     * @return
     *     The value of the signatories property.
     */
    public List<String> getSignatories() {
        if (signatories == null) {
            signatories = new ArrayList<>();
        }
        return this.signatories;
    }

}
