package com.dmmikkel.brisk.core.handler.image;

import com.dmmikkel.brisk.core.handler.Handler;
import com.dmmikkel.brisk.data.exception.NotFoundException;
import com.dmmikkel.brisk.data.model.Image;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.imgscalr.Scalr;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

public class ImageRequestHandler
        extends Handler
{
    private Logger logger = LoggerFactory.getLogger(ImageRequestHandler.class);

    @Override
    public void handle()
    {
        try
        {
            doHandle();
        }
        catch (NotFoundException e)
        {
            context.response.setStatus(404);
            e.printStackTrace();
        }
        catch (Exception e)
        {
            context.response.setStatus(500);
            e.printStackTrace();
        }
    }

    private void doHandle()
            throws Exception
    {
        Cache imageCache = context.cmsContext.cacheManager.getCache("imagecache");
        String cacheKey = context.request.getPathInfo();
        byte[] output;

        Element imageElement;
        if ((imageElement = imageCache.get(cacheKey)) != null)
        {
            output = (byte[]) imageElement.getObjectValue();
        } else {
            output = getImageBytes(cacheKey);
            imageCache.put(new Element(cacheKey, output));
        }

        int cacheSeconds = context.cmsContext.settings.imagesBrowserCacheTime;

        context.response.setContentType(context.request.getServletContext().getMimeType(cacheKey));
        context.response.addHeader("Cache-Control", String.format("max-age=%s, public", cacheSeconds));
        context.response.addDateHeader("Expires", DateTime.now().plusSeconds(cacheSeconds).getMillis());
        context.response.setContentLength(output.length);

        context.response.getOutputStream().write(output);

        if (output.length > 512000)
        {
            logger.info("Returned image is very large: " + context.request.getPathInfo() + " (" + output.length + ")");
        }
    }

    /**
     * Retrieves the image using the key from the URI and crops, scales and converts it to either JPEG or PNG.
     * Will get the image from cache if available.
     *
     * @param uri The URI of the image. Format: /_image/{width}/{ratio}/{quality}/{key}.{format}
     * @return The generated JPEG or PNG.
     * @throws Exception
     */
    private byte[] getImageBytes(String uri)
            throws Exception
    {
        ImageRequest imageRequest = new ImageRequest(uri);

        Image image = client.getImage(imageRequest.getKey());

        BufferedImage bufferedImage = ImageIO.read(new File(context.cmsContext.imagesDirectory, image.filename));
        BufferedImage newImage = toWidth(toRatio(bufferedImage, imageRequest.getRatio()), imageRequest.getWidth());

        // We first save the image bytes to memory
        // This helps with both caching and avoiding chunked transfer method
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        if (imageRequest.getFormat().equals("png"))
            writePng(newImage, outputStream);
        else if (imageRequest.getFormat().equals("jpg"))
            writeJpeg(newImage, outputStream, imageRequest.getQuality());

        return outputStream.toByteArray();
    }

    /**
     * Writes an image to an OutputStream as a JPEG with a specific quality
     *
     * @param image The image
     * @param outputStream The OutputStream to write the image to
     * @param quality The quality of the JPG
     * @throws IOException
     */
    private void writeJpeg(BufferedImage image, OutputStream outputStream, float quality)
            throws IOException
    {
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpeg");
        ImageWriter writer = writers.next();

        ImageWriteParam imageWriteParam = writer.getDefaultWriteParam();
        imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        imageWriteParam.setCompressionQuality(quality);

        ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(outputStream);
        writer.setOutput(imageOutputStream);
        writer.write(null, new IIOImage(image, null, null), imageWriteParam);
    }

    /**
     * Writes an image to an OutputStream as a PNG
     *
     * @param image The image
     * @param outputStream The OutputStream to write the image to
     * @throws IOException
     */
    private void writePng(BufferedImage image, OutputStream outputStream)
            throws IOException
    {
        ImageIO.write(image, "png", outputStream);
    }

    /**
     * Returns a new image scaled and cropped to the specific width/height ratio
     *
     * @param image
     * @param ratio
     * @return
     */
    private BufferedImage toRatio(BufferedImage image, float ratio)
    {
        if (ratio == 0f)
            return image;

        int width = image.getWidth();
        int height = image.getHeight();

        float oldRatio = (float) width / (float) height;

        if (ratio < oldRatio) // Original is wider
        {
            int newWidth = Math.round((float) height * ratio);
            int x = Math.round((float) (width - newWidth) / 2f); // Crop away left and right edges
            return Scalr.crop(image, x, 0, newWidth, height);
        } else
        { // Original is taller
            int newHeight = Math.round((float) width / ratio);
            int y = Math.round((float) (height - newHeight) / 2f); // Crop away top and bottom edges
            return Scalr.crop(image, 0, y, width, newHeight);
        }
    }

    /**
     * Returns a new image scaled down to fit the given width
     *
     * @param image
     * @param width
     * @return
     */
    private BufferedImage toWidth(BufferedImage image, int width)
    {
        int maxWidth = context.cmsContext.settings.imageMaxWidth;

        if (width > maxWidth)
            width = maxWidth;

        if (width > image.getWidth())
            return image;

        return Scalr.resize(image, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_TO_WIDTH, width);
    }
}
