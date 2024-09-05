
package at.gv.e_government.reference.namespace.moa._20020822_;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für VerifyASICSignatureResponseType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="VerifyASICSignatureResponseType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ASiCSignatureResult" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}ASICResultType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
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
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the aSiCSignatureResult property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getASiCSignatureResult().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ASICResultType }
     * 
     * 
     */
    public List<ASICResultType> getASiCSignatureResult() {
        if (aSiCSignatureResult == null) {
            aSiCSignatureResult = new ArrayList<ASICResultType>();
        }
        return this.aSiCSignatureResult;
    }

}
