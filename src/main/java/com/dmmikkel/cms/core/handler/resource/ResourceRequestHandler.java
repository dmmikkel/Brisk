package com.dmmikkel.cms.core.handler.resource;

import com.dmmikkel.cms.core.handler.Handler;
import com.dmmikkel.cms.data.model.Site;
import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;

import java.io.File;
import java.io.FileInputStream;

public class ResourceRequestHandler
        extends Handler
{

    @Override
    public void handle()
    {
        try
        {
            doHandle();
        }
        catch (Exception e)
        {
            // Something went wrong while handling the request
            e.printStackTrace();
            context.response.setStatus(500);
        }
    }

    private void doHandle()
            throws Exception
    {
        Site site = getSite();
        File siteDirectory = getSiteDirectory(site);
        File resourceDirectory = new File(siteDirectory, context.cmsContext.resourceDirectoryName);

        String filename = context.request.getPathInfo().replaceAll("^/_public/", "");
        File file = new File(resourceDirectory, filename);

        if (!resourceDirectory.exists())
            throw new Exception("Resource directory not found. " + resourceDirectory.getAbsoluteFile());

        if (file.exists())
        {
            FileInputStream inputStream = new FileInputStream(file);

            int cacheSeconds = context.cmsContext.settings.resourcesBrowserCacheTime;

            String mime = context.request.getServletContext().getMimeType(file.getName());
            context.response.setContentType(mime);
            context.response.addHeader("Cache-Control", String.format("max-age=%s, public", cacheSeconds));
            context.response.addDateHeader("Expires", DateTime.now().plusSeconds(cacheSeconds).getMillis());

            IOUtils.copy(inputStream, context.response.getOutputStream());
        } else
        {
            render404();
        }
    }
}
