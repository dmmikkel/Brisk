package com.dmmikkel.brisk.admin.controller;

import com.dmmikkel.brisk.data.Client;
import com.dmmikkel.brisk.data.model.Site;

public abstract class Controller
{
    protected Site site;
    protected Client client;

    public Controller withSite(Site site)
    {
        this.site = site;
        return this;
    }

    public Controller withClient(Client client)
    {
        this.client = client;
        return this;
    }
}
