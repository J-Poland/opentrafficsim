//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.08.11 at 02:51:13 PM CEST 
//


package org.opentrafficsim.xml.generated;

import java.io.Serializable;
import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for POSITIONDISTTYPE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="POSITIONDISTTYPE"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.opentrafficsim.org/ots}CONTDISTTYPE"&gt;
 *       &lt;attribute name="POSITIONUNIT" type="{http://www.opentrafficsim.org/ots}POSITIONUNITTYPE" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "POSITIONDISTTYPE")
@Generated(value = "com.sun.tools.xjc.Driver", date = "2020-08-11T02:51:12+02:00", comments = "JAXB RI v2.3.0")
public class POSITIONDISTTYPE
    extends CONTDISTTYPE
    implements Serializable
{

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2020-08-11T02:51:12+02:00", comments = "JAXB RI v2.3.0")
    private final static long serialVersionUID = 10102L;
    @XmlAttribute(name = "POSITIONUNIT")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2020-08-11T02:51:12+02:00", comments = "JAXB RI v2.3.0")
    protected String positionunit;

    /**
     * Gets the value of the positionunit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2020-08-11T02:51:12+02:00", comments = "JAXB RI v2.3.0")
    public String getPOSITIONUNIT() {
        return positionunit;
    }

    /**
     * Sets the value of the positionunit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2020-08-11T02:51:12+02:00", comments = "JAXB RI v2.3.0")
    public void setPOSITIONUNIT(String value) {
        this.positionunit = value;
    }

}
