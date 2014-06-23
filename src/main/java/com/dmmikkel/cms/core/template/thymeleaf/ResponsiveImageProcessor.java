package com.dmmikkel.cms.core.template.thymeleaf;

import com.dmmikkel.cms.core.CMSContext;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.IElementNameProcessorMatcher;
import org.thymeleaf.processor.ProcessorResult;
import org.thymeleaf.processor.element.AbstractElementProcessor;

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
        String ratio = element.getAttributeValueFromNormalizedName("img", "ratio");
        String quality = element.getAttributeValueFromNormalizedName("img", "quality");

        format = (format != null) ? format.trim() : "jpg";
        strWidth = (strWidth != null) ? strWidth.trim() : "100";
        ratio = (ratio != null) ? ratio.trim() : "0";
        quality = (quality != null) ? quality.trim() : "0.4";

        int width = Integer.parseInt(strWidth);
        int maxWidth = cmsContext.settings.imageMaxWidth;

        if (width > maxWidth)
            width = maxWidth;

        Element noscript = new Element("noscript");
        Element noscriptImg = new Element("img");
        Element img = new Element("img");

        String imageUrl = String.format("/_image/%s/%s/%s/%s.%s", width, ratio, quality, imageKey, format);

        noscriptImg.setAttribute("src", imageUrl);

        element.setAttribute("class", "responsive-figure");
        img.setAttribute("class", "js-img");
        img.setAttribute("src", "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAJCAYAAAA7KqwyAAAAD0lEQVR42mNgGAWjgAoAAAJJAAFgJMPaAAAAAElFTkSuQmCC");

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
}
