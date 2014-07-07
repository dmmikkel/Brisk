package com.dmmikkel.brisk.core.template.thymeleaf;

public class SiteContext
        extends org.thymeleaf.context.Context
{
    private String siteKey;

    public String getSiteKey()
    {
        return siteKey;
    }

    public void setSiteKey(String siteKey)
    {
        this.siteKey = siteKey;
    }
}
