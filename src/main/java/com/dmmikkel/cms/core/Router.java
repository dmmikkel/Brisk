package com.dmmikkel.cms.core;

import com.dmmikkel.cms.core.handler.*;
import com.dmmikkel.cms.core.handler.image.ImageRequestHandler;
import com.dmmikkel.cms.core.handler.page.PageRequestHandler;
import com.dmmikkel.cms.core.handler.resource.ResourceRequestHandler;

import javax.servlet.http.HttpServletRequest;

public class Router
{
    public void process(HandlerContext handlerContext)
    {
        Handler handler = getHandler(handlerContext.request);
        handler.init(handlerContext);
        handler.handle();
    }

    protected Handler getHandler(final HttpServletRequest request)
    {
        final String uri = request.getPathInfo();

        if (uri.startsWith("/_public/"))
            return new ResourceRequestHandler();
        else if (uri.startsWith("/_image/"))
            return new ImageRequestHandler();
        else
            return new PageRequestHandler();
    }
}
