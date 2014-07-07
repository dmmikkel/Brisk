package com.dmmikkel.brisk.core.template.thymeleaf;

import org.thymeleaf.TemplateProcessingParameters;
import org.thymeleaf.resourceresolver.FileResourceResolver;
import org.thymeleaf.resourceresolver.IResourceResolver;
import org.thymeleaf.templateresolver.AbstractTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolutionValidity;
import org.thymeleaf.templateresolver.NonCacheableTemplateResolutionValidity;

import java.io.File;

public class SiteTemplateResolver
        extends AbstractTemplateResolver
{
    private static NonCacheableTemplateResolutionValidity nonCacheableTemplateResolutionValidity = new NonCacheableTemplateResolutionValidity();

    private String prefix;
    private String suffix;

    @Override
    protected String computeResourceName(TemplateProcessingParameters templateProcessingParameters)
    {
        SiteContext siteContext = (SiteContext)templateProcessingParameters.getContext();
        return getPrefix() + siteContext.getSiteKey() + File.separator + "templates" + File.separator + templateProcessingParameters.getTemplateName() + getSuffix();
    }

    @Override
    protected IResourceResolver computeResourceResolver(TemplateProcessingParameters templateProcessingParameters)
    {
        return new FileResourceResolver();
    }

    @Override
    protected String computeCharacterEncoding(TemplateProcessingParameters templateProcessingParameters)
    {
        return "UTF-8";
    }

    @Override
    protected String computeTemplateMode(TemplateProcessingParameters templateProcessingParameters)
    {
        return "HTML5";
    }

    @Override
    protected ITemplateResolutionValidity computeValidity(TemplateProcessingParameters templateProcessingParameters)
    {
        return nonCacheableTemplateResolutionValidity;
    }

    public String getPrefix()
    {
        return prefix;
    }

    public void setPrefix(String prefix)
    {
        this.prefix = prefix;
    }

    public String getSuffix()
    {
        return suffix;
    }

    public void setSuffix(String suffix)
    {
        this.suffix = suffix;
    }
}
