
package io.naztech.nuxeoclient.model.invoice.owlett;

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
 *         &lt;element ref="{http://www.abbyy.com/FlexiCapture/Schemas/Export/Owlett_Document_Definition.xsd}_Owlett_Document_Definition"/>
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
    "owlettDocumentDefinition"
})
@XmlRootElement(name = "Documents")
public class OwlettInvoiceDocument {

    @XmlElement(name = "_Owlett_Document_Definition", namespace = "http://www.abbyy.com/FlexiCapture/Schemas/Export/Owlett_Document_Definition.xsd", required = true)
    protected OwlettDocumentDefinition owlettDocumentDefinition;

    /**
     * Gets the value of the owlettDocumentDefinition property.
     * 
     * @return
     *     possible object is
     *     {@link OwlettDocumentDefinition }
     *     
     */
    public OwlettDocumentDefinition getOwlettDocumentDefinition() {
        return owlettDocumentDefinition;
    }

    /**
     * Sets the value of the owlettDocumentDefinition property.
     * 
     * @param value
     *     allowed object is
     *     {@link OwlettDocumentDefinition }
     *     
     */
    public void setOwlettDocumentDefinition(OwlettDocumentDefinition value) {
        this.owlettDocumentDefinition = value;
    }

}
