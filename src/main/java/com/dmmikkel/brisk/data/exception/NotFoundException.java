package com.dmmikkel.brisk.data.exception;

public class NotFoundException
        extends ClientException
{
    public NotFoundException(String message)
    {
        super(message);
    }

    public NotFoundException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
