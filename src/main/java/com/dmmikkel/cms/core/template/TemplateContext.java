package com.dmmikkel.cms.core.template;

import com.dmmikkel.cms.core.CMSContext;
import com.dmmikkel.cms.data.model.ContentCollection;
import com.dmmikkel.cms.data.model.Page;
import com.dmmikkel.cms.data.model.Site;

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
