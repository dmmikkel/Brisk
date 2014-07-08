package com.dmmikkel.brisk.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/pages")
public class PageController
{
    @RequestMapping(method = RequestMethod.GET)
    public String getAll()
    {
        return "pages/index";
    }
}
