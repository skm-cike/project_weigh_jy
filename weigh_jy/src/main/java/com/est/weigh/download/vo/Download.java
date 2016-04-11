package com.est.weigh.download.vo;

import java.util.Date;

/**
 * Downloadqk entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Download implements java.io.Serializable {

	// Fields

	private Long id;
    private String operat;
    private Date operatDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOperat() {
        return operat;
    }

    public void setOperat(String operat) {
        this.operat = operat;
    }

    public Date getOperatDate() {
        return operatDate;
    }

    public void setOperatDate(Date operatDate) {
        this.operatDate = operatDate;
    }
}