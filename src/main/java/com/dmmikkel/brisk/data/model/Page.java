package com.dmmikkel.brisk.data.model;

import java.io.Serializable;

public class Page
        implements Serializable
{
    private String key;
    private String name;

    private String masterTemplate;
    private String template;

    private int    contentQueryMethod;
    private String contentKey;
    private String contentType;
    private String orderBy;
    private String orderDirection;
    private int    count;

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getMasterTemplate()
    {
        return masterTemplate;
    }

    public void setMasterTemplate(String masterTemplate)
    {
        this.masterTemplate = masterTemplate;
    }

    public String getTemplate()
    {
        return template;
    }

    public void setTemplate(String template)
    {
        this.template = template;
    }

    public int getContentQueryMethod()
    {
        return contentQueryMethod;
    }

    public void setContentQueryMethod(int contentQueryMethod)
    {
        this.contentQueryMethod = contentQueryMethod;
    }

    public String getContentKey()
    {
        return contentKey;
    }

    public void setContentKey(String contentKey)
    {
        this.contentKey = contentKey;
    }

    public String getContentType()
    {
        return contentType;
    }

    public void setContentType(String contentType)
    {
        this.contentType = contentType;
    }

    public String getOrderBy()
    {
        return orderBy;
    }

    public void setOrderBy(String orderBy)
    {
        this.orderBy = orderBy;
    }

    public String getOrderDirection()
    {
        return orderDirection;
    }

    public void setOrderDirection(String orderDirection)
    {
        this.orderDirection = orderDirection;
    }

    public int getCount()
    {
        return count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }
}
