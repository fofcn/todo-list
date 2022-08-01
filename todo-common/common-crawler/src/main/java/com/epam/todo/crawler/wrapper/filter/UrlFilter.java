package com.epam.todo.crawler.wrapper.filter;

import com.epam.todo.crawler.wrapper.WebCrawlerWrapper;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;

public interface UrlFilter {

    /**
     * filter an url
     * @param wrapper web crawler wrapper
     * @param referringPage referer page
     * @param url url
     * @return true if the url should process false otherwise
     */
    boolean filter(WebCrawlerWrapper wrapper, Page referringPage, WebURL url);
}
