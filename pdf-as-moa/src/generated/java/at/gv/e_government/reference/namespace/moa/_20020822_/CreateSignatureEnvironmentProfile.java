
package at.gv.e_government.reference.namespace.moa._20020822_;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
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
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="CreateSignatureLocation" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}CreateSignatureLocationType"/>
 *         <element name="Supplement" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}XMLDataObjectAssociationType" maxOccurs="unbounded" minOccurs="0"/>
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
    "createSignatureLocation",
    "supplement"
})
@XmlRootElement(name = "CreateSignatureEnvironmentProfile")
public class CreateSignatureEnvironmentProfile {

    @XmlElement(name = "CreateSignatureLocation", required = true)
    protected CreateSignatureLocationType createSignatureLocation;
    @XmlElement(name = "Supplement")
    protected List<XMLDataObjectAssociationType> supplement;

    /**
     * Gets the value of the createSignatureLocation property.
     * 
     * @return
     *     possible object is
     *     {@link CreateSignatureLocationType }
     *     
     */
    public CreateSignatureLocationType getCreateSignatureLocation() {
        return createSignatureLocation;
    }

    /**
     * Sets the value of the createSignatureLocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link CreateSignatureLocationType }
     *     
     */
    public void setCreateSignatureLocation(CreateSignatureLocationType value) {
        this.createSignatureLocation = value;
    }

    /**
     * Gets the value of the supplement property.
     * 
     * <p>This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the supplement property.</p>
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * </p>
     * <pre>
     * getSupplement().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XMLDataObjectAssociationType }
     * </p>
     * 
     * 
     * @return
     *     The value of the supplement property.
     */
    public List<XMLDataObjectAssociationType> getSupplement() {
        if (supplement == null) {
            supplement = new ArrayList<>();
        }
        return this.supplement;
    }

}
