package com.est.webservice.server.service;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.6.9
 * 2015-11-17T15:02:15.464+08:00
 * Generated source version: 2.6.9
 * 
 */
@WebServiceClient(name = "IWeighTrans", 
                  wsdlLocation = "http://127.0.0.1:8080/weigh_jy/interface/weighjytrans?wsdl",
                  targetNamespace = "http://service.server.webservice.est.com/") 
public class IWeighTrans extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://service.server.webservice.est.com/", "IWeighTrans");
    public final static QName WeighTransServiceImpPort = new QName("http://service.server.webservice.est.com/", "WeighTransServiceImpPort");
    static {
        URL url = null;
        try {
            url = new URL("http://127.0.0.1:8080/weigh_jy/interface/weighjytrans?wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(IWeighTrans.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "http://127.0.0.1:8080/weigh_jy/interface/weighjytrans?wsdl");
        }
        WSDL_LOCATION = url;
    }

    public IWeighTrans(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public IWeighTrans(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public IWeighTrans() {
        super(WSDL_LOCATION, SERVICE);
    }
    

    /**
     *
     * @return
     *     returns IWeighTransService
     */
    @WebEndpoint(name = "WeighTransServiceImpPort")
    public IWeighTransService getWeighTransServiceImpPort() {
        return super.getPort(WeighTransServiceImpPort, IWeighTransService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns IWeighTransService
     */
    @WebEndpoint(name = "WeighTransServiceImpPort")
    public IWeighTransService getWeighTransServiceImpPort(WebServiceFeature... features) {
        return super.getPort(WeighTransServiceImpPort, IWeighTransService.class, features);
    }

}