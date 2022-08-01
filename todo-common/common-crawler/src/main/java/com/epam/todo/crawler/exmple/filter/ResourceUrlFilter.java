package com.epam.todo.crawler.exmple.filter;

import com.epam.todo.crawler.exmple.util.StorePathUtil;
import com.epam.todo.crawler.exmple.config.ExampleConfig;
import com.epam.todo.crawler.wrapper.filter.UrlFilter;
import com.epam.todo.crawler.wrapper.WebCrawlerWrapper;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResourceUrlFilter implements UrlFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceUrlFilter.class);
    private static final Pattern MEDIA_FILTERS = Pattern.compile(
            "(css|js|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf|rm|smil|wmv|swf|wma|zip|rar|gz|jpg|jpeg|png|gif|bmp|svg|woff2)");


    private final ExampleConfig exampleConfig;

    public ResourceUrlFilter(ExampleConfig exampleConfig) {
        this.exampleConfig = exampleConfig;
    }

    @Override
    public boolean filter(WebCrawlerWrapper wrapper, Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        // check if media filter type
        Matcher matcher = MEDIA_FILTERS.matcher(href);
        if (matcher.find()) {
            // check if local path has downloaded
            String saveFilePath = StorePathUtil.determinePath(exampleConfig.getHostStorePath(), url.getPath(), false);
            if (Files.exists(Paths.get(saveFilePath))) {
                LOGGER.info("The url downloaded we should not visit URL: {}", href);
                return false;
            }
        }
        return true;
    }
}
