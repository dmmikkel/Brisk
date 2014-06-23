package com.dmmikkel.cms.data;

public class ClientFactory
{
    public static Client create()
    {
        return new PostgresqlClient();
    }
}
