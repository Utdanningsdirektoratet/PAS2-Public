//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.05.18 at 04:16:55 PM CEST 
//


package udir.types.eksamensplan;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EksamenType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EksamenType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Fagkode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Eksamensdeler" type="{http://pas.udir.no/Eksamensplan}EksamensdelerType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EksamenType", propOrder = {
    "fagkode",
    "eksamensdeler"
})
public class EksamenType {

    @XmlElement(name = "Fagkode", required = true)
    protected String fagkode;
    @XmlElement(name = "Eksamensdeler", required = true)
    protected EksamensdelerType eksamensdeler;

    /**
     * Gets the value of the fagkode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFagkode() {
        return fagkode;
    }

    /**
     * Sets the value of the fagkode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFagkode(String value) {
        this.fagkode = value;
    }

    /**
     * Gets the value of the eksamensdeler property.
     * 
     * @return
     *     possible object is
     *     {@link EksamensdelerType }
     *     
     */
    public EksamensdelerType getEksamensdeler() {
        return eksamensdeler;
    }

    /**
     * Sets the value of the eksamensdeler property.
     * 
     * @param value
     *     allowed object is
     *     {@link EksamensdelerType }
     *     
     */
    public void setEksamensdeler(EksamensdelerType value) {
        this.eksamensdeler = value;
    }

}
