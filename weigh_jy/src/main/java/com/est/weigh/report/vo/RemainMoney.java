package com.est.weigh.report.vo;

/**
 * @����: �������
 */
public class RemainMoney {
    private String companycode;
    private String companyname;
    private Double remainMoney;
    private Double jzje;
    private String jizu;

    public Double getJzje() {
        return jzje;
    }

    public void setJzje(Double jzje) {
        this.jzje = jzje;
    }

    public String getCompanycode() {
        return companycode;
    }

    public void setCompanycode(String companycode) {
        this.companycode = companycode;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public Double getRemainMoney() {
        return remainMoney;
    }

    public void setRemainMoney(Double remainMoney) {
        this.remainMoney = remainMoney;
    }

    public String getJizu() {
        return jizu;
    }

    public void setJizu(String jizu) {
        this.jizu = jizu;
    }
}
