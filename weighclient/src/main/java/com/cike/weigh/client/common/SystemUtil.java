package com.cike.weigh.client.common;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class SystemUtil {
	  public static String getLocalMac()
	  {
	    try
	    {
          byte[] mac = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getHardwareAddress();
	      StringBuffer sb = new StringBuffer("");
	      for (int i = 0; i < mac.length; i++) {
	        if (i != 0) {
	          sb.append("-");
	        }

	        int temp = mac[i] & 0xFF;
	        String str = Integer.toHexString(temp);
	        if (str.length() == 1)
	          sb.append("0" + str);
	        else {
	          sb.append(str);
	        }
	      }
	      return sb.toString();
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return null;
	  }
}
