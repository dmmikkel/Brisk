package com.dmmikkel.brisk.admin;

import com.dmmikkel.brisk.admin.controller.PageController;
import com.dmmikkel.brisk.data.Client;
import com.dmmikkel.brisk.data.ClientFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class Router
{
    public Map<String, Object> route(HttpServletRequest request)
    {
        Client client = ClientFactory.create();

        if (request.getPathInfo().equals("/page"))
        {
            PageController controller = (PageController)(new PageController().withClient(client));
            return controller.getIndex();
        }

        return new HashMap<>();
    }
}
