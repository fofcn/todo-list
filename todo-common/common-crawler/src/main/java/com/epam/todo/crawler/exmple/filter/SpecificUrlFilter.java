package com.epam.todo.crawler.exmple.filter;

import com.epam.todo.crawler.wrapper.WebCrawlerWrapper;
import com.epam.todo.crawler.exmple.config.ExampleConfig;
import com.epam.todo.crawler.wrapper.filter.UrlFilter;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpecificUrlFilter implements UrlFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpecificUrlFilter.class);

    private final ExampleConfig exampleConfig;

    public SpecificUrlFilter(ExampleConfig exampleConfig) {
        this.exampleConfig = exampleConfig;
    }

    @Override
    public boolean filter(WebCrawlerWrapper wrapper, Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        LOGGER.info("We should visit this URL: {}", href);
        if (exampleConfig.getAllowedDomains() != null) {
            for (String domain : exampleConfig.getAllowedDomains()) {
                if (href.startsWith(domain)) {
                    return true;
                }
            }
        }
        LOGGER.info("We should not visit this URL: {}", href);
        return false;
    }
}
