
package com.est.webservice.server.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>weight complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
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
     * ��ȡbanbie���Ե�ֵ��
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
     * ����banbie���Ե�ֵ��
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
     * ��ȡdeWeight���Ե�ֵ��
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
     * ����deWeight���Ե�ֵ��
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
     * ��ȡgrossDate���Ե�ֵ��
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
     * ����grossDate���Ե�ֵ��
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
     * ��ȡgrossMan���Ե�ֵ��
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
     * ����grossMan���Ե�ֵ��
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
     * ��ȡgrossWeight���Ե�ֵ��
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
     * ����grossWeight���Ե�ֵ��
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
     * ��ȡguige���Ե�ֵ��
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
     * ����guige���Ե�ֵ��
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
     * ��ȡjizu���Ե�ֵ��
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
     * ����jizu���Ե�ֵ��
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
     * ��ȡnetWeight���Ե�ֵ��
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
     * ����netWeight���Ե�ֵ��
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
     * ��ȡoutno���Ե�ֵ��
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
     * ����outno���Ե�ֵ��
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
     * ��ȡprintCount���Ե�ֵ��
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
     * ����printCount���Ե�ֵ��
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
     * ��ȡreciveCompany���Ե�ֵ��
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
     * ����reciveCompany���Ե�ֵ��
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
     * ��ȡremark���Ե�ֵ��
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
     * ����remark���Ե�ֵ��
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
     * ��ȡsendCompany���Ե�ֵ��
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
     * ����sendCompany���Ե�ֵ��
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
     * ��ȡsequence���Ե�ֵ��
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
     * ����sequence���Ե�ֵ��
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
     * ��ȡtareDate���Ե�ֵ��
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
     * ����tareDate���Ե�ֵ��
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
     * ��ȡtareMan���Ե�ֵ��
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
     * ����tareMan���Ե�ֵ��
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
     * ��ȡtype���Ե�ֵ��
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
     * ����type���Ե�ֵ��
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
     * ��ȡupdateDate���Ե�ֵ��
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
     * ����updateDate���Ե�ֵ��
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
     * ��ȡupdateMan���Ե�ֵ��
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
     * ����updateMan���Ե�ֵ��
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
     * ��ȡvehicleNo���Ե�ֵ��
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
     * ����vehicleNo���Ե�ֵ��
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
     * ��ȡvehicleWeight���Ե�ֵ��
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
     * ����vehicleWeight���Ե�ֵ��
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
     * ��ȡwaterNo���Ե�ֵ��
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
     * ����waterNo���Ե�ֵ��
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
     * ��ȡweightType���Ե�ֵ��
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
     * ����weightType���Ե�ֵ��
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
