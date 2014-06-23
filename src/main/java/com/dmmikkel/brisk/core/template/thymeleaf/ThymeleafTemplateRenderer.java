package com.dmmikkel.brisk.core.template.thymeleaf;

import com.dmmikkel.brisk.core.template.TemplateContext;
import com.dmmikkel.brisk.core.template.TemplateRenderer;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.processor.ElementNameProcessorMatcher;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ThymeleafTemplateRenderer
        implements TemplateRenderer
{
    @Override
    public String render(TemplateContext context)
            throws Exception
    {
        TemplateEngine templateEngine = getTemplateEngine(context);

        Map<String, Object> variables = new HashMap<>(3);
        variables.put("site", context.site);
        variables.put("page", context.page);
        variables.put("contents", context.contents);

        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(variables);

        return templateEngine.process(context.page.masterTemplate, thymeleafContext);
    }

    public TemplateEngine getTemplateEngine(TemplateContext context)
    {
        if (templateEngineInstance != null)
            return templateEngineInstance;

        templateEngineInstance = new TemplateEngine();
        FileTemplateResolver templateResolver = new FileTemplateResolver();
        templateResolver.setPrefix(context.siteTemplateDirectory.getAbsolutePath() + File.separator);
        templateResolver.setSuffix(".html");
        templateResolver.setCacheable(false);
        templateEngineInstance.setTemplateResolver(templateResolver);

        StandardDialect standardDialect = new StandardDialect();
        ElementNameProcessorMatcher elementNameProcessorMatcher = new ElementNameProcessorMatcher("figure", false);
        Set<IProcessor> processorSet = new HashSet<>();
        processorSet.add(new ResponsiveImageProcessor(elementNameProcessorMatcher, context.cmsContext));
        standardDialect.setAdditionalProcessors(processorSet);
        templateEngineInstance.setDialect(standardDialect);

        return templateEngineInstance;
    }

    // TODO: Maybe create and store instances of ViewProcessors instead of static storage here
    private static TemplateEngine templateEngineInstance;
}