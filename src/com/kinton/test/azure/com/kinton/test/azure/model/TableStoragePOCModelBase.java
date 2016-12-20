package com.kinton.test.azure.com.kinton.test.azure.model;

import com.microsoft.azure.storage.table.TableServiceEntity;
import com.yum.boh.core.DateFormats;
import com.yum.boh.core.model.ModelBase;
import org.apache.struts2.json.annotations.JSON;

import java.util.*;

/**
 * Created by mgkj1 on 11/24/2016.
 */
public class TableStoragePOCModelBase extends TableServiceEntity{
    private static final long serialVersionUID = 1L;

    /*
     * 转码使用的Map对象，里面存放转换后的码值
     * 转码来源于实体类的注解
     */
    private Map<String, String> code2Name = new HashMap<String, String>();

    /*
     * 被屏蔽的数据域
     */
    private Set<String> maskedField = new HashSet<String>();

    /* GUID(32位16进制字符外加4个连词符共36字符) */
    private String guid;

    /* 创建时间 */
    private Date createTime;

    /* 创建者GUID */
    private String createId;

    /* 更新时间 */
    private Date updateTime;

    /* 更新者GUID */
    private String updateId;

    /* 逻辑删除标志(Y/N) */
    private String delFlag;

    /**
     * @return the guid
     */
    public String getGuid()
    {
        return guid;
    }

    /**
     * @param guid the guid to set
     */
    public void setGuid(String guid)
    {
        this.guid = guid;
    }

    /**
     * @return the createTime
     */
    @JSON(format = DateFormats.TIME_FORMAT)
    public Date getCreateTime()
    {
        return createTime;
    }

    /**
     * @param createTime the createTime to set
     */
    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    /**
     * @return the createId
     */
    public String getCreateId()
    {
        return createId;
    }

    /**
     * @param createId the createId to set
     */
    public void setCreateId(String createId)
    {
        this.createId = createId;
    }

    /**
     * @return the updateDate
     */
    @JSON(format = DateFormats.TIME_FORMAT)
    public Date getUpdateTime()
    {
        return updateTime;
    }

    /**
     * @param updateTime the updateTime to set
     */
    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }

    /**
     * @return the updateId
     */
    public String getUpdateId()
    {
        return updateId;
    }

    /**
     * @param updateId the updateId to set
     */
    public void setUpdateId(String updateId)
    {

        this.updateId = updateId;
    }

    /**
     * @return the delFlag
     */
    public String getDelFlag()
    {
        return delFlag;
    }

    /**
     * @param delFlag the delFlag to set
     */
    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    /**
     * @return the maskedField
     */
    public Set<String> getMaskedField()
    {
        return maskedField;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("ModelBase [guid=");
        builder.append(guid);
        builder.append(", createTime=");
        builder.append(createTime);
        builder.append(", createId=");
        builder.append(createId);
        builder.append(", updateTime=");
        builder.append(updateTime);
        builder.append(", updateId=");
        builder.append(updateId);
        builder.append(", delFlag=");
        builder.append(delFlag);
        builder.append("]");
        return builder.toString();
    }

    /**
     * @return the code2Name
     */
    public Map<String, String> getCode2Name()
    {
        return code2Name;
    }

    /**
     * 为字段设置转码后的值
     * @param filed 字段名
     * @param value 转码后的值
     */
    public void putCodeValue(String filed, String value)
    {
        code2Name.put(filed, value);
    }

    /**
     * 屏蔽某数据域
     * @param field
     */
    public void mask(String field)
    {
        // step 1.加入屏蔽列表
        maskedField.add(field);
        //        code2Name.remove(field);
    }

    /**
     * 开放克隆方法
     *
     * @return 克隆对象
     *
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }
}
