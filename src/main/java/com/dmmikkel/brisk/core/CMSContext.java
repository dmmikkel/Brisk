package com.dmmikkel.brisk.core;

import com.dmmikkel.brisk.data.Client;
import net.sf.ehcache.CacheManager;

import java.io.File;

public class CMSContext
{
    // TODO: Fetch this somewhow
    public File homeDirectory;
    public File sitesDirectory;
    public File imagesDirectory;
    public String templateDirectoryName = "templates";
    public String resourceDirectoryName = "resources";
    public CacheManager cacheManager;
    public Client       client;
    public Settings     settings;
}
