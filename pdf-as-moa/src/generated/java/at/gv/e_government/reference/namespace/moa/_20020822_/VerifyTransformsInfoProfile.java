
package at.gv.e_government.reference.namespace.moa._20020822_;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import org.w3._2000._09.xmldsig_.TransformsType;


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
 *         <element ref="{http://www.w3.org/2000/09/xmldsig#}Transforms" minOccurs="0"/>
 *         <element name="TransformParameter" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}TransformParameterType" maxOccurs="unbounded" minOccurs="0"/>
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
    "transforms",
    "transformParameter"
})
@XmlRootElement(name = "VerifyTransformsInfoProfile")
public class VerifyTransformsInfoProfile {

    @XmlElement(name = "Transforms", namespace = "http://www.w3.org/2000/09/xmldsig#")
    protected TransformsType transforms;
    /**
     * Alle impliziten Transformationsparameter, die
     * 							zum Durchlaufen der oben angefÃ¼hrten Transformationskette
     * 							bekannt sein mÃ¼ssen, mÃ¼ssen hier angefÃ¼hrt werden. Das
     * 							Attribut "URI" bezeichnet den Transformationsparameter in exakt
     * 							jener Weise, wie er in der zu Ã¼berprÃ¼fenden Signatur gebraucht
     * 							wird.
     * 
     */
    @XmlElement(name = "TransformParameter")
    protected List<TransformParameterType> transformParameter;

    /**
     * Gets the value of the transforms property.
     * 
     * @return
     *     possible object is
     *     {@link TransformsType }
     *     
     */
    public TransformsType getTransforms() {
        return transforms;
    }

    /**
     * Sets the value of the transforms property.
     * 
     * @param value
     *     allowed object is
     *     {@link TransformsType }
     *     
     */
    public void setTransforms(TransformsType value) {
        this.transforms = value;
    }

    /**
     * Alle impliziten Transformationsparameter, die
     * 							zum Durchlaufen der oben angefÃ¼hrten Transformationskette
     * 							bekannt sein mÃ¼ssen, mÃ¼ssen hier angefÃ¼hrt werden. Das
     * 							Attribut "URI" bezeichnet den Transformationsparameter in exakt
     * 							jener Weise, wie er in der zu Ã¼berprÃ¼fenden Signatur gebraucht
     * 							wird.
     * 
     * Gets the value of the transformParameter property.
     * 
     * <p>This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the transformParameter property.</p>
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * </p>
     * <pre>
     * getTransformParameter().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TransformParameterType }
     * </p>
     * 
     * 
     * @return
     *     The value of the transformParameter property.
     */
    public List<TransformParameterType> getTransformParameter() {
        if (transformParameter == null) {
            transformParameter = new ArrayList<>();
        }
        return this.transformParameter;
    }

}
