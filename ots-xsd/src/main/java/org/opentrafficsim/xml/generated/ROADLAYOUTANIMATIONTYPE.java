//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.08.11 at 02:51:13 PM CEST 
//


package org.opentrafficsim.xml.generated;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.opentrafficsim.xml.bindings.ColorAdapter;


/**
 * <p>Java class for ROADLAYOUTANIMATIONTYPE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ROADLAYOUTANIMATIONTYPE"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice maxOccurs="unbounded"&gt;
 *         &lt;element name="SHOULDER"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;attribute name="ID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *                 &lt;attribute name="COLOR" use="required" type="{http://www.opentrafficsim.org/ots}COLORTYPE" /&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="LANE"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;attribute name="ID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *                 &lt;attribute name="COLOR" use="required" type="{http://www.opentrafficsim.org/ots}COLORTYPE" /&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="STRIPE"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;attribute name="ID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *                 &lt;attribute name="COLOR" use="required" type="{http://www.opentrafficsim.org/ots}COLORTYPE" /&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="NOTRAFFICLANE"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;attribute name="ID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *                 &lt;attribute name="COLOR" use="required" type="{http://www.opentrafficsim.org/ots}COLORTYPE" /&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/choice&gt;
 *       &lt;attribute name="ID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ROADLAYOUTANIMATIONTYPE", propOrder = {
    "shoulderOrLANEOrSTRIPE"
})
@Generated(value = "com.sun.tools.xjc.Driver", date = "2020-08-11T02:51:12+02:00", comments = "JAXB RI v2.3.0")
public class ROADLAYOUTANIMATIONTYPE
    implements Serializable
{

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2020-08-11T02:51:12+02:00", comments = "JAXB RI v2.3.0")
    private final static long serialVersionUID = 10102L;
    @XmlElements({
        @XmlElement(name = "SHOULDER", type = ROADLAYOUTANIMATIONTYPE.SHOULDER.class),
        @XmlElement(name = "LANE", type = ROADLAYOUTANIMATIONTYPE.LANE.class),
        @XmlElement(name = "STRIPE", type = ROADLAYOUTANIMATIONTYPE.STRIPE.class),
        @XmlElement(name = "NOTRAFFICLANE", type = ROADLAYOUTANIMATIONTYPE.NOTRAFFICLANE.class)
    })
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2020-08-11T02:51:12+02:00", comments = "JAXB RI v2.3.0")
    protected List<Serializable> shoulderOrLANEOrSTRIPE;
    @XmlAttribute(name = "ID", required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2020-08-11T02:51:12+02:00", comments = "JAXB RI v2.3.0")
    protected String id;

    /**
     * Gets the value of the shoulderOrLANEOrSTRIPE property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the shoulderOrLANEOrSTRIPE property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSHOULDEROrLANEOrSTRIPE().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ROADLAYOUTANIMATIONTYPE.SHOULDER }
     * {@link ROADLAYOUTANIMATIONTYPE.LANE }
     * {@link ROADLAYOUTANIMATIONTYPE.STRIPE }
     * {@link ROADLAYOUTANIMATIONTYPE.NOTRAFFICLANE }
     * 
     * 
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2020-08-11T02:51:12+02:00", comments = "JAXB RI v2.3.0")
    public List<Serializable> getSHOULDEROrLANEOrSTRIPE() {
        if (shoulderOrLANEOrSTRIPE == null) {
            shoulderOrLANEOrSTRIPE = new ArrayList<Serializable>();
        }
        return this.shoulderOrLANEOrSTRIPE;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2020-08-11T02:51:12+02:00", comments = "JAXB RI v2.3.0")
    public String getID() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2020-08-11T02:51:12+02:00", comments = "JAXB RI v2.3.0")
    public void setID(String value) {
        this.id = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;attribute name="ID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *       &lt;attribute name="COLOR" use="required" type="{http://www.opentrafficsim.org/ots}COLORTYPE" /&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2020-08-11T02:51:12+02:00", comments = "JAXB RI v2.3.0")
    public static class LANE implements Serializable
    {

        @Generated(value = "com.sun.tools.xjc.Driver", date = "2020-08-11T02:51:12+02:00", comments = "JAXB RI v2.3.0")
        private final static long serialVersionUID = 10102L;
        @XmlAttribute(name = "ID", required = true)
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2020-08-11T02:51:12+02:00", comments = "JAXB RI v2.3.0")
        protected String id;
        @XmlAttribute(name = "COLOR", required = true)
        @XmlJavaTypeAdapter(ColorAdapter.class)
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2020-08-11T02:51:12+02:00", comments = "JAXB RI v2.3.0")
        protected Color color;

        /**
         * Gets the value of the id property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2020-08-11T02:51:12+02:00", comments = "JAXB RI v2.3.0")
        public String getID() {
            return id;
        }

        /**
         * Sets the value of the id property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2020-08-11T02:51:12+02:00", comments = "JAXB RI v2.3.0")
        public void setID(String value) {
            this.id = value;
        }

        /**
         * Gets the value of the color property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2020-08-11T02:51:12+02:00", comments = "JAXB RI v2.3.0")
        public Color getCOLOR() {
            return color;
        }

        /**
         * Sets the value of the color property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2020-08-11T02:51:12+02:00", comments = "JAXB RI v2.3.0")
        public void setCOLOR(Color value) {
            this.color = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;attribute name="ID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *       &lt;attribute name="COLOR" use="required" type="{http://www.opentrafficsim.org/ots}COLORTYPE" /&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2020-08-11T02:51:12+02:00", comments = "JAXB RI v2.3.0")
    public static class NOTRAFFICLANE implements Serializable
    {

        @Generated(value = "com.sun.tools.xjc.Driver", date = "2020-08-11T02:51:12+02:00", comments = "JAXB RI v2.3.0")
        private final static long serialVersionUID = 10102L;
        @XmlAttribute(name = "ID", required = true)
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2020-08-11T02:51:12+02:00", comments = "JAXB RI v2.3.0")
        protected String id;
        @XmlAttribute(name = "COLOR", required = true)
        @XmlJavaTypeAdapter(ColorAdapter.class)
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2020-08-11T02:51:12+02:00", comments = "JAXB RI v2.3.0")
        protected Color color;

        /**
         * Gets the value of the id property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2020-08-11T02:51:12+02:00", comments = "JAXB RI v2.3.0")
        public String getID() {
            return id;
        }

        /**
         * Sets the value of the id property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2020-08-11T02:51:12+02:00", comments = "JAXB RI v2.3.0")
        public void setID(String value) {
            this.id = value;
        }

        /**
         * Gets the value of the color property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2020-08-11T02:51:12+02:00", comments = "JAXB RI v2.3.0")
        public Color getCOLOR() {
            return color;
        }

        /**
         * Sets the value of the color property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2020-08-11T02:51:12+02:00", comments = "JAXB RI v2.3.0")
        public void setCOLOR(Color value) {
            this.color = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;attribute name="ID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *       &lt;attribute name="COLOR" use="required" type="{http://www.opentrafficsim.org/ots}COLORTYPE" /&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2020-08-11T02:51:12+02:00", comments = "JAXB RI v2.3.0")
    public static class SHOULDER implements Serializable
    {

        @Generated(value = "com.sun.tools.xjc.Driver", date = "2020-08-11T02:51:12+02:00", comments = "JAXB RI v2.3.0")
        private final static long serialVersionUID = 10102L;
        @XmlAttribute(name = "ID", required = true)
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2020-08-11T02:51:12+02:00", comments = "JAXB RI v2.3.0")
        protected String id;
        @XmlAttribute(name = "COLOR", required = true)
        @XmlJavaTypeAdapter(ColorAdapter.class)
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2020-08-11T02:51:12+02:00", comments = "JAXB RI v2.3.0")
        protected Color color;

        /**
         * Gets the value of the id property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2020-08-11T02:51:12+02:00", comments = "JAXB RI v2.3.0")
        public String getID() {
            return id;
        }

        /**
         * Sets the value of the id property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2020-08-11T02:51:12+02:00", comments = "JAXB RI v2.3.0")
        public void setID(String value) {
            this.id = value;
        }

        /**
         * Gets the value of the color property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2020-08-11T02:51:12+02:00", comments = "JAXB RI v2.3.0")
        public Color getCOLOR() {
            return color;
        }

        /**
         * Sets the value of the color property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2020-08-11T02:51:12+02:00", comments = "JAXB RI v2.3.0")
        public void setCOLOR(Color value) {
            this.color = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;attribute name="ID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *       &lt;attribute name="COLOR" use="required" type="{http://www.opentrafficsim.org/ots}COLORTYPE" /&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2020-08-11T02:51:12+02:00", comments = "JAXB RI v2.3.0")
    public static class STRIPE implements Serializable
    {

        @Generated(value = "com.sun.tools.xjc.Driver", date = "2020-08-11T02:51:12+02:00", comments = "JAXB RI v2.3.0")
        private final static long serialVersionUID = 10102L;
        @XmlAttribute(name = "ID", required = true)
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2020-08-11T02:51:12+02:00", comments = "JAXB RI v2.3.0")
        protected String id;
        @XmlAttribute(name = "COLOR", required = true)
        @XmlJavaTypeAdapter(ColorAdapter.class)
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2020-08-11T02:51:12+02:00", comments = "JAXB RI v2.3.0")
        protected Color color;

        /**
         * Gets the value of the id property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2020-08-11T02:51:12+02:00", comments = "JAXB RI v2.3.0")
        public String getID() {
            return id;
        }

        /**
         * Sets the value of the id property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2020-08-11T02:51:12+02:00", comments = "JAXB RI v2.3.0")
        public void setID(String value) {
            this.id = value;
        }

        /**
         * Gets the value of the color property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2020-08-11T02:51:12+02:00", comments = "JAXB RI v2.3.0")
        public Color getCOLOR() {
            return color;
        }

        /**
         * Sets the value of the color property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2020-08-11T02:51:12+02:00", comments = "JAXB RI v2.3.0")
        public void setCOLOR(Color value) {
            this.color = value;
        }

    }

}
