
package io.naztech.nuxeoclient.model.invoice.festool;

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
 *         &lt;element ref="{http://www.abbyy.com/FlexiCapture/Schemas/Export/Festool_Document_Definition.xsd}_Festool_Document_Definition"/>
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
    "festoolDocumentDefinition"
})
@XmlRootElement(name = "Documents")
public class FestoolInvoiceDocument {

    @XmlElement(name = "_Festool_Document_Definition", namespace = "http://www.abbyy.com/FlexiCapture/Schemas/Export/Festool_Document_Definition.xsd", required = true)
    protected FestoolDocumentDefinition festoolDocumentDefinition;

    /**
     * Gets the value of the festoolDocumentDefinition property.
     * 
     * @return
     *     possible object is
     *     {@link FestoolDocumentDefinition }
     *     
     */
    public FestoolDocumentDefinition getFestoolDocumentDefinition() {
        return festoolDocumentDefinition;
    }

    /**
     * Sets the value of the festoolDocumentDefinition property.
     * 
     * @param value
     *     allowed object is
     *     {@link FestoolDocumentDefinition }
     *     
     */
    public void setFestoolDocumentDefinition(FestoolDocumentDefinition value) {
        this.festoolDocumentDefinition = value;
    }

}
