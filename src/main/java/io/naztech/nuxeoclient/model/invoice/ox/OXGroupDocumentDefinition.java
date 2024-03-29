//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.10.09 at 03:38:03 PM BDT 
//


package io.naztech.nuxeoclient.model.invoice.ox;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import io.naztech.nuxeoclient.model.invoice.ox.InvoiceTable;


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
 *         &lt;element ref="{}_customerName"/>
 *         &lt;element ref="{}_customerAddress"/>
 *         &lt;element ref="{}_invoiceDate"/>
 *         &lt;element ref="{}_invoiceNumber"/>
 *         &lt;element ref="{}_supplierTelephoneNo"/>
 *         &lt;element ref="{}_orderId"/>
 *         &lt;element ref="{}_supplierAccountNumber"/>
 *         &lt;element ref="{}_faxNumber"/>
 *         &lt;element ref="{}_email"/>
 *         &lt;element ref="{}_orderNo"/>
 *         &lt;element ref="{}_referenceNo"/>
 *         &lt;element ref="{}_orderDate"/>
 *         &lt;element ref="{}_deliveryNumber"/>
 *         &lt;element ref="{}_invoiceTable" maxOccurs="unbounded"/>
 *         &lt;element ref="{}_valueOfGoods"/>
 *         &lt;element ref="{}_vat"/>
 *         &lt;element ref="{}_netInvoiceTotal"/>
 *         &lt;element ref="{}_accountNumber"/>
 *         &lt;element ref="{}_supplierName"/>
 *         &lt;element ref="{}_supplierAddress"/>
 *         &lt;element ref="{}_vatRegNumber"/>
 *         &lt;element ref="{}_deliveryAddress"/>
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
    "customerName",
    "customerAddress",
    "invoiceDate",
    "invoiceNumber",
    "supplierTelephoneNo",
    "orderId",
    "supplierAccountNumber",
    "faxNumber",
    "email",
    "orderNo",
    "referenceNo",
    "orderDate",
    "deliveryNumber",
    "invoiceTable",
    "valueOfGoods",
    "vat",
    "netInvoiceTotal",
    "accountNumber",
    "supplierName",
    "supplierAddress",
    "vatRegNumber",
    "deliveryAddress"
})
@XmlRootElement(name = "_OXGroup_Document_Definition")
public class OXGroupDocumentDefinition {

    @XmlElement(name = "_customerName", required = true)
    protected String customerName;
    @XmlElement(name = "_customerAddress", required = true)
    protected String customerAddress;
    @XmlElement(name = "_invoiceDate", required = true)
    protected String invoiceDate;
    @XmlElement(name = "_invoiceNumber")
    @XmlSchemaType(name = "unsignedInt")
    protected long invoiceNumber;
    @XmlElement(name = "_supplierTelephoneNo", required = true)
    protected Object supplierTelephoneNo;
    @XmlElement(name = "_orderId", required = true)
    protected String orderId;
    @XmlElement(name = "_supplierAccountNumber", required = true)
    protected String supplierAccountNumber;
    @XmlElement(name = "_faxNumber", required = true)
    protected Object faxNumber;
    @XmlElement(name = "_email", required = true)
    protected Object email;
    @XmlElement(name = "_orderNo")
    @XmlSchemaType(name = "unsignedInt")
    protected long orderNo;
    @XmlElement(name = "_referenceNo", required = true)
    protected String referenceNo;
    @XmlElement(name = "_orderDate", required = true)
    protected String orderDate;
    @XmlElement(name = "_deliveryNumber")
    @XmlSchemaType(name = "unsignedInt")
    protected long deliveryNumber;
    @XmlElement(name = "_invoiceTable", required = true)
    protected List<InvoiceTable> invoiceTable;
    @XmlElement(name = "_valueOfGoods", required = true)
    protected BigDecimal valueOfGoods;
    @XmlElement(name = "_vat", required = true)
    protected BigDecimal vat;
    @XmlElement(name = "_netInvoiceTotal", required = true)
    protected BigDecimal netInvoiceTotal;
    @XmlElement(name = "_accountNumber")
    @XmlSchemaType(name = "unsignedInt")
    protected long accountNumber;
    @XmlElement(name = "_supplierName", required = true)
    protected String supplierName;
    @XmlElement(name = "_supplierAddress", required = true)
    protected String supplierAddress;
    @XmlElement(name = "_vatRegNumber", required = true)
    protected String vatRegNumber;
    @XmlElement(name = "_deliveryAddress", required = true)
    protected String deliveryAddress;
    @XmlAttribute(name = "ImagePath", namespace = "http://www.abbyy.com/FlexiCapture/Schemas/Export/AdditionalFormData.xsd", required = true)
    protected String imagePath;

