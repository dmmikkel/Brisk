package com.dmmikkel.brisk.data;

import com.dmmikkel.brisk.data.exception.ClientException;
import com.dmmikkel.brisk.data.exception.ConnectionException;
import com.dmmikkel.brisk.data.exception.NotFoundException;
import com.dmmikkel.brisk.data.model.ContentCollection;
import com.dmmikkel.brisk.data.model.Image;
import com.dmmikkel.brisk.data.model.Page;
import com.dmmikkel.brisk.data.model.Site;

import java.util.List;
import java.util.Map;

public interface Client
{
    /* Site */

    Site getSiteFromDomain(String domain)
            throws ClientException;

    Map<String, Site> getSites()
            throws ClientException;

    Site getSite(String siteKey)
            throws ClientException;

    /* Domain */

    Map<String, String> getDomains()
            throws ClientException;

    /* Page */

    List<Page> getPages(String siteKey)
            throws ClientException;

    Page getPage(String siteKey, String pageKey)
            throws ClientException;

    void savePage(Page page)
            throws ClientException;

    /* Image */

    Image getImage(String imageKey)
            throws ClientException;

    /* Content */

    ContentCollection getContentByKey(String siteKey, String key)
            throws ConnectionException, NotFoundException;

    ContentCollection getContentByType(String siteKey, String contentType, String orderBy, String orderDirection, int count, int page)
            throws ConnectionException, NotFoundException;
}
