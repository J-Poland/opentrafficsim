//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.02.28 at 12:24:19 AM CET 
//


package org.opentrafficsim.xml.generated;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.djunits.value.vdouble.scalar.Length;
import org.djunits.value.vdouble.scalar.Speed;
import org.opentrafficsim.xml.bindings.LengthAdapter;
import org.opentrafficsim.xml.bindings.SpeedAdapter;


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
 *         &lt;element name="SPEEDLIMIT" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;attribute name="GTUTYPE" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *                 &lt;attribute name="LEGALSPEEDLIMIT" type="{http://www.opentrafficsim.org/ots}SPEEDTYPE" /&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;choice maxOccurs="unbounded"&gt;
 *           &lt;element name="LANE" type="{http://www.opentrafficsim.org/ots}CSELANE" maxOccurs="unbounded" minOccurs="0"/&gt;
 *           &lt;element name="NOTRAFFICLANE" type="{http://www.opentrafficsim.org/ots}CSENOTRAFFICLANE" maxOccurs="unbounded" minOccurs="0"/&gt;
 *           &lt;element name="SHOULDER" type="{http://www.opentrafficsim.org/ots}CSESHOULDER" maxOccurs="unbounded" minOccurs="0"/&gt;
 *           &lt;element name="STRIPE" type="{http://www.opentrafficsim.org/ots}CSESTRIPE" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;/choice&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="NAME" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="ROADTYPE" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="WIDTH" type="{http://www.opentrafficsim.org/ots}LENGTHTYPE" /&gt;
 *       &lt;attribute name="LANEKEEPING" type="{http://www.opentrafficsim.org/ots}LANEKEEPINGTYPE" /&gt;
 *       &lt;attribute name="OVERTAKING" type="{http://www.opentrafficsim.org/ots}OVERTAKINGTYPE" /&gt;
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
    "speedlimit",
    "laneOrNOTRAFFICLANEOrSHOULDER"
})
@XmlRootElement(name = "ROADLAYOUT")
@Generated(value = "com.sun.tools.xjc.Driver", date = "2019-02-28T12:24:19+01:00", comments = "JAXB RI v2.3.0")
public class ROADLAYOUT {

