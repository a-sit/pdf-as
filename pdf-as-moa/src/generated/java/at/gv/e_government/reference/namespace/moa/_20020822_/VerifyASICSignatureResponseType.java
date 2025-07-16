
package at.gv.e_government.reference.namespace.moa._20020822_;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VerifyASICSignatureResponseType complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="VerifyASICSignatureResponseType">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="ASiCSignatureResult" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}ASICResultType" maxOccurs="unbounded" minOccurs="0"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VerifyASICSignatureResponseType", propOrder = {
    "aSiCSignatureResult"
})
public class VerifyASICSignatureResponseType {

    @XmlElement(name = "ASiCSignatureResult")
    protected List<ASICResultType> aSiCSignatureResult;

    /**
     * Gets the value of the aSiCSignatureResult property.
     * 
     * <p>This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the aSiCSignatureResult property.</p>
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * </p>
     * <pre>
     * getASiCSignatureResult().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ASICResultType }
     * </p>
     * 
     * 
     * @return
     *     The value of the aSiCSignatureResult property.
     */
    public List<ASICResultType> getASiCSignatureResult() {
        if (aSiCSignatureResult == null) {
            aSiCSignatureResult = new ArrayList<>();
        }
        return this.aSiCSignatureResult;
    }

}
