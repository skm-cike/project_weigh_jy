
/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

package com.est.webservice.server.service;

import java.util.logging.Logger;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 2.6.9
 * 2015-11-17T15:02:15.446+08:00
 * Generated source version: 2.6.9
 * 
 */

@WebService(
                      serviceName = "IWeighTrans",
                      portName = "WeighTransServiceImpPort",
                      targetNamespace = "http://service.server.webservice.est.com/",
                      wsdlLocation = "http://127.0.0.1:8080/weigh_jy/interface/weighjytrans?wsdl",
                      endpointInterface = "com.est.webservice.server.service.IWeighTransService")
                      
public class IWeighTransServiceImpl implements IWeighTransService {

    private static final Logger LOG = Logger.getLogger(IWeighTransServiceImpl.class.getName());

    /* (non-Javadoc)
     * @see com.est.webservice.server.service.IWeighTransService#getDownloads(java.lang.String  arg0 ,)java.lang.Long  arg1 )*
     */
    public java.util.List<DownloadForClient> getDownloads(String arg0,Long arg1) {
        LOG.info("Executing operation getDownloads");
        System.out.println(arg0);
        System.out.println(arg1);
        try {
            java.util.List<DownloadForClient> _return = null;
            return _return;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    /* (non-Javadoc)
     * @see com.est.webservice.server.service.IWeighTransService#savUploadData(java.lang.String  arg0 ,)java.util.List<com.est.webservice.server.service.Weight>  arg1 )*
     */
    public String savUploadData(String arg0,java.util.List<com.est.webservice.server.service.Weight> arg1) {
        LOG.info("Executing operation savUploadData");
        System.out.println(arg0);
        System.out.println(arg1);
        try {
            String _return = "";
            return _return;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

}