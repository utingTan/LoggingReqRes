/**
 * @(#) GwUrlMapping.java
 * 
 * Description: {function name}
 *
 * Modify History: 
 * 	v1.00, 2020/05/25, easonhsu
 *	 1) First release
 */

package com.tcb.otp.gw.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = "OTP", name = "GW_URL_MAPPING")
public class GwUrlMapping implements Serializable {

	private static final long serialVersionUID = 4131385967388473101L;

	@Id
	@Column(name = "API_ID")
	private String apiId;
	
	@Basic
	@Column(name = "URL")
	private String url;
	
	@Basic
	@Column(name = "ACCESS_RIGHTS")
	private String accessRights;
	
	@Basic
	@Column(name = "NEED_LOG")
	private String needLog;
	
	@Basic
	@Column(name = "MEMO")
	private String memo;
	
	// ========== Getter & Setter ==========
	public String getApiId() {
		return apiId;
	}
	public void setApiId(String apiId) {
		this.apiId = apiId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getAccessRights() {
		return accessRights;
	}
	public void setAccessRights(String accessRights) {
		this.accessRights = accessRights;
	}
	public String getNeedLog() {
		return needLog;
	}
	public void setNeedLog(String needLog) {
		this.needLog = needLog;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
}
