package com.dmmikkel.brisk.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Settings
{
    /**
     * Number of seconds images should be cached in browser
     */
    public final int imagesBrowserCacheTime;

    /**
     * Number of seconds pages should be cached in browser
     */
    public final int pagesBrowserCacheTime;

    /**
     * Maximum number of images stored in memory
     */
    public final int imageCacheMaxHeapEntries;

    /**
     * Maximum number of images cached on disk
     */
    public final int imageCacheMaxDiskEntries;

    /**
     * Number of seconds until an image is expelled from cache if it hasn't been accessed
     */
    public final int imageCacheTimeToIdle;

    /**
     * Maximum number of seconds an image can live in cache
     */
    public final int imageCacheTimeToLive;

    public final int pageCacheMaxHeapEntries;
    public final int pageCacheMaxDiskEntries;
    public final int pageCacheTimeToIdle;
    public final int pageCacheTimeToLive;

    public final int vhostCacheLifeTime;

    /**
     * The maximum width of images served by the CMS.
     * A low amount can help page load times and server cache size.
     */
    public final int imageMaxWidth;
    public final int resourcesBrowserCacheTime;

    public Settings(File cmsProperties)
    {
        Properties p = new Properties();
        try
        {
            p.load(new FileInputStream(cmsProperties));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        imagesBrowserCacheTime = p.getProperty("brisk.cache.images.browser") == null ? 0 : Integer.parseInt(p.getProperty("brisk.cache.images.browser"));
        pagesBrowserCacheTime = p.getProperty("brisk.cache.pages.browser") == null ? 0 : Integer.parseInt(p.getProperty("brisk.cache.pages.browser"));
        resourcesBrowserCacheTime = p.getProperty("brisk.cache.resources.browser") == null ? 0 : Integer.parseInt(p.getProperty("brisk.cache.resources.browser"));

        imageCacheMaxHeapEntries = p.getProperty("brisk.cache.images.maxentriesheap") == null ? 100 : Integer.parseInt(p.getProperty("brisk.cache.images.maxentriesheap"));
        imageCacheMaxDiskEntries = p.getProperty("brisk.cache.images.maxentriesdisk") == null ? 500 : Integer.parseInt(p.getProperty("brisk.cache.images.maxentriesdisk"));
        imageCacheTimeToIdle = p.getProperty("brisk.cache.images.timetoidle") == null ? 10 : Integer.parseInt(p.getProperty("brisk.cache.images.timetoidle"));
        imageCacheTimeToLive = p.getProperty("brisk.cache.images.timetolive") == null ? 10 : Integer.parseInt(p.getProperty("brisk.cache.images.timetolive"));

        pageCacheMaxHeapEntries = p.getProperty("brisk.cache.pages.maxentriesheap") == null ? 1000 : Integer.parseInt(p.getProperty("brisk.cache.pages.maxentriesheap"));
        pageCacheMaxDiskEntries = p.getProperty("brisk.cache.pages.maxentriesdisk") == null ? 10000 : Integer.parseInt(p.getProperty("brisk.cache.pages.maxentriesdisk"));
        pageCacheTimeToIdle = p.getProperty("brisk.cache.pages.timetoidle") == null ? 10 : Integer.parseInt(p.getProperty("brisk.cache.pages.timetoidle"));
        pageCacheTimeToLive = p.getProperty("brisk.cache.pages.timetolive") == null ? 10 : Integer.parseInt(p.getProperty("brisk.cache.pages.timetolive"));

        vhostCacheLifeTime = p.getProperty("brisk.cache.vhost.lifetime") == null ? 60 : Integer.parseInt(p.getProperty("brisk.cache.vhost.lifetime"));

        imageMaxWidth = p.getProperty("brisk.images.maxwidth") == null ? 0 : Integer.parseInt(p.getProperty("brisk.images.maxwidth"));
    }


}
