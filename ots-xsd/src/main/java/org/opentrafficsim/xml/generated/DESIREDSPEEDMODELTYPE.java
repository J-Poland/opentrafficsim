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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.opentrafficsim.xml.bindings.ClassNameAdapter;


/**
 * <p>Java class for DESIREDSPEEDMODELTYPE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DESIREDSPEEDMODELTYPE"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice&gt;
 *         &lt;element name="IDM" type="{http://www.w3.org/2001/XMLSchema}anyType"/&gt;
 *         &lt;element name="SOCIO" type="{http://www.opentrafficsim.org/ots}DESIREDSPEEDMODELTYPE"/&gt;
 *         &lt;element name="CLASS" type="{http://www.opentrafficsim.org/ots}CLASSNAMETYPE"/&gt;
 *       &lt;/choice&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DESIREDSPEEDMODELTYPE", propOrder = {
    "idm",
    "socio",
    "_class"
})
@Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-24T11:10:08+01:00", comments = "JAXB RI v2.3.0")
public class DESIREDSPEEDMODELTYPE
    implements Serializable
{

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-24T11:10:08+01:00", comments = "JAXB RI v2.3.0")
    private final static long serialVersionUID = 10102L;
    @XmlElement(name = "IDM")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-24T11:10:08+01:00", comments = "JAXB RI v2.3.0")
    protected Object idm;
    @XmlElement(name = "SOCIO")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-24T11:10:08+01:00", comments = "JAXB RI v2.3.0")
    protected DESIREDSPEEDMODELTYPE socio;
    @XmlElement(name = "CLASS", type = String.class)
    @XmlJavaTypeAdapter(ClassNameAdapter.class)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-24T11:10:08+01:00", comments = "JAXB RI v2.3.0")
    protected Class _class;

    /**
     * Gets the value of the idm property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-24T11:10:08+01:00", comments = "JAXB RI v2.3.0")
    public Object getIDM() {
        return idm;
    }

    /**
     * Sets the value of the idm property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-24T11:10:08+01:00", comments = "JAXB RI v2.3.0")
    public void setIDM(Object value) {
        this.idm = value;
    }

    /**
     * Gets the value of the socio property.
     * 
     * @return
     *     possible object is
     *     {@link DESIREDSPEEDMODELTYPE }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-24T11:10:08+01:00", comments = "JAXB RI v2.3.0")
    public DESIREDSPEEDMODELTYPE getSOCIO() {
        return socio;
    }

    /**
     * Sets the value of the socio property.
     * 
     * @param value
     *     allowed object is
     *     {@link DESIREDSPEEDMODELTYPE }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-24T11:10:08+01:00", comments = "JAXB RI v2.3.0")
    public void setSOCIO(DESIREDSPEEDMODELTYPE value) {
        this.socio = value;
    }

    /**
     * Gets the value of the class property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-24T11:10:08+01:00", comments = "JAXB RI v2.3.0")
    public Class getCLASS() {
        return _class;
    }

    /**
     * Sets the value of the class property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2022-02-24T11:10:08+01:00", comments = "JAXB RI v2.3.0")
    public void setCLASS(Class value) {
        this._class = value;
    }

}
