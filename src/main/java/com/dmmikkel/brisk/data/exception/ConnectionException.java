package com.dmmikkel.brisk.data.exception;

public class ConnectionException
        extends ClientException
{
    public ConnectionException(String message)
    {
        super(message);
    }

    public ConnectionException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
