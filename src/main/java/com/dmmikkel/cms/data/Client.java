package com.dmmikkel.cms.data;

import com.dmmikkel.cms.data.exception.ClientException;
import com.dmmikkel.cms.data.exception.ConnectionException;
import com.dmmikkel.cms.data.exception.NotFoundException;
import com.dmmikkel.cms.data.model.ContentCollection;
import com.dmmikkel.cms.data.model.Image;
import com.dmmikkel.cms.data.model.Page;
import com.dmmikkel.cms.data.model.Site;

import java.util.List;
import java.util.Map;

public interface Client
{
    Site getSiteFromDomain(String domain)
            throws ConnectionException, NotFoundException;

    Site getSite(String siteKey)
            throws ConnectionException, NotFoundException;

    Map<String, String> getDomains()
            throws ClientException;

    Map<String, Site> getSites()
            throws ClientException;

    Page getPage(String siteKey, String pageKey)
            throws ConnectionException, NotFoundException;

    Image getImage(String imageKey)
            throws ClientException;

    ContentCollection getContentByKey(String siteKey, String key)
            throws ConnectionException, NotFoundException;

    ContentCollection getContentByType(String siteKey, String contentType, String orderBy, String orderDirection, int count, int page)
            throws ConnectionException, NotFoundException;

    List<Page> getPages(String siteKey)
            throws ClientException;

    void savePage(String key, Page page)
            throws ClientException;
}
