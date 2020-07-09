package com.zhny.library.presenter.login.model.dto;


import java.io.Serializable;
import java.util.List;

/**
 * userinfo vo
 */

public class UserInfoDto implements Serializable {

    private String creationDate;
    private String createdByprivate;
    private String lastUpdateDate;
    private String lastUpdatedBy;
    private String objectVersionNumber;
    private String id;
    private String code;
    private String name;
    private String realName;
    private String status;
    private String phone;
    private String email;
    private String password;
    private String disabled;
    private String source;
    private String additionInfo;
    private String locked;
    private String passwordExpireAt;
    private String lastPasswordUpdatedAt;
    private String lockedUntilAt;
    private String passwordAttempt;
    private String lastLoginAt;
    private String language;
    private String timeZone;
    private String remark;
    private String organizationId;
    private String organizationName;
    private String imageUrl;
    private String userType;
    private String farmUuid;
    private String nickName;
    private String authority;
    private String tenantId;
    private String farmName;
    private String oldName;
    private String languageName;
    private String defaultOrganization;
    private String organizationList;
    private String organizationIds;
    private String roleList;
    private List<Role> roles;
    private String roleIds;
    private String oldRoleIds;
    private String projectList;
    private String menus;
    private String pushId;

    private class Role implements Serializable{
        private String code;
        private String name;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getCreatedByprivate() {
        return createdByprivate;
    }

    public void setCreatedByprivate(String createdByprivate) {
        this.createdByprivate = createdByprivate;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public String getObjectVersionNumber() {
        return objectVersionNumber;
    }

    public void setObjectVersionNumber(String objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDisabled() {
        return disabled;
    }

    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAdditionInfo() {
        return additionInfo;
    }

    public void setAdditionInfo(String additionInfo) {
        this.additionInfo = additionInfo;
    }

    public String getLocked() {
        return locked;
    }

    public void setLocked(String locked) {
        this.locked = locked;
    }

    public String getPasswordExpireAt() {
        return passwordExpireAt;
    }

    public void setPasswordExpireAt(String passwordExpireAt) {
        this.passwordExpireAt = passwordExpireAt;
    }

    public String getLastPasswordUpdatedAt() {
        return lastPasswordUpdatedAt;
    }

    public void setLastPasswordUpdatedAt(String lastPasswordUpdatedAt) {
        this.lastPasswordUpdatedAt = lastPasswordUpdatedAt;
    }

    public String getLockedUntilAt() {
        return lockedUntilAt;
    }

    public void setLockedUntilAt(String lockedUntilAt) {
        this.lockedUntilAt = lockedUntilAt;
    }

    public String getPasswordAttempt() {
        return passwordAttempt;
    }

    public void setPasswordAttempt(String passwordAttempt) {
        this.passwordAttempt = passwordAttempt;
    }

    public String getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(String lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getFarmUuid() {
        return farmUuid;
    }

    public void setFarmUuid(String farmUuid) {
        this.farmUuid = farmUuid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getFarmName() {
        return farmName;
    }

    public void setFarmName(String farmName) {
        this.farmName = farmName;
    }

    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public String getDefaultOrganization() {
        return defaultOrganization;
    }

    public void setDefaultOrganization(String defaultOrganization) {
        this.defaultOrganization = defaultOrganization;
    }

    public String getOrganizationList() {
        return organizationList;
    }

    public void setOrganizationList(String organizationList) {
        this.organizationList = organizationList;
    }

    public String getOrganizationIds() {
        return organizationIds;
    }

    public void setOrganizationIds(String organizationIds) {
        this.organizationIds = organizationIds;
    }

    public String getRoleList() {
        return roleList;
    }

    public void setRoleList(String roleList) {
        this.roleList = roleList;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }

    public String getOldRoleIds() {
        return oldRoleIds;
    }

    public void setOldRoleIds(String oldRoleIds) {
        this.oldRoleIds = oldRoleIds;
    }

    public String getProjectList() {
        return projectList;
    }

    public void setProjectList(String projectList) {
        this.projectList = projectList;
    }

    public String getMenus() {
        return menus;
    }

    public void setMenus(String menus) {
        this.menus = menus;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }
}
