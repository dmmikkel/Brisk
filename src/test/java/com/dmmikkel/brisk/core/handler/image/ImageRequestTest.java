package com.dmmikkel.brisk.core.handler.image;

import org.junit.Assert;
import org.junit.Test;

public class ImageRequestTest
{
    @Test
    public void getWidth_FromURI()
            throws Exception
    {
        ImageRequest imageRequest = new ImageRequest("/_image/400/2.5/0.5/something.jpg");

        Assert.assertEquals(400, imageRequest.getWidth());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getWidth_Zero()
            throws Exception
    {
        new ImageRequest("/_image/0/2.5/0.5/something.jpg");
    }

    @Test
    public void getRatio_FromURI()
            throws Exception
    {
        ImageRequest imageRequest = new ImageRequest("/_image/400/2.5/0.5/something.jpg");

        Assert.assertEquals(2.5f, imageRequest.getRatio(), 0.01f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getRatio_HeightTooSmall()
            throws Exception
    {
        new ImageRequest("/_image/10/0.05/0.5/something.jpg");
    }

    @Test
    public void getQuality_FromURI()
            throws Exception
    {
        ImageRequest imageRequest = new ImageRequest("/_image/400/2.5/0.5/something.jpg");

        Assert.assertEquals(0.5f, imageRequest.getQuality(), 0.01f);
    }

    @Test
    public void getKey_FromURI()
            throws Exception
    {
        ImageRequest imageRequest = new ImageRequest("/_image/400/2.5/0.5/something.jpg");

        Assert.assertEquals("something", imageRequest.getKey());
    }

    @Test
    public void getFormat_FromURI()
            throws Exception
    {
        ImageRequest imageRequest = new ImageRequest("/_image/400/2.5/0.5/something.jpg");

        Assert.assertEquals("jpg", imageRequest.getFormat());
    }
}
