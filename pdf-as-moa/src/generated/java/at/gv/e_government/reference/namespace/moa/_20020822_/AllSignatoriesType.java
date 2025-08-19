
package at.gv.e_government.reference.namespace.moa._20020822_;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * 
 * 
 * <p>Java class for AllSignatoriesType</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * <pre>{@code
 * <simpleType name="AllSignatoriesType">
 *   <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     <enumeration value="all"/>
 *   </restriction>
 * </simpleType>
 * }</pre>
 * 
 */
@XmlType(name = "AllSignatoriesType")
@XmlEnum
public enum AllSignatoriesType {

    @XmlEnumValue("all")
    ALL("all");
    private final String value;

    AllSignatoriesType(String v) {
        value = v;
    }

    /**
     * Gets the value associated to the enum constant.
     * 
     * @return
     *     The value linked to the enum.
     */
    public String value() {
        return value;
    }

    /**
     * Gets the enum associated to the value passed as parameter.
     * 
     * @param v
     *     The value to get the enum from.
     * @return
     *     The enum which corresponds to the value, if it exists.
     * @throws IllegalArgumentException
     *     If no value matches in the enum declaration.
     */
    public static AllSignatoriesType fromValue(String v) {
        for (AllSignatoriesType c: AllSignatoriesType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
