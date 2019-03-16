//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.03.16 at 05:48:13 PM CET 
//


package org.opentrafficsim.xml.generated;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.opentrafficsim.xml.bindings.DurationAdapter;
import org.opentrafficsim.xml.bindings.TimeAdapter;


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
 *         &lt;element name="SEED" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;attribute name="STREAMNAME" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *                 &lt;attribute name="SEEEDVALUE" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" /&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="STARTTIME" type="{http://www.opentrafficsim.org/ots}TIMETYPE" default="0s" /&gt;
 *       &lt;attribute name="WARMUPPERIOD" type="{http://www.opentrafficsim.org/ots}DURATIONTYPE" default="0s" /&gt;
 *       &lt;attribute name="RUNLENGTH" use="required" type="{http://www.opentrafficsim.org/ots}DURATIONTYPE" /&gt;
 *       &lt;attribute name="NUMBERREPLICATIONS" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" default="1" /&gt;
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
    "seed"
})
@XmlRootElement(name = "RUN")
@Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-16T05:48:13+01:00", comments = "JAXB RI v2.3.0")
public class RUN {

    @XmlElement(name = "SEED")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-16T05:48:13+01:00", comments = "JAXB RI v2.3.0")
    protected List<RUN.SEED> seed;
    @XmlAttribute(name = "STARTTIME")
    @XmlJavaTypeAdapter(TimeAdapter.class)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-16T05:48:13+01:00", comments = "JAXB RI v2.3.0")
    protected Time starttime;
    @XmlAttribute(name = "WARMUPPERIOD")
    @XmlJavaTypeAdapter(DurationAdapter.class)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-16T05:48:13+01:00", comments = "JAXB RI v2.3.0")
    protected Duration warmupperiod;
    @XmlAttribute(name = "RUNLENGTH", required = true)
    @XmlJavaTypeAdapter(DurationAdapter.class)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-16T05:48:13+01:00", comments = "JAXB RI v2.3.0")
    protected Duration runlength;
    @XmlAttribute(name = "NUMBERREPLICATIONS")
    @XmlSchemaType(name = "unsignedInt")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-16T05:48:13+01:00", comments = "JAXB RI v2.3.0")
    protected Long numberreplications;
    @XmlAttribute(name = "base", namespace = "http://www.w3.org/XML/1998/namespace")
    @XmlSchemaType(name = "anyURI")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-16T05:48:13+01:00", comments = "JAXB RI v2.3.0")
    protected String base;

    /**
     * Gets the value of the seed property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the seed property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSEED().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RUN.SEED }
     * 
     * 
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-16T05:48:13+01:00", comments = "JAXB RI v2.3.0")
    public List<RUN.SEED> getSEED() {
        if (seed == null) {
            seed = new ArrayList<RUN.SEED>();
        }
        return this.seed;
    }

    /**
     * Gets the value of the starttime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-16T05:48:13+01:00", comments = "JAXB RI v2.3.0")
    public Time getSTARTTIME() {
        if (starttime == null) {
            return new TimeAdapter().unmarshal("0s");
        } else {
            return starttime;
        }
    }

    /**
     * Sets the value of the starttime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-16T05:48:13+01:00", comments = "JAXB RI v2.3.0")
    public void setSTARTTIME(Time value) {
        this.starttime = value;
    }

    /**
     * Gets the value of the warmupperiod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-16T05:48:13+01:00", comments = "JAXB RI v2.3.0")
    public Duration getWARMUPPERIOD() {
        if (warmupperiod == null) {
            return new DurationAdapter().unmarshal("0s");
        } else {
            return warmupperiod;
        }
    }

    /**
     * Sets the value of the warmupperiod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-16T05:48:13+01:00", comments = "JAXB RI v2.3.0")
    public void setWARMUPPERIOD(Duration value) {
        this.warmupperiod = value;
    }

    /**
     * Gets the value of the runlength property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-16T05:48:13+01:00", comments = "JAXB RI v2.3.0")
    public Duration getRUNLENGTH() {
        return runlength;
    }

    /**
     * Sets the value of the runlength property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-16T05:48:13+01:00", comments = "JAXB RI v2.3.0")
    public void setRUNLENGTH(Duration value) {
        this.runlength = value;
    }

    /**
     * Gets the value of the numberreplications property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-16T05:48:13+01:00", comments = "JAXB RI v2.3.0")
    public long getNUMBERREPLICATIONS() {
        if (numberreplications == null) {
            return  1L;
        } else {
            return numberreplications;
        }
    }

    /**
     * Sets the value of the numberreplications property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-16T05:48:13+01:00", comments = "JAXB RI v2.3.0")
    public void setNUMBERREPLICATIONS(Long value) {
        this.numberreplications = value;
    }

    /**
     * Gets the value of the base property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-16T05:48:13+01:00", comments = "JAXB RI v2.3.0")
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
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-16T05:48:13+01:00", comments = "JAXB RI v2.3.0")
    public void setBase(String value) {
        this.base = value;
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
     *       &lt;attribute name="STREAMNAME" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *       &lt;attribute name="SEEEDVALUE" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" /&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-16T05:48:13+01:00", comments = "JAXB RI v2.3.0")
    public static class SEED {

        @XmlAttribute(name = "STREAMNAME", required = true)
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-16T05:48:13+01:00", comments = "JAXB RI v2.3.0")
        protected String streamname;
        @XmlAttribute(name = "SEEEDVALUE", required = true)
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-16T05:48:13+01:00", comments = "JAXB RI v2.3.0")
        protected BigInteger seeedvalue;

        /**
         * Gets the value of the streamname property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-16T05:48:13+01:00", comments = "JAXB RI v2.3.0")
        public String getSTREAMNAME() {
            return streamname;
        }

        /**
         * Sets the value of the streamname property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-16T05:48:13+01:00", comments = "JAXB RI v2.3.0")
        public void setSTREAMNAME(String value) {
            this.streamname = value;
        }

        /**
         * Gets the value of the seeedvalue property.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-16T05:48:13+01:00", comments = "JAXB RI v2.3.0")
        public BigInteger getSEEEDVALUE() {
            return seeedvalue;
        }

        /**
         * Sets the value of the seeedvalue property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-03-16T05:48:13+01:00", comments = "JAXB RI v2.3.0")
        public void setSEEEDVALUE(BigInteger value) {
            this.seeedvalue = value;
        }

    }

}
