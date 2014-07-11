package com.dmmikkel.brisk.admin.controller;

import com.dmmikkel.brisk.data.Client;
import com.dmmikkel.brisk.data.jdbc.JdbcClient;
import com.dmmikkel.brisk.data.model.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/pages")
public class PageController
{

    /**
     * Retrieves a list of all pages
     * @param request Current request
     * @return List of pages
     */
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public List<Page> getAll(HttpServletRequest request)
    {
        Client client = new JdbcClient();
        return client.getPages("site_1");
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public Page getAll(HttpServletRequest request, @RequestBody Page page)
    {
        Client client = new JdbcClient();
        client.savePage(page);
        return client.getPage(page.getSiteKey(), page.getKey());
    }

    /**
     * Retrieves a single page by pageKey
     * @param request Current request
     * @param pageKey The key of the page to retrieve
     * @return A page
     */
    @RequestMapping(value = "/{pageKey}", method = RequestMethod.GET, produces = "application/json")
    public Page getPage(HttpServletRequest request, @PathVariable String pageKey)
    {
        Client client = new JdbcClient();
        return client.getPage("site_1", pageKey);
    }

    /**
     * Update existing page
     * @param request Current request
     * @param page The updated page to be stored
     * @return A page
     */
    @RequestMapping(value = "/{pageKey}", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public Page savePage(HttpServletRequest request, @RequestPart String pageKey, @RequestBody Page page)
    {
        if (pageKey != page.getKey());

        Client client = new JdbcClient();
        client.savePage(page);
        return page;
    }
}
