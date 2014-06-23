package com.dmmikkel.brisk.data;

public class ClientFactory
{
    public static Client create()
    {
        return new PostgresqlClient();
    }
}
