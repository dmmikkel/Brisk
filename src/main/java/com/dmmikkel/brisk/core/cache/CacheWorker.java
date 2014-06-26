package com.dmmikkel.brisk.core.cache;

import com.dmmikkel.brisk.core.CMSContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CacheWorker runs a background task updating the cache at regular intervals.
 */
public class CacheWorker
        implements Runnable
{
    private Logger logger = LoggerFactory.getLogger(CacheWorker.class);
    private volatile boolean running = true;
    private final CMSContext cmsContext;

    public CacheWorker(CMSContext cmsContext)
    {
        this.cmsContext = cmsContext;
        doUpdateVhostCache();
    }

    public void terminate()
    {
        running = false;
    }

    @Override
    public void run()
    {
        logger.info("CacheWorker Thread Started.");

        while (running)
        {

            if (cmsContext.vhostCache.isExpired())
                doUpdateVhostCache();

            try
            {
                Thread.sleep(1000l);
            }
            catch (InterruptedException e)
            {
                logger.error("Exception", e);
                running = false;
            }
        }

        logger.info("CacheWorker Thread Stopped.");
    }

    private void doUpdateVhostCache()
    {
        cmsContext.vhostCache.performUpdate(cmsContext.client);
    }
}
