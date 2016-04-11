package com.est.webservice.server.interceptor;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.springframework.beans.factory.annotation.Autowired;

import com.est.sysinit.sysuser.service.ISysUserService;

public class SoapOutInterceptor extends AbstractPhaseInterceptor<Message>{
	public SoapOutInterceptor() {
		super(Phase.PRE_STREAM);
	}
	public void handleMessage(Message arg0) throws Fault {
		//System.out.println("退出");
	}
}
