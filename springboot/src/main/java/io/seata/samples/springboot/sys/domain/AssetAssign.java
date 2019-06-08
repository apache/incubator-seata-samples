/*
 *  Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.seata.samples.springboot.sys.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the t_asset_assign database table.
 */
@Entity
@Table(name = "t_asset_assign")
@NamedQuery(name = "AssetAssign.findAll", query = "SELECT a FROM AssetAssign a")
public class AssetAssign implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name = "asset_id")
	private String assetId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time")
	private Date createTime;

	@Column(name = "create_user")
	private String createUser;

	@Column(name = "`desc`")
	private String desc;

	@Column(name = "`status`")
	private String status;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_time")
	private Date updateTime;

	@Column(name = "update_user")
	private String updateUser;

	/**
	 * Gets id.
	 *
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets id.
	 *
	 * @param id the id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets asset id.
	 *
	 * @return the asset id
	 */
	public String getAssetId() {
		return assetId;
	}

	/**
	 * Sets asset id.
	 *
	 * @param assetId the asset id
	 */
	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}

	/**
	 * Gets create time.
	 *
	 * @return the create time
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * Sets create time.
	 *
	 * @param createTime the create time
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * Gets create user.
	 *
	 * @return the create user
	 */
	public String getCreateUser() {
		return createUser;
	}

	/**
	 * Sets create user.
	 *
	 * @param createUser the create user
	 */
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	/**
	 * Gets desc.
	 *
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * Sets desc.
	 *
	 * @param desc the desc
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * Gets status.
	 *
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets status.
	 *
	 * @param status the status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Gets update time.
	 *
	 * @return the update time
	 */
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * Sets update time.
	 *
	 * @param updateTime the update time
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * Gets update user.
	 *
	 * @return the update user
	 */
	public String getUpdateUser() {
		return updateUser;
	}

	/**
	 * Sets update user.
	 *
	 * @param updateUser the update user
	 */
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("AssetAssign{");
		sb.append("id='").append(id).append('\'');
		sb.append(", assetId='").append(assetId).append('\'');
		sb.append(", createTime=").append(createTime);
		sb.append(", createUser='").append(createUser).append('\'');
		sb.append(", desc='").append(desc).append('\'');
		sb.append(", status='").append(status).append('\'');
		sb.append(", updateTime=").append(updateTime);
		sb.append(", updateUser='").append(updateUser).append('\'');
		sb.append('}');
		return sb.toString();
	}
}