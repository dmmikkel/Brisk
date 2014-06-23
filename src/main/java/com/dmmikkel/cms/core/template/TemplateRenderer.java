package com.dmmikkel.cms.core.template;

public interface TemplateRenderer
{
    public String render(TemplateContext context)
            throws Exception;
}
