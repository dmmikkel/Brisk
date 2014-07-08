package com.dmmikkel.brisk.core;

import com.dmmikkel.brisk.core.cache.CacheWorker;
import com.dmmikkel.brisk.core.cache.VhostCache;
import com.dmmikkel.brisk.core.handler.HandlerContext;
import com.dmmikkel.brisk.data.ClientFactory;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.DiskStoreConfiguration;
import net.sf.ehcache.config.PersistenceConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PortalServlet
        extends HttpServlet
{
    private CMSContext cmsContext;
    private File       homeDirectory;
    private File       configDirectory;
    private Logger     logger = LoggerFactory.getLogger(PortalServlet.class);

    public PortalServlet()
            throws IOException
    {
        logger.info("Brisk CMS started!");

        homeDirectory = new File("/Users/dmi/Documents/Labs/TestCMSTemplates");
        configDirectory = new File(homeDirectory, "conf");

        if (!homeDirectory.exists())
            throw new FileNotFoundException("Could not find brisk home directory.");

        cmsContext = new CMSContext();
        cmsContext.settings = new Settings(new File(configDirectory, "cms.properties"));
        cmsContext.homeDirectory = homeDirectory;
        cmsContext.sitesDirectory = new File(homeDirectory, "sites");
        cmsContext.imagesDirectory = new File(homeDirectory, "images");

        // Cache setup
        cmsContext.vhostCache = new VhostCache(cmsContext.settings.vhostCacheLifeTime);

        DiskStoreConfiguration diskStoreConfiguration = new DiskStoreConfiguration().path(new File(homeDirectory, "cache").getPath());
        Configuration configuration = new Configuration().name("cmscache").diskStore(diskStoreConfiguration);
        cmsContext.cacheManager = CacheManager.create(configuration);
        cmsContext.cacheManager.addCache(createImageCache("imagecache", cmsContext.settings));
        cmsContext.cacheManager.addCache(createPageCache("pagecache", cmsContext.settings));
        cmsContext.client = ClientFactory.create();


    }

    @Override
    public void init(ServletConfig config)
            throws ServletException
    {
        super.init(config);

        // Start cache thread
        CacheWorker cacheWorker = new CacheWorker(cmsContext);
        new Thread(cacheWorker, "cacheworker").start();
        config.getServletContext().setAttribute("cacheworker", cacheWorker);
    }

    @Override
    public void destroy()
    {
        CacheWorker cacheWorker = (CacheWorker)getServletContext().getAttribute("cacheworker");
        cacheWorker.terminate();

        cmsContext.cacheManager.shutdown();

        super.destroy();
    }

    private Cache createImageCache(String name, Settings settings)
    {
        PersistenceConfiguration persistenceConfiguration = new PersistenceConfiguration().strategy(PersistenceConfiguration.Strategy.NONE);

        CacheConfiguration imageCacheConfig = new CacheConfiguration().name(name).statistics(false).maxEntriesLocalHeap(settings.imageCacheMaxHeapEntries).maxEntriesLocalDisk(settings.imageCacheMaxDiskEntries).timeToIdleSeconds(settings.imageCacheTimeToIdle).timeToLiveSeconds(settings.imageCacheTimeToLive).persistence(persistenceConfiguration);

        return new Cache(imageCacheConfig);
    }

    private Cache createPageCache(String name, Settings settings)
    {
        PersistenceConfiguration persistenceConfiguration = new PersistenceConfiguration().strategy(PersistenceConfiguration.Strategy.NONE);

        CacheConfiguration imageCacheConfig = new CacheConfiguration().name(name).statistics(false).maxEntriesLocalHeap(settings.pageCacheMaxHeapEntries).maxEntriesLocalDisk(settings.pageCacheMaxDiskEntries).timeToIdleSeconds(settings.pageCacheTimeToIdle).timeToLiveSeconds(settings.pageCacheTimeToLive).persistence(persistenceConfiguration);

        return new Cache(imageCacheConfig);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        logger.info(request.getPathInfo());

        HandlerContext handlerContext = new HandlerContext(request, response, cmsContext);

        Router router = new Router();
        router.process(handlerContext);
    }
}
