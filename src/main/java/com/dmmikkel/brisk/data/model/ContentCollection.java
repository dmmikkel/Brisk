package com.dmmikkel.brisk.data.model;

import java.util.Iterator;
import java.util.List;

public class ContentCollection
        implements Iterable<Content>
{
    public List<Content> contents;
    public int           count;
    public int           page;
    public int           totalPages;

    @Override
    public Iterator<Content> iterator()
    {
        return contents.iterator();
    }
}
