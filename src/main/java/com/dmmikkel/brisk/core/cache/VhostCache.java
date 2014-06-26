package com.dmmikkel.brisk.core.cache;

import com.dmmikkel.brisk.data.Client;
import com.dmmikkel.brisk.data.exception.NotFoundException;
import com.dmmikkel.brisk.data.model.Site;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class VhostCache
{
    private final ConcurrentHashMap<String, Site>   siteCache  = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> hostCache  = new ConcurrentHashMap<>();
    private       DateTime                          lastUpdate = new DateTime(1900, 1, 1, 1, 1);
    private final int lifeTime;
    private Logger logger = LoggerFactory.getLogger(VhostCache.class);

    public VhostCache(int lifeTime)
    {
        this.lifeTime = lifeTime;
    }

    public Site hostLookup(String host)
    {
        String siteKey = hostCache.get(host);

        if (siteKey == null)
            throw new NotFoundException(String.format("Host %s was not found in vhost cache.", host));

        Site site = siteCache.get(siteKey);

        if (site == null)
            throw new NotFoundException(String.format("Site %s was not found in vhost cache.", siteKey));

        return site;
    }

    public boolean isExpired()
    {
        return DateTime.now().isAfter(lastUpdate.plusSeconds(lifeTime));
    }

    public void performUpdate(Client client)
    {
        Map<String, String> hosts = client.getDomains();
        Map<String, Site> sites = client.getSites();

        rebuildCaches(hosts, sites);
    }

    private synchronized void rebuildCaches(Map<String, String> hosts, Map<String, Site> sites)
    {
        lastUpdate = DateTime.now();

        siteCache.clear();
        hostCache.clear();

        hostCache.putAll(hosts);
        siteCache.putAll(sites);

        logger.info("VhostCache rebuilt.");
    }
}
