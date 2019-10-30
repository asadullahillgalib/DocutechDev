
package io.naztech.nuxeoclient.model.invoice.owlett;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
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
 *         &lt;element name="_V" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="_ProductCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="_Description" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="_Qty" type="{http://www.w3.org/2001/XMLSchema}unsignedByte"/>
 *         &lt;element name="_Price" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="_Disc" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="_Amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
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
    "v",
    "productCode",
    "description",
    "qty",
    "price",
    "disc",
    "amount"
})
@XmlRootElement(name = "_Table", namespace = "")
public class Table {

    @XmlElement(name = "_V", required = true)
    protected String v;
    @XmlElement(name = "_ProductCode", required = true)
    protected String productCode;
    @XmlElement(name = "_Description", required = true)
    protected String description;
    @XmlElement(name = "_Qty")
    @XmlSchemaType(name = "unsignedByte")
    protected short qty;
    @XmlElement(name = "_Price", required = true)
    protected String price;
    @XmlElement(name = "_Disc", required = true)
    protected String disc;
    @XmlElement(name = "_Amount", required = true)
    protected BigDecimal amount;

    /**
     * Gets the value of the v property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getV() {
        return v;
    }

    /**
     * Sets the value of the v property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setV(String value) {
        this.v = value;
    }

    /**
     * Gets the value of the productCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductCode() {
        return productCode;
    }

    /**
     * Sets the value of the productCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductCode(String value) {
        this.productCode = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the qty property.
     * 
     */
    public short getQty() {
        return qty;
    }

    /**
     * Sets the value of the qty property.
     * 
     */
    public void setQty(short value) {
        this.qty = value;
    }

    /**
     * Gets the value of the price property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrice() {
        return price;
    }

    /**
     * Sets the value of the price property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrice(String value) {
        this.price = value;
    }

    /**
     * Gets the value of the disc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisc() {
        return disc;
    }

    /**
     * Sets the value of the disc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisc(String value) {
        this.disc = value;
    }

    /**
     * Gets the value of the amount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Sets the value of the amount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAmount(BigDecimal value) {
        this.amount = value;
    }

}
