package com.dmmikkel.cms.core.handler;

public class ContentNotFoundException
        extends RuntimeException
{
    public ContentNotFoundException(String content)
    {
        super(content);
    }
}
