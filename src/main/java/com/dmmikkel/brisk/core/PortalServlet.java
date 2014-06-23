package com.dmmikkel.brisk.core;

import com.dmmikkel.brisk.core.handler.HandlerContext;
import com.dmmikkel.brisk.data.ClientFactory;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.DiskStoreConfiguration;
import net.sf.ehcache.config.PersistenceConfiguration;

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

    public PortalServlet()
            throws IOException
    {
        homeDirectory = new File("/Users/dmi/Documents/Labs/TestCMSTemplates");
        configDirectory = new File(homeDirectory, "conf");

        if (!homeDirectory.exists())
            throw new FileNotFoundException("Could not find brisk home directory.");

        cmsContext = new CMSContext();
        cmsContext.settings = new Settings(new File(configDirectory, "brisk.properties"));
        cmsContext.homeDirectory = homeDirectory;
        cmsContext.sitesDirectory = new File(homeDirectory, "sites");
        cmsContext.imagesDirectory = new File(homeDirectory, "images");

        DiskStoreConfiguration diskStoreConfiguration = new DiskStoreConfiguration().path(new File(homeDirectory, "cache").getPath());
        Configuration configuration = new Configuration().name("cmscache").diskStore(diskStoreConfiguration);
        cmsContext.cacheManager = CacheManager.create(configuration);
        cmsContext.cacheManager.addCache(createImageCache("imagecache", cmsContext.settings));
        cmsContext.cacheManager.addCache(createPageCache("pagecache", cmsContext.settings));
        cmsContext.cacheManager.addCache(createVhostCache("vhostcache", cmsContext.settings));
        cmsContext.client = ClientFactory.create();
    }

    private Cache createImageCache(String name, Settings settings)
    {
        PersistenceConfiguration persistenceConfiguration = new PersistenceConfiguration().strategy(PersistenceConfiguration.Strategy.LOCALTEMPSWAP);

        CacheConfiguration imageCacheConfig = new CacheConfiguration().name(name).maxEntriesLocalHeap(settings.imageCacheMaxHeapEntries).maxEntriesLocalDisk(settings.imageCacheMaxDiskEntries).timeToIdleSeconds(settings.imageCacheTimeToIdle).timeToLiveSeconds(settings.imageCacheTimeToLive).persistence(persistenceConfiguration);

        return new Cache(imageCacheConfig);
    }

    private Cache createPageCache(String name, Settings settings)
    {
        PersistenceConfiguration persistenceConfiguration = new PersistenceConfiguration().strategy(PersistenceConfiguration.Strategy.LOCALTEMPSWAP);

        CacheConfiguration imageCacheConfig = new CacheConfiguration().name(name).maxEntriesLocalHeap(settings.pageCacheMaxHeapEntries).maxEntriesLocalDisk(settings.pageCacheMaxDiskEntries).timeToIdleSeconds(settings.pageCacheTimeToIdle).timeToLiveSeconds(settings.pageCacheTimeToLive).persistence(persistenceConfiguration);

        return new Cache(imageCacheConfig);
    }

    private Cache createVhostCache(String name, Settings settings)
    {
        PersistenceConfiguration persistenceConfiguration = new PersistenceConfiguration().strategy(PersistenceConfiguration.Strategy.LOCALTEMPSWAP);

        CacheConfiguration imageCacheConfig = new CacheConfiguration().name(name).maxEntriesLocalHeap(settings.vhostCacheMaxHeapEntries).maxEntriesLocalDisk(settings.vhostCacheMaxDiskEntries).timeToIdleSeconds(settings.vhostCacheTimeToIdle).timeToLiveSeconds(settings.vhostCacheTimeToLive).persistence(persistenceConfiguration);

        return new Cache(imageCacheConfig);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        HandlerContext handlerContext = new HandlerContext(request, response, cmsContext);

        Router router = new Router();
        router.process(handlerContext);
    }
}
