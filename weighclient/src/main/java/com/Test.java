package com;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 15-7-20
 * Time: 上午11:28
 * To change this template use File | Settings | File Templates.
 */
public class Test {
    public static void  main(String[] args) {
        try {
            Desktop.getDesktop().open(new File("test.bat"));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
