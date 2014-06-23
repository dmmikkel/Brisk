package com.dmmikkel.cms.admin.controller;

import com.dmmikkel.cms.data.model.Page;
import com.dmmikkel.cms.data.model.Site;

import java.util.HashMap;
import java.util.Map;

public class PageController
    extends Controller
{
    public Map<String, Object> getIndex()
    {
        Site site = client.getSite("demo");
        Map<String, Object> result = new HashMap<>();
        result.put("pages", client.getPages(site.key));
        return result;
    }

    public Map<String, Object> postNew()
    {
        Site site = client.getSite("demo");
        Map<String, Object> result = new HashMap<>();

        Page page = new Page();
        // TODO do things with page
        try {
            client.savePage(site.key, page);
            result.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("exception", e.toString());
        }
        return result;
    }
}
