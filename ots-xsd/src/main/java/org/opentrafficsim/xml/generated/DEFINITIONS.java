//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.02.26 at 04:02:02 PM CET 
//


package org.opentrafficsim.xml.generated;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.opentrafficsim.org/ots}GLOBAL" minOccurs="0"/&gt;
 *         &lt;choice maxOccurs="unbounded"&gt;
 *           &lt;element ref="{http://www.w3.org/2001/XInclude}include" maxOccurs="unbounded" minOccurs="0"/&gt;
 *           &lt;element ref="{http://www.opentrafficsim.org/ots}GTUTYPE" maxOccurs="unbounded" minOccurs="0"/&gt;
 *           &lt;element ref="{http://www.opentrafficsim.org/ots}GTU" maxOccurs="unbounded" minOccurs="0"/&gt;
 *           &lt;element ref="{http://www.opentrafficsim.org/ots}GTUMIX" maxOccurs="unbounded" minOccurs="0"/&gt;
 *           &lt;element ref="{http://www.opentrafficsim.org/ots}ROADTYPE" maxOccurs="unbounded" minOccurs="0"/&gt;
 *           &lt;element ref="{http://www.opentrafficsim.org/ots}LANETYPE" maxOccurs="unbounded" minOccurs="0"/&gt;
 *           &lt;element ref="{http://www.opentrafficsim.org/ots}ROADLAYOUT" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;/choice&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute ref="{http://www.w3.org/XML/1998/namespace}base"/&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "content"
})
@XmlRootElement(name = "DEFINITIONS")
@Generated(value = "com.sun.tools.xjc.Driver", date = "2019-02-26T04:02:02+01:00", comments = "JAXB RI v2.3.0")
public class DEFINITIONS {

    @XmlElementRefs({
        @XmlElementRef(name = "GLOBAL", namespace = "http://www.opentrafficsim.org/ots", type = GLOBAL.class, required = false),
        @XmlElementRef(name = "include", namespace = "http://www.w3.org/2001/XInclude", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "GTUTYPE", namespace = "http://www.opentrafficsim.org/ots", type = GTUTYPE.class, required = false),
        @XmlElementRef(name = "GTU", namespace = "http://www.opentrafficsim.org/ots", type = GTU.class, required = false),
        @XmlElementRef(name = "GTUMIX", namespace = "http://www.opentrafficsim.org/ots", type = GTUMIX.class, required = false),
        @XmlElementRef(name = "ROADTYPE", namespace = "http://www.opentrafficsim.org/ots", type = ROADTYPE.class, required = false),
        @XmlElementRef(name = "LANETYPE", namespace = "http://www.opentrafficsim.org/ots", type = LANETYPE.class, required = false),
        @XmlElementRef(name = "ROADLAYOUT", namespace = "http://www.opentrafficsim.org/ots", type = ROADLAYOUT.class, required = false)
    })
    @XmlMixed
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-02-26T04:02:02+01:00", comments = "JAXB RI v2.3.0")
    protected List<Object> content;
    @XmlAttribute(name = "base", namespace = "http://www.w3.org/XML/1998/namespace")
    @XmlSchemaType(name = "anyURI")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-02-26T04:02:02+01:00", comments = "JAXB RI v2.3.0")
    protected String base;

    /**
     * Gets the value of the content property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the content property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GLOBAL }
     * {@link JAXBElement }{@code <}{@link IncludeType }{@code >}
     * {@link GTUTYPE }
     * {@link GTU }
     * {@link GTUMIX }
     * {@link ROADTYPE }
     * {@link LANETYPE }
     * {@link ROADLAYOUT }
     * {@link String }
     * 
     * 
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-02-26T04:02:02+01:00", comments = "JAXB RI v2.3.0")
    public List<Object> getContent() {
        if (content == null) {
            content = new ArrayList<Object>();
        }
        return this.content;
    }

    /**
     * Gets the value of the base property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-02-26T04:02:02+01:00", comments = "JAXB RI v2.3.0")
    public String getBase() {
        return base;
    }

    /**
     * Sets the value of the base property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-02-26T04:02:02+01:00", comments = "JAXB RI v2.3.0")
    public void setBase(String value) {
        this.base = value;
    }

}
