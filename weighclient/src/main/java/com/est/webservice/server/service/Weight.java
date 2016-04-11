
package com.est.webservice.server.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>weight complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="weight">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="banbie" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="de_weight" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="gross_date" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="gross_man" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="gross_weight" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="guige" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="jizu" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="net_weight" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="outno" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="printCount" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="recive_company" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="remark" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="send_company" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sequence" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="tare_date" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="tare_man" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="updateDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="updateMan" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="vehicle_no" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="vehicle_weight" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="water_no" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="weight_type" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "weight", propOrder = {
    "banbie",
    "deWeight",
    "grossDate",
    "grossMan",
    "grossWeight",
    "guige",
    "jizu",
    "netWeight",
    "outno",
    "printCount",
    "reciveCompany",
    "remark",
    "sendCompany",
    "sequence",
    "tareDate",
    "tareMan",
    "type",
    "updateDate",
    "updateMan",
    "vehicleNo",
    "vehicleWeight",
    "waterNo",
    "weightType"
})
public class Weight {

    protected String banbie;
    @XmlElement(name = "de_weight")
    protected Double deWeight;
    @XmlElement(name = "gross_date")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar grossDate;
    @XmlElement(name = "gross_man")
    protected String grossMan;
    @XmlElement(name = "gross_weight")
    protected Double grossWeight;
    protected String guige;
    protected String jizu;
    @XmlElement(name = "net_weight")
    protected Double netWeight;
    protected String outno;
    protected Integer printCount;
    @XmlElement(name = "recive_company")
    protected String reciveCompany;
    protected String remark;
    @XmlElement(name = "send_company")
    protected String sendCompany;
    protected Long sequence;
    @XmlElement(name = "tare_date")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar tareDate;
    @XmlElement(name = "tare_man")
    protected String tareMan;
    protected String type;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar updateDate;
    protected String updateMan;
    @XmlElement(name = "vehicle_no")
    protected String vehicleNo;
    @XmlElement(name = "vehicle_weight")
    protected Double vehicleWeight;
    @XmlElement(name = "water_no")
    protected String waterNo;
    @XmlElement(name = "weight_type")
    protected String weightType;

    /**
     * 获取banbie属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBanbie() {
        return banbie;
    }

    /**
     * 设置banbie属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBanbie(String value) {
        this.banbie = value;
    }

    /**
     * 获取deWeight属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getDeWeight() {
        return deWeight;
    }

    /**
     * 设置deWeight属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setDeWeight(Double value) {
        this.deWeight = value;
    }

    /**
     * 获取grossDate属性的值。
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getGrossDate() {
        return grossDate;
    }

    /**
     * 设置grossDate属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setGrossDate(XMLGregorianCalendar value) {
        this.grossDate = value;
    }

    /**
     * 获取grossMan属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGrossMan() {
        return grossMan;
    }

    /**
     * 设置grossMan属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGrossMan(String value) {
        this.grossMan = value;
    }

    /**
     * 获取grossWeight属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getGrossWeight() {
        return grossWeight;
    }

    /**
     * 设置grossWeight属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setGrossWeight(Double value) {
        this.grossWeight = value;
    }

    /**
     * 获取guige属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGuige() {
        return guige;
    }

    /**
     * 设置guige属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGuige(String value) {
        this.guige = value;
    }

    /**
     * 获取jizu属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJizu() {
        return jizu;
    }

    /**
     * 设置jizu属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJizu(String value) {
        this.jizu = value;
    }

    /**
     * 获取netWeight属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getNetWeight() {
        return netWeight;
    }

    /**
     * 设置netWeight属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setNetWeight(Double value) {
        this.netWeight = value;
    }

    /**
     * 获取outno属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOutno() {
        return outno;
    }

    /**
     * 设置outno属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOutno(String value) {
        this.outno = value;
    }

    /**
     * 获取printCount属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPrintCount() {
        return printCount;
    }

    /**
     * 设置printCount属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPrintCount(Integer value) {
        this.printCount = value;
    }

    /**
     * 获取reciveCompany属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReciveCompany() {
        return reciveCompany;
    }

    /**
     * 设置reciveCompany属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReciveCompany(String value) {
        this.reciveCompany = value;
    }

    /**
     * 获取remark属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置remark属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRemark(String value) {
        this.remark = value;
    }

    /**
     * 获取sendCompany属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSendCompany() {
        return sendCompany;
    }

    /**
     * 设置sendCompany属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSendCompany(String value) {
        this.sendCompany = value;
    }

    /**
     * 获取sequence属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getSequence() {
        return sequence;
    }

    /**
     * 设置sequence属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setSequence(Long value) {
        this.sequence = value;
    }

    /**
     * 获取tareDate属性的值。
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getTareDate() {
        return tareDate;
    }

    /**
     * 设置tareDate属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setTareDate(XMLGregorianCalendar value) {
        this.tareDate = value;
    }

    /**
     * 获取tareMan属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTareMan() {
        return tareMan;
    }

    /**
     * 设置tareMan属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTareMan(String value) {
        this.tareMan = value;
    }

    /**
     * 获取type属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * 设置type属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * 获取updateDate属性的值。
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getUpdateDate() {
        return updateDate;
    }

    /**
     * 设置updateDate属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setUpdateDate(XMLGregorianCalendar value) {
        this.updateDate = value;
    }

    /**
     * 获取updateMan属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUpdateMan() {
        return updateMan;
    }

    /**
     * 设置updateMan属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUpdateMan(String value) {
        this.updateMan = value;
    }

    /**
     * 获取vehicleNo属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVehicleNo() {
        return vehicleNo;
    }

    /**
     * 设置vehicleNo属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVehicleNo(String value) {
        this.vehicleNo = value;
    }

    /**
     * 获取vehicleWeight属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getVehicleWeight() {
        return vehicleWeight;
    }

    /**
     * 设置vehicleWeight属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setVehicleWeight(Double value) {
        this.vehicleWeight = value;
    }

    /**
     * 获取waterNo属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWaterNo() {
        return waterNo;
    }

    /**
     * 设置waterNo属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWaterNo(String value) {
        this.waterNo = value;
    }

    /**
     * 获取weightType属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWeightType() {
        return weightType;
    }

    /**
     * 设置weightType属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWeightType(String value) {
        this.weightType = value;
    }

}
