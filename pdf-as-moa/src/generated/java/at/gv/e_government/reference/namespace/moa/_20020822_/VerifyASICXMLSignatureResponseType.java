
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
 * <p>Java class for VerifyASICXMLSignatureResponseType complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="VerifyASICXMLSignatureResponseType">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="SignerInfo" type="{http://www.w3.org/2000/09/xmldsig#}KeyInfoType"/>
 *         <element name="SigningTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         <element name="HashInputData" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}InputDataType" maxOccurs="unbounded" minOccurs="0"/>
 *         <element name="ReferenceInputData" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}InputDataType" maxOccurs="unbounded" minOccurs="0"/>
 *         <element name="SignatureCheck" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}ReferencesCheckResultType"/>
 *         <element name="SignatureManifestCheck" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}ReferencesCheckResultType" minOccurs="0"/>
 *         <element name="XMLDSIGManifestCheck" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}ManifestRefsCheckResultType" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "VerifyASICXMLSignatureResponseType", propOrder = {
    "signerInfo",
    "signingTime",
    "hashInputData",
    "referenceInputData",
    "signatureCheck",
    "signatureManifestCheck",
    "xmldsigManifestCheck",
    "certificateCheck",
    "formCheckResult",
    "extendedCertificateCheck"
})
public class VerifyASICXMLSignatureResponseType {

    /**
     * only ds:X509Data and ds:RetrievalMethod is
     * 						supported; QualifiedCertificate is included as X509Data/any;
     * 						PublicAuthority is included as X509Data/any;
     * 						SecureSignatureCreationDevice is included as X509Data/any,
     * 						IssuingCountry is included as X509Data/any
     * 
     */
    @XmlElement(name = "SignerInfo", required = true)
    protected KeyInfoType signerInfo;
    @XmlElement(name = "SigningTime")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar signingTime;
    @XmlElement(name = "HashInputData")
    protected List<InputDataType> hashInputData;
    @XmlElement(name = "ReferenceInputData")
    protected List<InputDataType> referenceInputData;
    @XmlElement(name = "SignatureCheck", required = true)
    protected ReferencesCheckResultType signatureCheck;
    @XmlElement(name = "SignatureManifestCheck")
    protected ReferencesCheckResultType signatureManifestCheck;
    @XmlElement(name = "XMLDSIGManifestCheck")
    protected List<ManifestRefsCheckResultType> xmldsigManifestCheck;
    @XmlElement(name = "CertificateCheck", required = true)
    protected CheckResultType certificateCheck;
    @XmlElement(name = "FormCheckResult")
    protected List<FormResultType> formCheckResult;
    @XmlElement(name = "ExtendedCertificateCheck")
    protected ExtendedCertificateCheckResultType extendedCertificateCheck;

    /**
     * only ds:X509Data and ds:RetrievalMethod is
     * 						supported; QualifiedCertificate is included as X509Data/any;
     * 						PublicAuthority is included as X509Data/any;
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
     * Gets the value of the hashInputData property.
     * 
     * <p>This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the hashInputData property.</p>
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * </p>
     * <pre>
     * getHashInputData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InputDataType }
     * </p>
     * 
     * 
     * @return
     *     The value of the hashInputData property.
     */
    public List<InputDataType> getHashInputData() {
        if (hashInputData == null) {
            hashInputData = new ArrayList<>();
        }
        return this.hashInputData;
    }

    /**
     * Gets the value of the referenceInputData property.
     * 
     * <p>This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the referenceInputData property.</p>
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * </p>
     * <pre>
     * getReferenceInputData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InputDataType }
     * </p>
     * 
     * 
     * @return
     *     The value of the referenceInputData property.
     */
    public List<InputDataType> getReferenceInputData() {
        if (referenceInputData == null) {
            referenceInputData = new ArrayList<>();
        }
        return this.referenceInputData;
    }

    /**
     * Gets the value of the signatureCheck property.
     * 
     * @return
     *     possible object is
     *     {@link ReferencesCheckResultType }
     *     
     */
    public ReferencesCheckResultType getSignatureCheck() {
        return signatureCheck;
    }

    /**
     * Sets the value of the signatureCheck property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReferencesCheckResultType }
     *     
     */
    public void setSignatureCheck(ReferencesCheckResultType value) {
        this.signatureCheck = value;
    }

    /**
     * Gets the value of the signatureManifestCheck property.
     * 
     * @return
     *     possible object is
     *     {@link ReferencesCheckResultType }
     *     
     */
    public ReferencesCheckResultType getSignatureManifestCheck() {
        return signatureManifestCheck;
    }

    /**
     * Sets the value of the signatureManifestCheck property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReferencesCheckResultType }
     *     
     */
    public void setSignatureManifestCheck(ReferencesCheckResultType value) {
        this.signatureManifestCheck = value;
    }

    /**
     * Gets the value of the xmldsigManifestCheck property.
     * 
     * <p>This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the xmldsigManifestCheck property.</p>
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * </p>
     * <pre>
     * getXMLDSIGManifestCheck().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ManifestRefsCheckResultType }
     * </p>
     * 
     * 
     * @return
     *     The value of the xmldsigManifestCheck property.
     */
    public List<ManifestRefsCheckResultType> getXMLDSIGManifestCheck() {
        if (xmldsigManifestCheck == null) {
            xmldsigManifestCheck = new ArrayList<>();
        }
        return this.xmldsigManifestCheck;
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

}
