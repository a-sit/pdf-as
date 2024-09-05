
package at.gv.e_government.reference.namespace.moa._20020822_;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für KeyStorageType.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <pre>
 * &lt;simpleType name="KeyStorageType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Software"/&gt;
 *     &lt;enumeration value="Hardware"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "KeyStorageType")
@XmlEnum
public enum KeyStorageType {

    @XmlEnumValue("Software")
    SOFTWARE("Software"),
    @XmlEnumValue("Hardware")
    HARDWARE("Hardware");
    private final String value;

    KeyStorageType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static KeyStorageType fromValue(String v) {
        for (KeyStorageType c: KeyStorageType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
