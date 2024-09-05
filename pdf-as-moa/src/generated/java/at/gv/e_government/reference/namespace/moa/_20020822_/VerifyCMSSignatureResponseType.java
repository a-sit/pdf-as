
package at.gv.e_government.reference.namespace.moa._20020822_;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlType;
import org.w3._2000._09.xmldsig_.KeyInfoType;


/**
 * <p>Java-Klasse für VerifyCMSSignatureResponseType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="VerifyCMSSignatureResponseType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence maxOccurs="unbounded"&gt;
 *         &lt;element name="SignerInfo" type="{http://www.w3.org/2000/09/xmldsig#}KeyInfoType"/&gt;
 *         &lt;element name="SignatureAlgorithm" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="SignatureCheck" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}CheckResultType"/&gt;
 *         &lt;element name="CertificateCheck" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}CheckResultType"/&gt;
 *         &lt;element name="FormCheckResult" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}FormResultType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="ExtendedCertificateCheck" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}ExtendedCertificateCheckResultType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VerifyCMSSignatureResponseType", propOrder = {
    "signerInfoAndSignatureAlgorithmAndSignatureCheck"
})
public class VerifyCMSSignatureResponseType {

    @XmlElementRefs({
        @XmlElementRef(name = "SignerInfo", namespace = "http://reference.e-government.gv.at/namespace/moa/20020822#", type = JAXBElement.class),
        @XmlElementRef(name = "SignatureAlgorithm", namespace = "http://reference.e-government.gv.at/namespace/moa/20020822#", type = JAXBElement.class),
        @XmlElementRef(name = "SignatureCheck", namespace = "http://reference.e-government.gv.at/namespace/moa/20020822#", type = JAXBElement.class),
        @XmlElementRef(name = "CertificateCheck", namespace = "http://reference.e-government.gv.at/namespace/moa/20020822#", type = JAXBElement.class),
        @XmlElementRef(name = "FormCheckResult", namespace = "http://reference.e-government.gv.at/namespace/moa/20020822#", type = JAXBElement.class),
        @XmlElementRef(name = "ExtendedCertificateCheck", namespace = "http://reference.e-government.gv.at/namespace/moa/20020822#", type = JAXBElement.class)
    })
    protected List<JAXBElement<?>> signerInfoAndSignatureAlgorithmAndSignatureCheck;

    /**
     * Gets the value of the signerInfoAndSignatureAlgorithmAndSignatureCheck property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the signerInfoAndSignatureAlgorithmAndSignatureCheck property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSignerInfoAndSignatureAlgorithmAndSignatureCheck().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link CheckResultType }{@code >}
     * {@link JAXBElement }{@code <}{@link CheckResultType }{@code >}
     * {@link JAXBElement }{@code <}{@link ExtendedCertificateCheckResultType }{@code >}
     * {@link JAXBElement }{@code <}{@link FormResultType }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link KeyInfoType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<?>> getSignerInfoAndSignatureAlgorithmAndSignatureCheck() {
        if (signerInfoAndSignatureAlgorithmAndSignatureCheck == null) {
            signerInfoAndSignatureAlgorithmAndSignatureCheck = new ArrayList<JAXBElement<?>>();
        }
        return this.signerInfoAndSignatureAlgorithmAndSignatureCheck;
    }

}
