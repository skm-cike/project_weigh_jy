package com.est.webservice.server.vo;

/**
 * Created by 陆华 on 15-10-28 下午2:55
 */
public class DownloadForClient {
    private String operat;
    private Long operatDate;

    public String getOperat() {
        return operat;
    }

    public void setOperat(String operat) {
        this.operat = operat;
    }

    public Long getOperatDate() {
        return operatDate;
    }

    public void setOperatDate(Long operatDate) {
        this.operatDate = operatDate;
    }
}
