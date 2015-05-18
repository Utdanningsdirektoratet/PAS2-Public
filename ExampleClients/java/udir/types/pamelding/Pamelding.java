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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BrukernavnPAS" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="EksamensPeriodeKode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Orgnr" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Skolenavn" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Kandidatgrupper" type="{http://pas.udir.no/Pamelding}KandidatgrupperType"/>
 *         &lt;element name="Opplaeringsniva" type="{http://pas.udir.no/CommonTypes}OpplaeringsnivaType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "brukernavnPAS",
    "eksamensPeriodeKode",
    "orgnr",
    "skolenavn",
    "kandidatgrupper",
    "opplaeringsniva"
})
@XmlRootElement(name = "Pamelding")
public class Pamelding {

    @XmlElement(name = "BrukernavnPAS", required = true)
    protected String brukernavnPAS;
    @XmlElement(name = "EksamensPeriodeKode", required = true)
    protected String eksamensPeriodeKode;
    @XmlElement(name = "Orgnr", required = true)
    protected String orgnr;
    @XmlElement(name = "Skolenavn", required = true)
    protected String skolenavn;
    @XmlElement(name = "Kandidatgrupper", required = true, nillable = true)
    protected KandidatgrupperType kandidatgrupper;
    @XmlElement(name = "Opplaeringsniva", required = true)
    protected OpplaeringsnivaType opplaeringsniva;

    /**
     * Gets the value of the brukernavnPAS property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBrukernavnPAS() {
        return brukernavnPAS;
    }

    /**
     * Sets the value of the brukernavnPAS property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBrukernavnPAS(String value) {
        this.brukernavnPAS = value;
    }

    /**
     * Gets the value of the eksamensPeriodeKode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEksamensPeriodeKode() {
        return eksamensPeriodeKode;
    }

    /**
     * Sets the value of the eksamensPeriodeKode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEksamensPeriodeKode(String value) {
        this.eksamensPeriodeKode = value;
    }

    /**
     * Gets the value of the orgnr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrgnr() {
        return orgnr;
    }

    /**
     * Sets the value of the orgnr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrgnr(String value) {
        this.orgnr = value;
    }

    /**
     * Gets the value of the skolenavn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSkolenavn() {
        return skolenavn;
    }

    /**
     * Sets the value of the skolenavn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSkolenavn(String value) {
        this.skolenavn = value;
    }

    /**
     * Gets the value of the kandidatgrupper property.
     * 
     * @return
     *     possible object is
     *     {@link KandidatgrupperType }
     *     
     */
    public KandidatgrupperType getKandidatgrupper() {
        return kandidatgrupper;
    }

    /**
     * Sets the value of the kandidatgrupper property.
     * 
     * @param value
     *     allowed object is
     *     {@link KandidatgrupperType }
     *     
     */
    public void setKandidatgrupper(KandidatgrupperType value) {
        this.kandidatgrupper = value;
    }

    /**
     * Gets the value of the opplaeringsniva property.
     * 
     * @return
     *     possible object is
     *     {@link OpplaeringsnivaType }
     *     
     */
    public OpplaeringsnivaType getOpplaeringsniva() {
        return opplaeringsniva;
    }

    /**
     * Sets the value of the opplaeringsniva property.
     * 
     * @param value
     *     allowed object is
     *     {@link OpplaeringsnivaType }
     *     
     */
    public void setOpplaeringsniva(OpplaeringsnivaType value) {
        this.opplaeringsniva = value;
    }

}
