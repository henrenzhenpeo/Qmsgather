package com.biel.qmsgather.domain.upweb;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * cg 百格测试mek
 * @TableName df_up_cg_baige_mek
 */
@TableName(value ="df_up_cg_baige_mek")
public class DfUpCgBaigeMek implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 工序
     */
    private String process;

    /**
     * 工厂
     */
    private String factory;

    /**
     * 阶段
     */
    private String stage;

    /**
     * 日期（yyyy-mm-dd）
     */
    private String testDate;

    /**
     * 出炉时间
     */
    private String stoveTime;

    /**
     * 项目
     */
    private String project;

    /**
     * 烤炉型号（格式：塔式炉/右）
     */
    private String stoveType;

    /**
     * 批号
     */
    private String batchCode;

    /**
     * 测试时间（hh:mm）
     */
    private String testTime;

    /**
     * 取出时间
     */
    private String testTimeEnd;

    /**
     * 测试数量
     */
    private String testNumber;

    /**
     * 测试结果
     */
    private String testResult;

    /**
     * 上传人
     */
    private String uploadName;

    /**
     * 批次号
     */
    private String batchId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    public Integer getId() {
        return id;
    }

    /**
     * id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 工序
     */
    public String getProcess() {
        return process;
    }

    /**
     * 工序
     */
    public void setProcess(String process) {
        this.process = process;
    }

    /**
     * 工厂
     */
    public String getFactory() {
        return factory;
    }

    /**
     * 工厂
     */
    public void setFactory(String factory) {
        this.factory = factory;
    }

    /**
     * 阶段
     */
    public String getStage() {
        return stage;
    }

    /**
     * 阶段
     */
    public void setStage(String stage) {
        this.stage = stage;
    }

    /**
     * 日期（yyyy-mm-dd）
     */
    public String getTestDate() {
        return testDate;
    }

    /**
     * 日期（yyyy-mm-dd）
     */
    public void setTestDate(String testDate) {
        this.testDate = testDate;
    }

    /**
     * 出炉时间
     */
    public String getStoveTime() {
        return stoveTime;
    }

    /**
     * 出炉时间
     */
    public void setStoveTime(String stoveTime) {
        this.stoveTime = stoveTime;
    }

    /**
     * 项目
     */
    public String getProject() {
        return project;
    }

    /**
     * 项目
     */
    public void setProject(String project) {
        this.project = project;
    }

    /**
     * 烤炉型号（格式：塔式炉/右）
     */
    public String getStoveType() {
        return stoveType;
    }

    /**
     * 烤炉型号（格式：塔式炉/右）
     */
    public void setStoveType(String stoveType) {
        this.stoveType = stoveType;
    }

    /**
     * 批号
     */
    public String getBatchCode() {
        return batchCode;
    }

    /**
     * 批号
     */
    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    /**
     * 测试时间（hh:mm）
     */
    public String getTestTime() {
        return testTime;
    }

    /**
     * 测试时间（hh:mm）
     */
    public void setTestTime(String testTime) {
        this.testTime = testTime;
    }

    /**
     * 取出时间
     */
    public String getTestTimeEnd() {
        return testTimeEnd;
    }

    /**
     * 取出时间
     */
    public void setTestTimeEnd(String testTimeEnd) {
        this.testTimeEnd = testTimeEnd;
    }

    /**
     * 测试数量
     */
    public String getTestNumber() {
        return testNumber;
    }

    /**
     * 测试数量
     */
    public void setTestNumber(String testNumber) {
        this.testNumber = testNumber;
    }

    /**
     * 测试结果
     */
    public String getTestResult() {
        return testResult;
    }

    /**
     * 测试结果
     */
    public void setTestResult(String testResult) {
        this.testResult = testResult;
    }

    /**
     * 上传人
     */
    public String getUploadName() {
        return uploadName;
    }

    /**
     * 上传人
     */
    public void setUploadName(String uploadName) {
        this.uploadName = uploadName;
    }

    /**
     * 批次号
     */
    public String getBatchId() {
        return batchId;
    }

    /**
     * 批次号
     */
    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        DfUpCgBaigeMek other = (DfUpCgBaigeMek) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getProcess() == null ? other.getProcess() == null : this.getProcess().equals(other.getProcess()))
            && (this.getFactory() == null ? other.getFactory() == null : this.getFactory().equals(other.getFactory()))
            && (this.getStage() == null ? other.getStage() == null : this.getStage().equals(other.getStage()))
            && (this.getTestDate() == null ? other.getTestDate() == null : this.getTestDate().equals(other.getTestDate()))
            && (this.getStoveTime() == null ? other.getStoveTime() == null : this.getStoveTime().equals(other.getStoveTime()))
            && (this.getProject() == null ? other.getProject() == null : this.getProject().equals(other.getProject()))
            && (this.getStoveType() == null ? other.getStoveType() == null : this.getStoveType().equals(other.getStoveType()))
            && (this.getBatchCode() == null ? other.getBatchCode() == null : this.getBatchCode().equals(other.getBatchCode()))
            && (this.getTestTime() == null ? other.getTestTime() == null : this.getTestTime().equals(other.getTestTime()))
            && (this.getTestTimeEnd() == null ? other.getTestTimeEnd() == null : this.getTestTimeEnd().equals(other.getTestTimeEnd()))
            && (this.getTestNumber() == null ? other.getTestNumber() == null : this.getTestNumber().equals(other.getTestNumber()))
            && (this.getTestResult() == null ? other.getTestResult() == null : this.getTestResult().equals(other.getTestResult()))
            && (this.getUploadName() == null ? other.getUploadName() == null : this.getUploadName().equals(other.getUploadName()))
            && (this.getBatchId() == null ? other.getBatchId() == null : this.getBatchId().equals(other.getBatchId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getProcess() == null) ? 0 : getProcess().hashCode());
        result = prime * result + ((getFactory() == null) ? 0 : getFactory().hashCode());
        result = prime * result + ((getStage() == null) ? 0 : getStage().hashCode());
        result = prime * result + ((getTestDate() == null) ? 0 : getTestDate().hashCode());
        result = prime * result + ((getStoveTime() == null) ? 0 : getStoveTime().hashCode());
        result = prime * result + ((getProject() == null) ? 0 : getProject().hashCode());
        result = prime * result + ((getStoveType() == null) ? 0 : getStoveType().hashCode());
        result = prime * result + ((getBatchCode() == null) ? 0 : getBatchCode().hashCode());
        result = prime * result + ((getTestTime() == null) ? 0 : getTestTime().hashCode());
        result = prime * result + ((getTestTimeEnd() == null) ? 0 : getTestTimeEnd().hashCode());
        result = prime * result + ((getTestNumber() == null) ? 0 : getTestNumber().hashCode());
        result = prime * result + ((getTestResult() == null) ? 0 : getTestResult().hashCode());
        result = prime * result + ((getUploadName() == null) ? 0 : getUploadName().hashCode());
        result = prime * result + ((getBatchId() == null) ? 0 : getBatchId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", process=").append(process);
        sb.append(", factory=").append(factory);
        sb.append(", stage=").append(stage);
        sb.append(", testDate=").append(testDate);
        sb.append(", stoveTime=").append(stoveTime);
        sb.append(", project=").append(project);
        sb.append(", stoveType=").append(stoveType);
        sb.append(", batchCode=").append(batchCode);
        sb.append(", testTime=").append(testTime);
        sb.append(", testTimeEnd=").append(testTimeEnd);
        sb.append(", testNumber=").append(testNumber);
        sb.append(", testResult=").append(testResult);
        sb.append(", uploadName=").append(uploadName);
        sb.append(", batchId=").append(batchId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}