package com.cike.weigh.client.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ResourceUtil
{
  public static InputStream getInputStream(String filename)
  {
    try
    {
      return new FileInputStream(getFile(filename));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }return null;
  }

  public static File getFile(String filename)
  {
    return new File(getUrl(filename));
  }

  public static String getUrl(String filename)
  {
    return new File(filename).getAbsolutePath();
//    return Class.class.getResource("/" + filename).getPath();
  }
}