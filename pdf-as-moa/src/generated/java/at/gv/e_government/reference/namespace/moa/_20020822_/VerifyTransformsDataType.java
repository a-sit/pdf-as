
package at.gv.e_government.reference.namespace.moa._20020822_;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElements;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VerifyTransformsDataType complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="VerifyTransformsDataType">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <choice maxOccurs="unbounded">
 *         <element ref="{http://reference.e-government.gv.at/namespace/moa/20020822#}VerifyTransformsInfoProfile"/>
 *         <element name="VerifyTransformsInfoProfileID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       </choice>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VerifyTransformsDataType", propOrder = {
    "verifyTransformsInfoProfileOrVerifyTransformsInfoProfileID"
})
public class VerifyTransformsDataType {

    /**
     * Ein oder mehrere Transformationswege kÃ¶nnen von
     * 					der Applikation an MOA mitgeteilt werden. Die zu prÃ¼fende Signatur
     * 					hat zumindest einem dieser Transformationswege zu entsprechen. Die
     * 					Angabe kann explizit oder als Profilbezeichner erfolgen.
     * 
     */
    @XmlElements({
        @XmlElement(name = "VerifyTransformsInfoProfile", type = VerifyTransformsInfoProfile.class),
        @XmlElement(name = "VerifyTransformsInfoProfileID", type = String.class)
    })
    protected List<Object> verifyTransformsInfoProfileOrVerifyTransformsInfoProfileID;

    /**
     * Ein oder mehrere Transformationswege kÃ¶nnen von
     * 					der Applikation an MOA mitgeteilt werden. Die zu prÃ¼fende Signatur
     * 					hat zumindest einem dieser Transformationswege zu entsprechen. Die
     * 					Angabe kann explizit oder als Profilbezeichner erfolgen.
     * 
     * Gets the value of the verifyTransformsInfoProfileOrVerifyTransformsInfoProfileID property.
     * 
     * <p>This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the verifyTransformsInfoProfileOrVerifyTransformsInfoProfileID property.</p>
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * </p>
     * <pre>
     * getVerifyTransformsInfoProfileOrVerifyTransformsInfoProfileID().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link VerifyTransformsInfoProfile }
     * {@link String }
     * </p>
     * 
     * 
     * @return
     *     The value of the verifyTransformsInfoProfileOrVerifyTransformsInfoProfileID property.
     */
    public List<Object> getVerifyTransformsInfoProfileOrVerifyTransformsInfoProfileID() {
        if (verifyTransformsInfoProfileOrVerifyTransformsInfoProfileID == null) {
            verifyTransformsInfoProfileOrVerifyTransformsInfoProfileID = new ArrayList<>();
        }
        return this.verifyTransformsInfoProfileOrVerifyTransformsInfoProfileID;
    }

}
