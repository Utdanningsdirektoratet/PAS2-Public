//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.05.18 at 04:16:59 PM CEST 
//


package udir.types.pamelding;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for KandidatgruppeReportsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="KandidatgruppeReportsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Kandidatgruppe" type="{http://pas.udir.no/Pamelding}KandidatgruppeReportType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "KandidatgruppeReportsType", propOrder = {
    "kandidatgruppe"
})
public class KandidatgruppeReportsType {

    @XmlElement(name = "Kandidatgruppe")
    protected List<KandidatgruppeReportType> kandidatgruppe;

    /**
     * Gets the value of the kandidatgruppe property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the kandidatgruppe property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getKandidatgruppe().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link KandidatgruppeReportType }
     * 
     * 
     */
    public List<KandidatgruppeReportType> getKandidatgruppe() {
        if (kandidatgruppe == null) {
            kandidatgruppe = new ArrayList<KandidatgruppeReportType>();
        }
        return this.kandidatgruppe;
    }

}
