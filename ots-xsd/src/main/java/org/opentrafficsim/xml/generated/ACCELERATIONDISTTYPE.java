//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2022.02.24 at 11:10:08 PM CET 
//


package org.opentrafficsim.xml.generated;

import java.io.Serializable;
import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ACCELERATIONDISTTYPE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ACCELERATIONDISTTYPE"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.opentrafficsim.org/ots}CONTDISTTYPE"&gt;
 *       &lt;attribute name="ACCELERATIONUNIT" type="{http://www.opentrafficsim.org/ots}ACCELERATIONUNITTYPE" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ACCELERATIONDISTTYPE")
@XmlSeeAlso({
    PARAMETERACCELERATIONDIST.class
})
@Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-24T11:10:08+01:00", comments = "JAXB RI v2.3.0")
public class ACCELERATIONDISTTYPE
    extends CONTDISTTYPE
    implements Serializable
{

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-24T11:10:08+01:00", comments = "JAXB RI v2.3.0")
    private final static long serialVersionUID = 10102L;
    @XmlAttribute(name = "ACCELERATIONUNIT")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-24T11:10:08+01:00", comments = "JAXB RI v2.3.0")
    protected String accelerationunit;

    /**
     * Gets the value of the accelerationunit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-24T11:10:08+01:00", comments = "JAXB RI v2.3.0")
    public String getACCELERATIONUNIT() {
        return accelerationunit;
    }

    /**
     * Sets the value of the accelerationunit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-24T11:10:08+01:00", comments = "JAXB RI v2.3.0")
    public void setACCELERATIONUNIT(String value) {
        this.accelerationunit = value;
    }

}
