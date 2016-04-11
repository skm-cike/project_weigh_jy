
package com.est.webservice.server.service;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.est.webservice.server.service package. 
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

    private final static QName _GetDownloadsResponse_QNAME = new QName("http://service.server.webservice.est.com/", "getDownloadsResponse");
    private final static QName _GetDownloads_QNAME = new QName("http://service.server.webservice.est.com/", "getDownloads");
    private final static QName _SavUploadDataResponse_QNAME = new QName("http://service.server.webservice.est.com/", "savUploadDataResponse");
    private final static QName _SavUploadData_QNAME = new QName("http://service.server.webservice.est.com/", "savUploadData");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.est.webservice.server.service
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SavUploadData }
     * 
     */
    public SavUploadData createSavUploadData() {
        return new SavUploadData();
    }

    /**
     * Create an instance of {@link SavUploadDataResponse }
     * 
     */
    public SavUploadDataResponse createSavUploadDataResponse() {
        return new SavUploadDataResponse();
    }

    /**
     * Create an instance of {@link GetDownloads }
     * 
     */
    public GetDownloads createGetDownloads() {
        return new GetDownloads();
    }

    /**
     * Create an instance of {@link GetDownloadsResponse }
     * 
     */
    public GetDownloadsResponse createGetDownloadsResponse() {
        return new GetDownloadsResponse();
    }

    /**
     * Create an instance of {@link DownloadForClient }
     * 
     */
    public DownloadForClient createDownloadForClient() {
        return new DownloadForClient();
    }

    /**
     * Create an instance of {@link Weight }
     * 
     */
    public Weight createWeight() {
        return new Weight();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDownloadsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.webservice.est.com/", name = "getDownloadsResponse")
    public JAXBElement<GetDownloadsResponse> createGetDownloadsResponse(GetDownloadsResponse value) {
        return new JAXBElement<GetDownloadsResponse>(_GetDownloadsResponse_QNAME, GetDownloadsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDownloads }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.webservice.est.com/", name = "getDownloads")
    public JAXBElement<GetDownloads> createGetDownloads(GetDownloads value) {
        return new JAXBElement<GetDownloads>(_GetDownloads_QNAME, GetDownloads.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SavUploadDataResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.webservice.est.com/", name = "savUploadDataResponse")
    public JAXBElement<SavUploadDataResponse> createSavUploadDataResponse(SavUploadDataResponse value) {
        return new JAXBElement<SavUploadDataResponse>(_SavUploadDataResponse_QNAME, SavUploadDataResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SavUploadData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.server.webservice.est.com/", name = "savUploadData")
    public JAXBElement<SavUploadData> createSavUploadData(SavUploadData value) {
        return new JAXBElement<SavUploadData>(_SavUploadData_QNAME, SavUploadData.class, null, value);
    }

}
