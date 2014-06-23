package com.dmmikkel.cms.core.handler.page;

import com.dmmikkel.cms.core.handler.ContentNotFoundException;
import com.dmmikkel.cms.core.handler.Handler;
import com.dmmikkel.cms.core.template.TemplateContext;
import com.dmmikkel.cms.core.template.TemplateRenderer;
import com.dmmikkel.cms.core.template.thymeleaf.ThymeleafTemplateRenderer;
import com.dmmikkel.cms.data.exception.NotFoundException;
import com.dmmikkel.cms.data.model.ContentCollection;
import com.dmmikkel.cms.data.model.Page;
import com.dmmikkel.cms.data.model.Site;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class PageRequestHandler
        extends Handler
{
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void handle()
    {
        try
        {
            doHandle();
        }
        catch (NotFoundException e)
        {
            e.printStackTrace();
            render404();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            context.response.setStatus(500);
        }
    }


    private void doHandle()
            throws Exception
    {
        Cache cache = context.cmsContext.cacheManager.getCache("pagecache");
        String cacheKey = context.request.getServerName() + context.request.getPathInfo();
        String html;

        // Return from cache if we find it there
        Element element;
        if ((element = cache.get(cacheKey)) != null)
        {
            context.statsEntry.pageCacheHit = true;
            html = (String) element.getObjectValue();
        } else
        {
            final String[] urlParts = parseUrlParts(context.request.getPathInfo());
            final String pageKey = (urlParts[0].equals("")) ? "index" : urlParts[0];
            final Site site = getSite();

            Page page = client.getPage(site.key, pageKey);

            final ContentCollection contents = getContent(site, page, urlParts);

            html = render(site, page, contents);
            cache.put(new Element(cacheKey, html));
        }

        int cacheSeconds = context.cmsContext.settings.pagesBrowserCacheTime;

        context.response.setContentType("text/html");
        context.response.setCharacterEncoding("UTF-8");
        context.response.addHeader("Cache-Control", String.format("max-age=%s, public", cacheSeconds));
        context.response.setDateHeader("Expires", DateTime.now().plusSeconds(cacheSeconds).getMillis());

        context.response.getWriter().write(html);

        savePageStats();
    }

    private String render(Site site, Page page, ContentCollection contents)
            throws Exception
    {
        // Build TemplateContext
        File siteDir = getSiteDirectory(site);
        TemplateContext templateContext = new TemplateContext();
        templateContext.cmsContext = context.cmsContext;
        templateContext.contents = contents;
        templateContext.site = site;
        templateContext.page = page;
        templateContext.request = context.request;
        templateContext.response = context.response;
        templateContext.siteTemplateDirectory = new File(siteDir, context.cmsContext.templateDirectoryName);

        // Render
        TemplateRenderer templateRenderer = new ThymeleafTemplateRenderer();
        return templateRenderer.render(templateContext);
    }

    private ContentCollection getContent(Site site, Page page, String[] urlParts)
            throws Exception
    {System.out.println(page.contentQueryMethod);
        ContentCollection contents;
        switch (page.contentQueryMethod)
        {
            case 0:
                return null;
            case 1: // getContentByKey
                if (page.contentKey != null)
                    contents = client.getContentByKey(site.key, page.contentKey);
                else if (urlParts.length > 1)
                {
                    contents = client.getContentByKey(site.key, urlParts[1]);
                    if (contents.contents.isEmpty())
                        throw new ContentNotFoundException("Site: " + site.key + " Content: " + urlParts[1]);
                } else
                    throw new Exception("ContentKey not found.");
                break;
            case 2: // getContentByContentType
                contents = client.getContentByType(site.key, page.contentType, page.orderBy, page.orderDirection, page.count, 1);
                break;
            default:
                contents = new ContentCollection();
                break;
        }
        return contents;
    }

    protected void savePageStats()
    {
        logger.info(String.format("[CACHE STATS] vhost = %s, page = %s", context.statsEntry.vhostCacheHit, context.statsEntry.pageCacheHit));
    }


}
