package com.est.sysinit.sysfilemanage.common;

import com.est.common.ext.util.fileutil.PropertiesProvider;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 15-7-17
 * Time: 上午1:02
 * To change this template use File | Settings | File Templates.
 */
public class NormalFileProvider  {
    private static String basePath;
    private static String propertyName="filepath.properties";
    public static String getBasePath() {
        if (basePath == null) {
            try {
                PropertiesProvider provider = PropertiesProvider.getInstance();
                provider.load(propertyName);
                basePath = provider.getProperty("fileupanddown_basepth");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return basePath;
    }
}
