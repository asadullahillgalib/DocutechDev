
package io.naztech.nuxeoclient.model.invoice.owlett;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
 *         &lt;element ref="{}_InvoiceNo"/>
 *         &lt;element ref="{}_Address"/>
 *         &lt;element ref="{}_DateOfInvoice"/>
 *         &lt;element ref="{}_TaxPointDate"/>
 *         &lt;element ref="{}_CustomerOrderNo"/>
 *         &lt;element ref="{}_DespatchDate"/>
 *         &lt;element ref="{}_CustomerACNo"/>
 *         &lt;element ref="{}_InvoiceTotal"/>
 *         &lt;element ref="{}_Table"/>
 *       &lt;/sequence>
 *       &lt;attribute ref="{http://www.abbyy.com/FlexiCapture/Schemas/Export/AdditionalFormData.xsd}ImagePath use="required""/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "invoiceNo",
    "address",
    "dateOfInvoice",
    "taxPointDate",
    "customerOrderNo",
    "despatchDate",
    "customerACNo",
    "invoiceTotal",
    "table"
})
@XmlRootElement(name = "_Owlett_Document_Definition", namespace = "http://www.abbyy.com/FlexiCapture/Schemas/Export/Owlett_Document_Definition.xsd")
public class OwlettDocumentDefinition {

    @XmlElement(name = "_InvoiceNo", required = true)
    protected String invoiceNo;
    @XmlElement(name = "_Address", required = true)
    protected String address;
    @XmlElement(name = "_DateOfInvoice", required = true)
    protected String dateOfInvoice;
    @XmlElement(name = "_TaxPointDate", required = true)
    protected String taxPointDate;
    @XmlElement(name = "_CustomerOrderNo", required = true)
    protected String customerOrderNo;
    @XmlElement(name = "_DespatchDate", required = true)
    protected String despatchDate;
    @XmlElement(name = "_CustomerACNo", required = true)
    protected String customerACNo;
    @XmlElement(name = "_InvoiceTotal", required = true)
    protected BigDecimal invoiceTotal;
    @XmlElement(name = "_Table", required = true)
    protected Table table;
    @XmlAttribute(name = "ImagePath", namespace = "http://www.abbyy.com/FlexiCapture/Schemas/Export/AdditionalFormData.xsd", required = true)
    protected String imagePath;

    /**
     * Gets the value of the invoiceNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInvoiceNo() {
        return invoiceNo;
    }

    /**
     * Sets the value of the invoiceNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInvoiceNo(String value) {
        this.invoiceNo = value;
    }

    /**
     * Gets the value of the address property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the value of the address property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddress(String value) {
        this.address = value;
    }

    /**
     * Gets the value of the dateOfInvoice property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDateOfInvoice() {
        return dateOfInvoice;
    }

    /**
     * Sets the value of the dateOfInvoice property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateOfInvoice(String value) {
        this.dateOfInvoice = value;
    }

    /**
     * Gets the value of the taxPointDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTaxPointDate() {
        return taxPointDate;
    }

    /**
     * Sets the value of the taxPointDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTaxPointDate(String value) {
        this.taxPointDate = value;
    }

    /**
     * Gets the value of the customerOrderNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerOrderNo() {
        return customerOrderNo;
    }

    /**
     * Sets the value of the customerOrderNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerOrderNo(String value) {
        this.customerOrderNo = value;
    }

    /**
     * Gets the value of the despatchDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDespatchDate() {
        return despatchDate;
    }

    /**
     * Sets the value of the despatchDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDespatchDate(String value) {
        this.despatchDate = value;
    }

    /**
     * Gets the value of the customerACNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerACNo() {
        return customerACNo;
    }

    /**
     * Sets the value of the customerACNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerACNo(String value) {
        this.customerACNo = value;
    }

    /**
     * Gets the value of the invoiceTotal property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getInvoiceTotal() {
        return invoiceTotal;
    }

    /**
     * Sets the value of the invoiceTotal property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setInvoiceTotal(BigDecimal value) {
        this.invoiceTotal = value;
    }

    /**
     * Gets the value of the table property.
     * 
     * @return
     *     possible object is
     *     {@link Table }
     *     
     */
    public Table getTable() {
        return table;
    }

    /**
     * Sets the value of the table property.
     * 
     * @param value
     *     allowed object is
     *     {@link Table }
     *     
     */
    public void setTable(Table value) {
        this.table = value;
    }

    /**
     * Gets the value of the imagePath property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * Sets the value of the imagePath property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImagePath(String value) {
        this.imagePath = value;
    }

}
