package com.cike.weigh.client.common;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CfgUtil
{
  private static PropertiesUtil pro = new PropertiesUtil();
  private static final String fileName = "cfg.properties";

  static
  {
    loadProperties();
  }
  private static void loadProperties() {
    InputStream is = null;
    try {
      is = ResourceUtil.getInputStream("cfg.properties");
      pro.load(is);
    } catch (IOException e) {
      e.printStackTrace();

      if (is != null)
        try {
          is.close();
        } catch (IOException e1) {
          e1.printStackTrace();
        }
    }
    finally
    {
      if (is != null)
        try {
          is.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
    }
  }

  public static String getKey(String key) {
    return pro.getProperty(key);
  }
  public static void write(String key, String val) throws FileNotFoundException {
    pro.setProperty(key, val);
    OutputStream fos = new FileOutputStream("cfg.properties");
    try {
      pro.store(fos, "Update '" + key + "' value");
      fos.flush();
    } catch (IOException e) {
      e.printStackTrace();

      if (fos != null)
        try {
          fos.close();
        } catch (IOException e1) {
          e1.printStackTrace();
        }
    }
    finally
    {
      if (fos != null)
        try {
          fos.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
    }
  }
}