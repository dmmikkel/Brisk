package com.dmmikkel.cms.core.handler;

import com.dmmikkel.cms.core.CMSContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HandlerContext
{
    public final HttpServletRequest  request;
    public final HttpServletResponse response;
    public final CMSContext          cmsContext;
    public final StatsEntry          statsEntry;

    public HandlerContext(HttpServletRequest request, HttpServletResponse response, CMSContext cmsContext)
    {
        this.request = request;
        this.response = response;
        this.cmsContext = cmsContext;
        this.statsEntry = new StatsEntry();
    }
}
