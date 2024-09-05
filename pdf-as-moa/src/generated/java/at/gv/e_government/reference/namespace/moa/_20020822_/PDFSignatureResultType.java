
package at.gv.e_government.reference.namespace.moa._20020822_;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import org.w3._2000._09.xmldsig_.KeyInfoType;


/**
 * <p>Java-Klasse für PDFSignatureResultType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="PDFSignatureResultType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="SignerInfo" type="{http://www.w3.org/2000/09/xmldsig#}KeyInfoType" minOccurs="0"/&gt;
 *         &lt;element name="SigningTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="SignatureAlgorithm" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="SignatureCheck" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}CheckResultType"/&gt;
 *         &lt;element name="CertificateCheck" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}CheckResultType"/&gt;
 *         &lt;element name="FormCheckResult" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}FormResultType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="ExtendedCertificateCheck" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}ExtendedCertificateCheckResultType" minOccurs="0"/&gt;
 *         &lt;element name="SignatureProperties" type="{http://reference.e-government.gv.at/namespace/moa/20020822#}PDFSignatureProperties" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
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
     * Ruft den Wert der signerInfo-Eigenschaft ab.
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
     * Legt den Wert der signerInfo-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link KeyInfoType }
     *     
     */
    public void setSignerInfo(KeyInfoType value) {
        this.signerInfo = value;
    }

    /**
     * Ruft den Wert der signingTime-Eigenschaft ab.
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
     * Legt den Wert der signingTime-Eigenschaft fest.
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
     * Ruft den Wert der signatureAlgorithm-Eigenschaft ab.
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
     * Legt den Wert der signatureAlgorithm-Eigenschaft fest.
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
     * Ruft den Wert der signatureCheck-Eigenschaft ab.
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
     * Legt den Wert der signatureCheck-Eigenschaft fest.
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
     * Ruft den Wert der certificateCheck-Eigenschaft ab.
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
     * Legt den Wert der certificateCheck-Eigenschaft fest.
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
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the formCheckResult property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFormCheckResult().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FormResultType }
     * 
     * 
     */
    public List<FormResultType> getFormCheckResult() {
        if (formCheckResult == null) {
            formCheckResult = new ArrayList<FormResultType>();
        }
        return this.formCheckResult;
    }

    /**
     * Ruft den Wert der extendedCertificateCheck-Eigenschaft ab.
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
     * Legt den Wert der extendedCertificateCheck-Eigenschaft fest.
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
     * Ruft den Wert der signatureProperties-Eigenschaft ab.
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
     * Legt den Wert der signatureProperties-Eigenschaft fest.
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
