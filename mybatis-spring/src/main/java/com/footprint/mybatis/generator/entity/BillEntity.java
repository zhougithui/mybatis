package com.footprint.mybatis.generator.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class BillEntity {
    private Long id;

    private LocalDate billDate;

    private String billStatus;

    private String billResultDesc;

    private String billRequestNo;

    private BigDecimal initiatorAmount;

    private String parentMerchantNo;

    private LocalDateTime createTime;

    private Date finishTime;

    private String remark;

    private String callbackUrl;

    private Long version;

    private String serviceName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDate billDate) {
        this.billDate = billDate;
    }

    public String getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(String billStatus) {
        this.billStatus = billStatus;
    }

    public String getBillResultDesc() {
        return billResultDesc;
    }

    public void setBillResultDesc(String billResultDesc) {
        this.billResultDesc = billResultDesc;
    }

    public String getBillRequestNo() {
        return billRequestNo;
    }

    public void setBillRequestNo(String billRequestNo) {
        this.billRequestNo = billRequestNo;
    }

    public BigDecimal getInitiatorAmount() {
        return initiatorAmount;
    }

    public void setInitiatorAmount(BigDecimal initiatorAmount) {
        this.initiatorAmount = initiatorAmount;
    }

    public String getParentMerchantNo() {
        return parentMerchantNo;
    }

    public void setParentMerchantNo(String parentMerchantNo) {
        this.parentMerchantNo = parentMerchantNo;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}