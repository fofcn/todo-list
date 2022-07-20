package com.epam.todo.crawler.exmple.filter;

import com.epam.todo.crawler.wrapper.WebCrawlerWrapper;
import com.epam.todo.crawler.exmple.config.ExampleConfig;
import com.epam.todo.crawler.wrapper.filter.UrlFilter;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExternalUrlFilter implements UrlFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalUrlFilter.class);

    private final ExampleConfig exampleConfig;

    public ExternalUrlFilter(final ExampleConfig exampleConfig) {
        this.exampleConfig = exampleConfig;
    }

    @Override
    public boolean filter(WebCrawlerWrapper wrapper, Page referringPage, WebURL url) {
        LOGGER.debug("should visit URL?: {}", url.getURL());
        String href = url.getURL().toLowerCase();

        if (exampleConfig.getAllowedDomains() != null) {
            for (String domain : exampleConfig.getAllowedDomains()) {
                if (href.startsWith(domain)) {
                    LOGGER.debug("Yes, we should visit URL!: {}", url.getURL());
                    return true;
                }
            }
        }
        LOGGER.info("External link, skip this url: {}", href);
        return false;
    }
}
