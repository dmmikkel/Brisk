package com.dmmikkel.cms.core.handler.image;

public class ImageRequest
{
    private int width = 100;
    private float ratio = 0f;
    private float quality;
    private String key;
    private String format;

    public ImageRequest(String uri)
    {
        if (uri == null)
            throw new IllegalArgumentException("URI can't be null.");

        String[] urlParts = uri.replaceFirst("^/", "") // Remove any leading '/'
                .replaceFirst("/$", "") // Remove any trailing '/'
                .split("/");

        if (urlParts.length != 5)
            throw new IllegalArgumentException("URI must have 5 parts. Actual: " + urlParts.length);

        setWidth(Integer.parseInt(urlParts[1]));
        setRatio(Float.parseFloat(urlParts[2]));
        setQuality(Float.parseFloat(urlParts[3]));

        String imageName = urlParts[4];

        if (imageName.length() < 5)
            throw new IllegalArgumentException("Image name part is too short.");

        setFormat(imageName.substring(imageName.length() - 3, imageName.length()));
        setKey(imageName.substring(0, imageName.length() - 4));
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        if (width < 0)
            throw new IllegalArgumentException("Width must be larger than 0.");
        if (getRatio() != 0f && (float)width * getRatio() < 1f)
            throw new IllegalArgumentException("The given width and ratio generated a height less than 1.");
        this.width = width;
    }

    public float getRatio()
    {
        return ratio;
    }

    public void setRatio(float ratio)
    {
        if (ratio != 0f && (float)getWidth() * ratio < 1f)
            throw new IllegalArgumentException("The given width and ratio generated a height less than 1.");
        this.ratio = ratio;
    }

    public float getQuality()
    {
        return quality;
    }

    public void setQuality(float quality)
    {
        if (quality < 0f || quality > 1f)
            throw new IllegalArgumentException("Quality must be between 0 and 1.");
        this.quality = quality;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public String getFormat()
    {
        return format;
    }

    public void setFormat(String format)
    {
        if (format.equals("jpg") || format.equals("png"))
            this.format = format;
        else
            throw new IllegalArgumentException("Format must be jpg or png. Actual: " + format);
    }
}
