//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.05.18 at 04:16:59 PM CEST 
//


package udir.types.pamelding;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PameldtKandidatgruppeType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PameldtKandidatgruppeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Navn" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Kandidatgruppeansvarlige" type="{http://pas.udir.no/CommonTypes}KandidatgruppeansvarligeType"/>
 *         &lt;element name="Kandidater" type="{http://pas.udir.no/Pamelding}PameldteKandidaterType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PameldtKandidatgruppeType", propOrder = {
    "navn",
    "kandidatgruppeansvarlige",
    "kandidater"
})
public class PameldtKandidatgruppeType {

    @XmlElement(name = "Navn", namespace = "", required = true)
    protected String navn;
    @XmlElement(name = "Kandidatgruppeansvarlige", namespace = "", required = true)
    protected KandidatgruppeansvarligeType kandidatgruppeansvarlige;
    @XmlElement(name = "Kandidater", namespace = "", required = true)
    protected PameldteKandidaterType kandidater;

    /**
     * Gets the value of the navn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNavn() {
        return navn;
    }

    /**
     * Sets the value of the navn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNavn(String value) {
        this.navn = value;
    }

    /**
     * Gets the value of the kandidatgruppeansvarlige property.
     * 
     * @return
     *     possible object is
     *     {@link KandidatgruppeansvarligeType }
     *     
     */
    public KandidatgruppeansvarligeType getKandidatgruppeansvarlige() {
        return kandidatgruppeansvarlige;
    }

    /**
     * Sets the value of the kandidatgruppeansvarlige property.
     * 
     * @param value
     *     allowed object is
     *     {@link KandidatgruppeansvarligeType }
     *     
     */
    public void setKandidatgruppeansvarlige(KandidatgruppeansvarligeType value) {
        this.kandidatgruppeansvarlige = value;
    }

    /**
     * Gets the value of the kandidater property.
     * 
     * @return
     *     possible object is
     *     {@link PameldteKandidaterType }
     *     
     */
    public PameldteKandidaterType getKandidater() {
        return kandidater;
    }

    /**
     * Sets the value of the kandidater property.
     * 
     * @param value
     *     allowed object is
     *     {@link PameldteKandidaterType }
     *     
     */
    public void setKandidater(PameldteKandidaterType value) {
        this.kandidater = value;
    }

}
