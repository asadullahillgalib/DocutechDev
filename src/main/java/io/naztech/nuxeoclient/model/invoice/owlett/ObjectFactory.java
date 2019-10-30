
package io.naztech.nuxeoclient.model.invoice.owlett;

import java.math.BigDecimal;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the io.naztech.nuxeoclient.model.invoice.owlett package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _CustomerOrderNo_QNAME = new QName("", "_CustomerOrderNo");
    private final static QName _InvoiceTotal_QNAME = new QName("", "_InvoiceTotal");
    private final static QName _DateOfInvoice_QNAME = new QName("", "_DateOfInvoice");
    private final static QName _DespatchDate_QNAME = new QName("", "_DespatchDate");
    private final static QName _InvoiceNo_QNAME = new QName("", "_InvoiceNo");
    private final static QName _Address_QNAME = new QName("", "_Address");
    private final static QName _TaxPointDate_QNAME = new QName("", "_TaxPointDate");
    private final static QName _CustomerACNo_QNAME = new QName("", "_CustomerACNo");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: io.naztech.nuxeoclient.model.invoice.owlett
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link OwlettInvoiceDocument }
     * 
     */
    public OwlettInvoiceDocument createDocuments() {
        return new OwlettInvoiceDocument();
    }

    /**
     * Create an instance of {@link OwlettDocumentDefinition }
     * 
     */
    public OwlettDocumentDefinition createOwlettDocumentDefinition() {
        return new OwlettDocumentDefinition();
    }

    /**
     * Create an instance of {@link Table }
     * 
     */
    public Table createTable() {
        return new Table();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "_CustomerOrderNo")
    public JAXBElement<String> createCustomerOrderNo(String value) {
        return new JAXBElement<String>(_CustomerOrderNo_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "_InvoiceTotal")
    public JAXBElement<BigDecimal> createInvoiceTotal(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_InvoiceTotal_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "_DateOfInvoice")
    public JAXBElement<String> createDateOfInvoice(String value) {
        return new JAXBElement<String>(_DateOfInvoice_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "_DespatchDate")
    public JAXBElement<String> createDespatchDate(String value) {
        return new JAXBElement<String>(_DespatchDate_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "_InvoiceNo")
    public JAXBElement<String> createInvoiceNo(String value) {
        return new JAXBElement<String>(_InvoiceNo_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "_Address")
    public JAXBElement<String> createAddress(String value) {
        return new JAXBElement<String>(_Address_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "_TaxPointDate")
    public JAXBElement<String> createTaxPointDate(String value) {
        return new JAXBElement<String>(_TaxPointDate_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "_CustomerACNo")
    public JAXBElement<String> createCustomerACNo(String value) {
        return new JAXBElement<String>(_CustomerACNo_QNAME, String.class, null, value);
    }

}