    /**
     * Gets the value of the customerName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Sets the value of the customerName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerName(String value) {
        this.customerName = value;
    }

    /**
     * Gets the value of the customerAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerAddress() {
        return customerAddress;
    }

    /**
     * Sets the value of the customerAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerAddress(String value) {
        this.customerAddress = value;
    }

    /**
     * Gets the value of the invoiceDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInvoiceDate() {
        return invoiceDate;
    }

    /**
     * Sets the value of the invoiceDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInvoiceDate(String value) {
        this.invoiceDate = value;
    }

    /**
     * Gets the value of the invoiceNumber property.
     * 
     */
    public long getInvoiceNumber() {
        return invoiceNumber;
    }

    /**
     * Sets the value of the invoiceNumber property.
     * 
     */
    public void setInvoiceNumber(long value) {
        this.invoiceNumber = value;
    }

    /**
     * Gets the value of the supplierTelephoneNo property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getSupplierTelephoneNo() {
        return supplierTelephoneNo;
    }

    /**
     * Sets the value of the supplierTelephoneNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setSupplierTelephoneNo(Object value) {
        this.supplierTelephoneNo = value;
    }

    /**
     * Gets the value of the orderId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * Sets the value of the orderId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderId(String value) {
        this.orderId = value;
    }

    /**
     * Gets the value of the supplierAccountNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSupplierAccountNumber() {
        return supplierAccountNumber;
    }

    /**
     * Sets the value of the supplierAccountNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSupplierAccountNumber(String value) {
        this.supplierAccountNumber = value;
    }

    /**
     * Gets the value of the faxNumber property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getFaxNumber() {
        return faxNumber;
    }

    /**
     * Sets the value of the faxNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setFaxNumber(Object value) {
        this.faxNumber = value;
    }

    /**
     * Gets the value of the email property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getEmail() {
        return email;
    }

    /**
     * Sets the value of the email property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setEmail(Object value) {
        this.email = value;
    }

    /**
     * Gets the value of the orderNo property.
     * 
     */
    public long getOrderNo() {
        return orderNo;
    }

    /**
     * Sets the value of the orderNo property.
     * 
     */
    public void setOrderNo(long value) {
        this.orderNo = value;
    }

    /**
     * Gets the value of the referenceNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReferenceNo() {
        return referenceNo;
    }

    /**
     * Sets the value of the referenceNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReferenceNo(String value) {
        this.referenceNo = value;
    }

    /**
     * Gets the value of the orderDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderDate() {
        return orderDate;
    }

    /**
     * Sets the value of the orderDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderDate(String value) {
        this.orderDate = value;
    }

    /**
     * Gets the value of the deliveryNumber property.
     * 
     */
    public long getDeliveryNumber() {
        return deliveryNumber;
    }

    /**
     * Sets the value of the deliveryNumber property.
     * 
     */
    public void setDeliveryNumber(long value) {
        this.deliveryNumber = value;
    }

    /**
     * Gets the value of the invoiceTable property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the invoiceTable property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInvoiceTable().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InvoiceTable }
     * 
     * 
     */
    public List<InvoiceTable> getInvoiceTable() {
        if (invoiceTable == null) {
            invoiceTable = new ArrayList<InvoiceTable>();
        }
        return this.invoiceTable;
    }

    /**
     * Gets the value of the valueOfGoods property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getValueOfGoods() {
        return valueOfGoods;
    }

    /**
     * Sets the value of the valueOfGoods property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setValueOfGoods(BigDecimal value) {
        this.valueOfGoods = value;
    }

    /**
     * Gets the value of the vat property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getVat() {
        return vat;
    }

    /**
     * Sets the value of the vat property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setVat(BigDecimal value) {
        this.vat = value;
    }

    /**
     * Gets the value of the netInvoiceTotal property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getNetInvoiceTotal() {
        return netInvoiceTotal;
    }

    /**
     * Sets the value of the netInvoiceTotal property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setNetInvoiceTotal(BigDecimal value) {
        this.netInvoiceTotal = value;
    }

    /**
     * Gets the value of the accountNumber property.
     * 
     */
    public long getAccountNumber() {
        return accountNumber;
    }

    /**
     * Sets the value of the accountNumber property.
     * 
     */
    public void setAccountNumber(long value) {
        this.accountNumber = value;
    }

    /**
     * Gets the value of the supplierName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSupplierName() {
        return supplierName;
    }

    /**
     * Sets the value of the supplierName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSupplierName(String value) {
        this.supplierName = value;
    }

    /**
     * Gets the value of the supplierAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSupplierAddress() {
        return supplierAddress;
    }

    /**
     * Sets the value of the supplierAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSupplierAddress(String value) {
        this.supplierAddress = value;
    }

    /**
     * Gets the value of the vatRegNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVatRegNumber() {
        return vatRegNumber;
    }

    /**
     * Sets the value of the vatRegNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVatRegNumber(String value) {
        this.vatRegNumber = value;
    }

    /**
     * Gets the value of the deliveryAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    /**
     * Sets the value of the deliveryAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliveryAddress(String value) {
        this.deliveryAddress = value;
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
