
package at.gv.e_government.reference.namespace.moa._20020822_;

import java.util.ArrayList;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlElementRefs;
import jakarta.xml.bind.annotation.XmlType;
import org.w3._2000._09.xmldsig_.KeyInfoType;


/**
 * <p>Java class for VerifyASICCMSSignatureResponseType complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="VerifyASICCMSSignatureResponseType">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence maxOccurs="unbounded">
 *         <element name="SignerInfo" type="{http://www.w3.org/2000/09/xmldsig#}KeyInfoType"/>
 *         <element name="SigningTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         <element name="SignatureCheck" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}CheckResultType"/>
 *         <element name="CertificateCheck" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}CheckResultType"/>
 *         <element name="FormCheckResult" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}FormResultType" maxOccurs="unbounded" minOccurs="0"/>
 *         <element name="ExtendedCertificateCheck" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}ExtendedCertificateCheckResultType" minOccurs="0"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VerifyASICCMSSignatureResponseType", propOrder = {
    "signerInfoAndSigningTimeAndSignatureCheck"
})
public class VerifyASICCMSSignatureResponseType {

    @XmlElementRefs({
        @XmlElementRef(name = "SignerInfo", namespace = "http://reference.e-government.gv.at/namespace/moa/20020822#", type = JAXBElement.class),
        @XmlElementRef(name = "SigningTime", namespace = "http://reference.e-government.gv.at/namespace/moa/20020822#", type = JAXBElement.class),
        @XmlElementRef(name = "SignatureCheck", namespace = "http://reference.e-government.gv.at/namespace/moa/20020822#", type = JAXBElement.class),
        @XmlElementRef(name = "CertificateCheck", namespace = "http://reference.e-government.gv.at/namespace/moa/20020822#", type = JAXBElement.class),
        @XmlElementRef(name = "FormCheckResult", namespace = "http://reference.e-government.gv.at/namespace/moa/20020822#", type = JAXBElement.class),
        @XmlElementRef(name = "ExtendedCertificateCheck", namespace = "http://reference.e-government.gv.at/namespace/moa/20020822#", type = JAXBElement.class)
    })
    protected List<JAXBElement<?>> signerInfoAndSigningTimeAndSignatureCheck;

    /**
     * Gets the value of the signerInfoAndSigningTimeAndSignatureCheck property.
     * 
     * <p>This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the signerInfoAndSigningTimeAndSignatureCheck property.</p>
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * </p>
     * <pre>
     * getSignerInfoAndSigningTimeAndSignatureCheck().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link CheckResultType }{@code >}
     * {@link JAXBElement }{@code <}{@link CheckResultType }{@code >}
     * {@link JAXBElement }{@code <}{@link ExtendedCertificateCheckResultType }{@code >}
     * {@link JAXBElement }{@code <}{@link FormResultType }{@code >}
     * {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     * {@link JAXBElement }{@code <}{@link KeyInfoType }{@code >}
     * </p>
     * 
     * 
     * @return
     *     The value of the signerInfoAndSigningTimeAndSignatureCheck property.
     */
    public List<JAXBElement<?>> getSignerInfoAndSigningTimeAndSignatureCheck() {
        if (signerInfoAndSigningTimeAndSignatureCheck == null) {
            signerInfoAndSigningTimeAndSignatureCheck = new ArrayList<>();
        }
        return this.signerInfoAndSigningTimeAndSignatureCheck;
    }

}
