package com.est.workflow.processdefination.vo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * WfDfprocess entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class WfDfprocess implements java.io.Serializable {

	// Fields

	private Long processId;
	private Long jbpmpid;
	private String processname;
	private String description;
	private String processversion;
	private Long moduleId;
	private String dfxml;
	private String dfjson;
	private Long order;
	private String isvalid;
	private Date deployDate;
	private String deployer;
	private Date modifyDate;
	private String modifier;
	
	private Set wfProcessinstances = new HashSet(0);
	private Set wfDftasks = new HashSet(0);
	
	
	public Long getProcessId() {
		return processId;
	}
	public void setProcessId(Long processId) {
		this.processId = processId;
	}
	public Long getJbpmpid() {
		return jbpmpid;
	}
	public void setJbpmpid(Long jbpmpid) {
		this.jbpmpid = jbpmpid;
	}
	public String getProcessname() {
		return processname;
	}
	public void setProcessname(String processname) {
		this.processname = processname;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getProcessversion() {
		return processversion;
	}
	public void setProcessversion(String processversion) {
		this.processversion = processversion;
	}
	public Long getModuleId() {
		return moduleId;
	}
	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}
	public String getDfxml() {
		return dfxml;
	}
	public void setDfxml(String dfxml) {
		this.dfxml = dfxml;
	}
	public String getDfjson() {
		return dfjson;
	}
	public void setDfjson(String dfjson) {
		this.dfjson = dfjson;
	}
	public Long getOrder() {
		return order;
	}
	public void setOrder(Long order) {
		this.order = order;
	}
	public String getIsvalid() {
		return isvalid;
	}
	public void setIsvalid(String isvalid) {
		this.isvalid = isvalid;
	}
	public Set getWfProcessinstances() {
		return wfProcessinstances;
	}
	public void setWfProcessinstances(Set wfProcessinstances) {
		this.wfProcessinstances = wfProcessinstances;
	}
	public Set getWfDftasks() {
		return wfDftasks;
	}
	public void setWfDftasks(Set wfDftasks) {
		this.wfDftasks = wfDftasks;
	}
	public Date getDeployDate() {
		return deployDate;
	}
	public void setDeployDate(Date deployDate) {
		this.deployDate = deployDate;
	}
	public String getDeployer() {
		return deployer;
	}
	public void setDeployer(String deployer) {
		this.deployer = deployer;
	}
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	
	public String getName() {
		return this.getProcessname() + " 版本" + this.getProcessversion();
	}
	
	


}