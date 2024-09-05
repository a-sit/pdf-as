
package at.gv.e_government.reference.namespace.moa._20020822_;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für AllSignatoriesType.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <pre>
 * &lt;simpleType name="AllSignatoriesType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="all"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
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

    public String value() {
        return value;
    }

    public static AllSignatoriesType fromValue(String v) {
        for (AllSignatoriesType c: AllSignatoriesType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
