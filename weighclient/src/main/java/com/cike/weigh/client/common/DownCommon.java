package com.cike.weigh.client.common;

import com.est.webservice.server.service.DownloadForClient;

import java.util.LinkedList;

/**
 * Created by 陆华 on 15-10-28 下午3:10
 */
public class DownCommon {
    private static LinkedList<DownloadForClient> list = new LinkedList<DownloadForClient>();
    public static DownloadForClient get() {
        return process("get", null);
    }

    public static void put(DownloadForClient c) {
        process("sav", c);
    }

    private static DownloadForClient process(String operat, DownloadForClient c) {
        synchronized (DownCommon.class) {
            if ("get".equals(operat)) {
                if (list.size() != 0) {
                    return list.remove(0);
                }
            }
            if ("sav".equals(operat)) {
                list.addLast(c);
                return null;
            }
            return null;
        }
    }
}
