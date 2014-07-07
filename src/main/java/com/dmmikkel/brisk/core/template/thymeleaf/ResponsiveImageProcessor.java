package com.dmmikkel.brisk.core.template.thymeleaf;

import com.dmmikkel.brisk.core.CMSContext;
import com.sun.imageio.plugins.common.ImageUtil;
import org.apache.commons.codec.binary.Base64;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.IElementNameProcessorMatcher;
import org.thymeleaf.processor.ProcessorResult;
import org.thymeleaf.processor.element.AbstractElementProcessor;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ResponsiveImageProcessor
        extends AbstractElementProcessor
{
    private CMSContext cmsContext;

    protected ResponsiveImageProcessor(IElementNameProcessorMatcher matcher, CMSContext cmsContext)
    {
        super(matcher);
        this.cmsContext = cmsContext;
    }

    protected ResponsiveImageProcessor(String elementName)
    {
        super(elementName);
    }

    @Override
    protected ProcessorResult processElement(Arguments arguments, Element element)
    {
        if (!element.hasNormalizedAttribute("img", "key"))
            return ProcessorResult.OK;

        String imageKey = element.getAttributeValueFromNormalizedName("img", "key");
        String format = element.getAttributeValueFromNormalizedName("img", "format");
        String strWidth = element.getAttributeValueFromNormalizedName("img", "width");
        String strRatio = element.getAttributeValueFromNormalizedName("img", "ratio");
        String quality = element.getAttributeValueFromNormalizedName("img", "quality");

        format = (format != null) ? format.trim() : "jpg";
        strWidth = (strWidth != null) ? strWidth.trim() : "100";
        strRatio = (strRatio != null) ? strRatio.trim() : "0";
        quality = (quality != null) ? quality.trim() : "0.4";

        int width = Integer.parseInt(strWidth);
        int maxWidth = cmsContext.settings.imageMaxWidth;
        double ratio = Double.parseDouble(strRatio);

        if (maxWidth > 0 && width > maxWidth)
            width = maxWidth;

        Element noscript = new Element("noscript");
        Element noscriptImg = new Element("img");
        Element img = new Element("img");

        String imageUrl = String.format("/_image/%s/%s/%s/%s.%s", width, strRatio, quality, imageKey, format);

        noscriptImg.setAttribute("src", imageUrl);

        element.setAttribute("class", "responsive-figure");
        img.setAttribute("class", "js-img");
        int[] size = generateSizeNew(ratio, width, 20);
        img.setAttribute("src", createPlaceholderImage(size[0], size[1]));

        noscript.addChild(noscriptImg);
        element.addChild(noscript);
        element.addChild(img);

        return ProcessorResult.OK;
    }

    @Override
    public int getPrecedence()
    {
        return 0;
    }

    private String createPlaceholderImage(int width, int height)
    {
        try
        {
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);


            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(out);

            ImageWriter imageWriter = ImageIO.getImageWritersByFormatName("png").next();
            imageWriter.setOutput(imageOutputStream);

            imageWriter.write(null, new IIOImage(bufferedImage, null, null), null);
            out.close();

            byte[] bytes = out.toByteArray();

            return "data:image/png;base64," + new String(Base64.encodeBase64(bytes));
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABAQMAAAAl21bKAAAAA1BMVEUAAACnej3aAAAAAXRSTlMAQObYZgAAAApJREFUCNdjYAAAAAIAAeIhvDMAAAAASUVORK5CYII=";
        }
    }

    int[] generateSize(double ratio, int maxSize)
    {
        int[] result = new int[] {1, 1};
        double lowestDiff = 9999d;
        int i = 0;

        for (int w = 1; w <= maxSize; w++)
        {
            for (int h = (int)((double)w/ratio); h <= maxSize; h++)
            {
                i++;
                double currentRatio = (double)w / (double)h;
                double diff = Math.abs(currentRatio - ratio);
                if (diff < lowestDiff)
                {
                    result = new int[] {w, h};
                    System.out.println("it: " + i);

                    if (diff < 0.02d)
                        return result;

                    lowestDiff = diff;
                }
            }
        }
        return result;
    }

    int[] generateSizeNew(double ratio, int width, int maxSize)
    {
        int height = (int)Math.round((double)width / ratio);
        int gcd = gcd(width, height);
        int newWidth = width / gcd;
        int newHeight = height / gcd;

        if (Math.max(newWidth, newHeight) > maxSize)
        {
            if (ratio >= 1d)
            {
                newWidth = maxSize;
                newHeight = (int)Math.round((double)maxSize / ratio);
            }
            else
            {
                newHeight = maxSize;
                newWidth = (int)Math.round((double)maxSize * ratio);
            }
        }

        return new int[] { newWidth, newHeight };
    }

    private int gcd(int a, int b)
    {
        if(a == 0 || b == 0) return a+b;
        return gcd(b,a%b);
    }
}
