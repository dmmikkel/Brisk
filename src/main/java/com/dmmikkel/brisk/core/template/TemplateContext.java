package com.dmmikkel.brisk.core.template;

import com.dmmikkel.brisk.core.CMSContext;
import com.dmmikkel.brisk.data.model.ContentCollection;
import com.dmmikkel.brisk.data.model.Page;
import com.dmmikkel.brisk.data.model.Site;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

public class TemplateContext
{
    public Site                site;
    public Page                page;
    public ContentCollection   contents;
    public CMSContext          cmsContext;
    public HttpServletResponse response;
    public HttpServletRequest  request;
    public File                siteTemplateDirectory;
}
