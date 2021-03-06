package com.est.webservice.server.interceptor;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.ws.security.WSPasswordCallback;

public class ServerPasswordCallback  implements CallbackHandler {
	
	public void handle(Callback[] callbacks) throws IOException,
			UnsupportedCallbackException {
		WSPasswordCallback  pc=(WSPasswordCallback) callbacks[0]; 
        String pw=pc.getPassword();  
        String idf=pc.getIdentifier();  
        System.out.println("密码是:"+pw);  
        System.out.println("类型是:"+idf);  
        if(pw.equals("123")&&idf.equals("admin")){  
            System.out.println("成功");  
        }  
        else{  
            throw new SecurityException("验证失败");  
        }  
		
	}
}
