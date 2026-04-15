
package org.w3._2000._09.xmldsig_;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DSAKeyValueType complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="DSAKeyValueType">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <sequence minOccurs="0">
 *           <element name="P" type="{http://www.w3.org/2000/09/xmldsig#}CryptoBinary"/>
 *           <element name="Q" type="{http://www.w3.org/2000/09/xmldsig#}CryptoBinary"/>
 *         </sequence>
 *         <element name="J" type="{http://www.w3.org/2000/09/xmldsig#}CryptoBinary" minOccurs="0"/>
 *         <element name="G" type="{http://www.w3.org/2000/09/xmldsig#}CryptoBinary" minOccurs="0"/>
 *         <element name="Y" type="{http://www.w3.org/2000/09/xmldsig#}CryptoBinary"/>
 *         <sequence minOccurs="0">
 *           <element name="Seed" type="{http://www.w3.org/2000/09/xmldsig#}CryptoBinary"/>
 *           <element name="PgenCounter" type="{http://www.w3.org/2000/09/xmldsig#}CryptoBinary"/>
 *         </sequence>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DSAKeyValueType", propOrder = {
    "p",
    "q",
    "j",
    "g",
    "y",
    "seed",
    "pgenCounter"
})
public class DSAKeyValueType {

    @XmlElement(name = "P")
    protected String p;
    @XmlElement(name = "Q")
    protected String q;
    @XmlElement(name = "J")
    protected String j;
    @XmlElement(name = "G")
    protected String g;
    @XmlElement(name = "Y", required = true)
    protected String y;
    @XmlElement(name = "Seed")
    protected String seed;
    @XmlElement(name = "PgenCounter")
    protected String pgenCounter;

    /**
     * Gets the value of the p property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getP() {
        return p;
    }

    /**
     * Sets the value of the p property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setP(String value) {
        this.p = value;
    }

    /**
     * Gets the value of the q property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQ() {
        return q;
    }

    /**
     * Sets the value of the q property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQ(String value) {
        this.q = value;
    }

    /**
     * Gets the value of the j property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJ() {
        return j;
    }

    /**
     * Sets the value of the j property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJ(String value) {
        this.j = value;
    }

    /**
     * Gets the value of the g property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getG() {
        return g;
    }

    /**
     * Sets the value of the g property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setG(String value) {
        this.g = value;
    }

    /**
     * Gets the value of the y property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getY() {
        return y;
    }

    /**
     * Sets the value of the y property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setY(String value) {
        this.y = value;
    }

    /**
     * Gets the value of the seed property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSeed() {
        return seed;
    }

    /**
     * Sets the value of the seed property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSeed(String value) {
        this.seed = value;
    }

    /**
     * Gets the value of the pgenCounter property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPgenCounter() {
        return pgenCounter;
    }

    /**
     * Sets the value of the pgenCounter property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPgenCounter(String value) {
        this.pgenCounter = value;
    }

}
