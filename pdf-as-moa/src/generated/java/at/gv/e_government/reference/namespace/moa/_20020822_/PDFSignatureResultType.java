
package at.gv.e_government.reference.namespace.moa._20020822_;

import java.util.ArrayList;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import org.w3._2000._09.xmldsig_.KeyInfoType;


/**
 * <p>Java class for PDFSignatureResultType complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="PDFSignatureResultType">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="SignerInfo" type="{http://www.w3.org/2000/09/xmldsig#}KeyInfoType" minOccurs="0"/>
 *         <element name="SigningTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         <element name="SignatureAlgorithm" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="SignatureCheck" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}CheckResultType"/>
 *         <element name="CertificateCheck" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}CheckResultType"/>
 *         <element name="FormCheckResult" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}FormResultType" maxOccurs="unbounded" minOccurs="0"/>
 *         <element name="ExtendedCertificateCheck" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}ExtendedCertificateCheckResultType" minOccurs="0"/>
 *         <element name="SignatureProperties" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}PDFSignatureProperties" minOccurs="0"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PDFSignatureResultType", propOrder = {
    "signerInfo",
    "signingTime",
    "signatureAlgorithm",
    "signatureCheck",
    "certificateCheck",
    "formCheckResult",
    "extendedCertificateCheck",
    "signatureProperties"
})
public class PDFSignatureResultType {

    /**
     * only ds:X509Data and RetrievalMethod is
     * 						supported; QualifiedCertificate is included as
     * 						X509Data/any;publicAuthority is included as X509Data/any;
     * 						SecureSignatureCreationDevice is included as X509Data/any,
     * 						IssuingCountry is included as X509Data/any
     * 
     */
    @XmlElement(name = "SignerInfo")
    protected KeyInfoType signerInfo;
    @XmlElement(name = "SigningTime")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar signingTime;
    @XmlElement(name = "SignatureAlgorithm")
    protected String signatureAlgorithm;
    @XmlElement(name = "SignatureCheck", required = true)
    protected CheckResultType signatureCheck;
    @XmlElement(name = "CertificateCheck", required = true)
    protected CheckResultType certificateCheck;
    @XmlElement(name = "FormCheckResult")
    protected List<FormResultType> formCheckResult;
    @XmlElement(name = "ExtendedCertificateCheck")
    protected ExtendedCertificateCheckResultType extendedCertificateCheck;
    @XmlElement(name = "SignatureProperties")
    protected PDFSignatureProperties signatureProperties;

    /**
     * only ds:X509Data and RetrievalMethod is
     * 						supported; QualifiedCertificate is included as
     * 						X509Data/any;publicAuthority is included as X509Data/any;
     * 						SecureSignatureCreationDevice is included as X509Data/any,
     * 						IssuingCountry is included as X509Data/any
     * 
     * @return
     *     possible object is
     *     {@link KeyInfoType }
     *     
     */
    public KeyInfoType getSignerInfo() {
        return signerInfo;
    }

    /**
     * Sets the value of the signerInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link KeyInfoType }
     *     
     * @see #getSignerInfo()
     */
    public void setSignerInfo(KeyInfoType value) {
        this.signerInfo = value;
    }

    /**
     * Gets the value of the signingTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getSigningTime() {
        return signingTime;
    }

    /**
     * Sets the value of the signingTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setSigningTime(XMLGregorianCalendar value) {
        this.signingTime = value;
    }

    /**
     * Gets the value of the signatureAlgorithm property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSignatureAlgorithm() {
        return signatureAlgorithm;
    }

    /**
     * Sets the value of the signatureAlgorithm property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSignatureAlgorithm(String value) {
        this.signatureAlgorithm = value;
    }

    /**
     * Gets the value of the signatureCheck property.
     * 
     * @return
     *     possible object is
     *     {@link CheckResultType }
     *     
     */
    public CheckResultType getSignatureCheck() {
        return signatureCheck;
    }

    /**
     * Sets the value of the signatureCheck property.
     * 
     * @param value
     *     allowed object is
     *     {@link CheckResultType }
     *     
     */
    public void setSignatureCheck(CheckResultType value) {
        this.signatureCheck = value;
    }

    /**
     * Gets the value of the certificateCheck property.
     * 
     * @return
     *     possible object is
     *     {@link CheckResultType }
     *     
     */
    public CheckResultType getCertificateCheck() {
        return certificateCheck;
    }

    /**
     * Sets the value of the certificateCheck property.
     * 
     * @param value
     *     allowed object is
     *     {@link CheckResultType }
     *     
     */
    public void setCertificateCheck(CheckResultType value) {
        this.certificateCheck = value;
    }

    /**
     * Gets the value of the formCheckResult property.
     * 
     * <p>This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the formCheckResult property.</p>
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * </p>
     * <pre>
     * getFormCheckResult().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FormResultType }
     * </p>
     * 
     * 
     * @return
     *     The value of the formCheckResult property.
     */
    public List<FormResultType> getFormCheckResult() {
        if (formCheckResult == null) {
            formCheckResult = new ArrayList<>();
        }
        return this.formCheckResult;
    }

    /**
     * Gets the value of the extendedCertificateCheck property.
     * 
     * @return
     *     possible object is
     *     {@link ExtendedCertificateCheckResultType }
     *     
     */
    public ExtendedCertificateCheckResultType getExtendedCertificateCheck() {
        return extendedCertificateCheck;
    }

    /**
     * Sets the value of the extendedCertificateCheck property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExtendedCertificateCheckResultType }
     *     
     */
    public void setExtendedCertificateCheck(ExtendedCertificateCheckResultType value) {
        this.extendedCertificateCheck = value;
    }

    /**
     * Gets the value of the signatureProperties property.
     * 
     * @return
     *     possible object is
     *     {@link PDFSignatureProperties }
     *     
     */
    public PDFSignatureProperties getSignatureProperties() {
        return signatureProperties;
    }

    /**
     * Sets the value of the signatureProperties property.
     * 
     * @param value
     *     allowed object is
     *     {@link PDFSignatureProperties }
     *     
     */
    public void setSignatureProperties(PDFSignatureProperties value) {
        this.signatureProperties = value;
    }

}
