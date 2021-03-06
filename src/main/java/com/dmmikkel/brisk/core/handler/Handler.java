package com.dmmikkel.brisk.core.handler;

import com.dmmikkel.brisk.data.Client;
import com.dmmikkel.brisk.data.model.Site;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public abstract class Handler
{
    private static String KEY_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_";

    protected Client         client;
    protected HandlerContext context;
    protected Logger logger = LoggerFactory.getLogger("CMS Handler");

    public final void init(HandlerContext context)
    {
        this.client = context.cmsContext.client;
        this.context = context;
    }

    protected Site getSite()
            throws Exception
    {
        final String http_host = context.request.getServerName();

        if (!isValidHostname(http_host))
            throw new Exception("Invalid characters in host name.");

        return context.cmsContext.vhostCache.hostLookup(http_host);
    }

    protected void render404()
    {
        try
        {
            context.response.setStatus(404);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            context.response.setStatus(500);
        }
    }

    protected File getSiteDirectory(Site site)
    {
        return new File(context.cmsContext.sitesDirectory, site.key);
    }

    protected String generateKey(int length)
    {
        if (length < 1)
            throw new IllegalArgumentException("Length must be 1 or more");

        if (length > 256)
            throw new IllegalArgumentException("Length can't be more than 256");

        StringBuilder stringBuilder = new StringBuilder(length);

        for (int i = 0; i < length; i++)
        {
            int ichar = (int) (Math.random() * KEY_CHARS.length());
            stringBuilder.append(KEY_CHARS.substring(ichar, ichar + 1));
        }

        return stringBuilder.toString();
    }

    protected boolean isValidHostname(final String host)
    {
        return host.matches("[a-z0-9\\-.]+");
    }

    protected String[] parseUrlParts(final String uri)
    {
        if (uri == null)
            return new String[]{""};
        return uri.replaceFirst("^/", "") // Remove any leading '/'
                .replaceFirst("/$", "") // Remove any trailing '/'
                .split("/");
    }

    public abstract void handle();
}
