package com.dmmikkel.brisk.core.template;

public interface TemplateRenderer
{
    public String render(TemplateContext context)
            throws Exception;
}