    @XmlElement(name = "SPEEDLIMIT")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-02-28T12:24:19+01:00", comments = "JAXB RI v2.3.0")
    protected List<ROADLAYOUT.SPEEDLIMIT> speedlimit;
    @XmlElements({
        @XmlElement(name = "LANE", type = CSELANE.class),
        @XmlElement(name = "NOTRAFFICLANE", type = CSENOTRAFFICLANE.class),
        @XmlElement(name = "SHOULDER", type = CSESHOULDER.class),
        @XmlElement(name = "STRIPE", type = CSESTRIPE.class)
    })
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-02-28T12:24:19+01:00", comments = "JAXB RI v2.3.0")
    protected List<CROSSSECTIONELEMENT> laneOrNOTRAFFICLANEOrSHOULDER;
    @XmlAttribute(name = "NAME", required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-02-28T12:24:19+01:00", comments = "JAXB RI v2.3.0")
    protected String name;
    @XmlAttribute(name = "ROADTYPE", required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-02-28T12:24:19+01:00", comments = "JAXB RI v2.3.0")
    protected String roadtype;
    @XmlAttribute(name = "WIDTH")
    @XmlJavaTypeAdapter(LengthAdapter.class)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-02-28T12:24:19+01:00", comments = "JAXB RI v2.3.0")
    protected Length width;
    @XmlAttribute(name = "LANEKEEPING")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-02-28T12:24:19+01:00", comments = "JAXB RI v2.3.0")
    protected String lanekeeping;
    @XmlAttribute(name = "OVERTAKING")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-02-28T12:24:19+01:00", comments = "JAXB RI v2.3.0")
    protected String overtaking;
    @XmlAttribute(name = "base", namespace = "http://www.w3.org/XML/1998/namespace")
    @XmlSchemaType(name = "anyURI")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-02-28T12:24:19+01:00", comments = "JAXB RI v2.3.0")
    protected String base;

    /**
     * Gets the value of the speedlimit property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the speedlimit property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSPEEDLIMIT().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ROADLAYOUT.SPEEDLIMIT }
     * 
     * 
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-02-28T12:24:19+01:00", comments = "JAXB RI v2.3.0")
    public List<ROADLAYOUT.SPEEDLIMIT> getSPEEDLIMIT() {
        if (speedlimit == null) {
            speedlimit = new ArrayList<ROADLAYOUT.SPEEDLIMIT>();
        }
        return this.speedlimit;
    }

    /**
     * Gets the value of the laneOrNOTRAFFICLANEOrSHOULDER property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the laneOrNOTRAFFICLANEOrSHOULDER property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLANEOrNOTRAFFICLANEOrSHOULDER().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CSELANE }
     * {@link CSENOTRAFFICLANE }
     * {@link CSESHOULDER }
     * {@link CSESTRIPE }
     * 
     * 
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-02-28T12:24:19+01:00", comments = "JAXB RI v2.3.0")
    public List<CROSSSECTIONELEMENT> getLANEOrNOTRAFFICLANEOrSHOULDER() {
        if (laneOrNOTRAFFICLANEOrSHOULDER == null) {
            laneOrNOTRAFFICLANEOrSHOULDER = new ArrayList<CROSSSECTIONELEMENT>();
        }
        return this.laneOrNOTRAFFICLANEOrSHOULDER;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-02-28T12:24:19+01:00", comments = "JAXB RI v2.3.0")
    public String getNAME() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-02-28T12:24:19+01:00", comments = "JAXB RI v2.3.0")
    public void setNAME(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the roadtype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-02-28T12:24:19+01:00", comments = "JAXB RI v2.3.0")
    public String getROADTYPE() {
        return roadtype;
    }

    /**
     * Sets the value of the roadtype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-02-28T12:24:19+01:00", comments = "JAXB RI v2.3.0")
    public void setROADTYPE(String value) {
        this.roadtype = value;
    }

    /**
     * Gets the value of the width property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-02-28T12:24:19+01:00", comments = "JAXB RI v2.3.0")
    public Length getWIDTH() {
        return width;
    }

    /**
     * Sets the value of the width property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-02-28T12:24:19+01:00", comments = "JAXB RI v2.3.0")
    public void setWIDTH(Length value) {
        this.width = value;
    }

    /**
     * Gets the value of the lanekeeping property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-02-28T12:24:19+01:00", comments = "JAXB RI v2.3.0")
    public String getLANEKEEPING() {
        return lanekeeping;
    }

    /**
     * Sets the value of the lanekeeping property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-02-28T12:24:19+01:00", comments = "JAXB RI v2.3.0")
    public void setLANEKEEPING(String value) {
        this.lanekeeping = value;
    }

    /**
     * Gets the value of the overtaking property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-02-28T12:24:19+01:00", comments = "JAXB RI v2.3.0")
    public String getOVERTAKING() {
        return overtaking;
    }

    /**
     * Sets the value of the overtaking property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-02-28T12:24:19+01:00", comments = "JAXB RI v2.3.0")
    public void setOVERTAKING(String value) {
        this.overtaking = value;
    }

    /**
     * Gets the value of the base property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-02-28T12:24:19+01:00", comments = "JAXB RI v2.3.0")
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
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-02-28T12:24:19+01:00", comments = "JAXB RI v2.3.0")
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
     *       &lt;attribute name="GTUTYPE" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *       &lt;attribute name="LEGALSPEEDLIMIT" type="{http://www.opentrafficsim.org/ots}SPEEDTYPE" /&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-02-28T12:24:19+01:00", comments = "JAXB RI v2.3.0")
    public static class SPEEDLIMIT {

        @XmlAttribute(name = "GTUTYPE", required = true)
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-02-28T12:24:19+01:00", comments = "JAXB RI v2.3.0")
        protected String gtutype;
        @XmlAttribute(name = "LEGALSPEEDLIMIT")
        @XmlJavaTypeAdapter(SpeedAdapter.class)
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-02-28T12:24:19+01:00", comments = "JAXB RI v2.3.0")
        protected Speed legalspeedlimit;

        /**
         * Gets the value of the gtutype property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-02-28T12:24:19+01:00", comments = "JAXB RI v2.3.0")
        public String getGTUTYPE() {
            return gtutype;
        }

        /**
         * Sets the value of the gtutype property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-02-28T12:24:19+01:00", comments = "JAXB RI v2.3.0")
        public void setGTUTYPE(String value) {
            this.gtutype = value;
        }

        /**
         * Gets the value of the legalspeedlimit property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-02-28T12:24:19+01:00", comments = "JAXB RI v2.3.0")
        public Speed getLEGALSPEEDLIMIT() {
            return legalspeedlimit;
        }

        /**
         * Sets the value of the legalspeedlimit property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2019-02-28T12:24:19+01:00", comments = "JAXB RI v2.3.0")
        public void setLEGALSPEEDLIMIT(Speed value) {
            this.legalspeedlimit = value;
        }

    }

}