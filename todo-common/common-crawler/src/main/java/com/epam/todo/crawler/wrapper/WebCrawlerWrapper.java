package com.epam.todo.crawler.wrapper;

import com.epam.todo.crawler.wrapper.filter.UrlFilter;
import com.epam.todo.crawler.wrapper.finder.UrlFinder;
import com.epam.todo.crawler.wrapper.store.DataStoreStrategy;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * this is a demo of crawler4j.
 * this crawler can not crawler resource these nested in div tag and etc.
 */
public class WebCrawlerWrapper extends WebCrawler {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebCrawlerWrapper.class);

    private final List<UrlFilter> urlFilterList = new ArrayList<>();

    private final List<UrlFinder> urlFinderList = new ArrayList<>();

    private final DataStoreStrategy dataStoreStrategy;

    public WebCrawlerWrapper(final DataStoreStrategy dataStoreStrategy) {
        this.dataStoreStrategy = dataStoreStrategy;
    }

    /**
     * add a filter
     * @param urlFilters url filter
     */
    public void addUrlFilter(List<UrlFilter> urlFilters) {
        for (UrlFilter urlFilter : urlFilters) {
            urlFilterList.add(urlFilter);
        }
    }

    public void addUrlFinder(List<UrlFinder> urlFinders) {
        for (UrlFinder urlFinder: urlFinders) {
            urlFinderList.add(urlFinder);
        }
    }

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        for (UrlFilter urlFilter : urlFilterList) {
            boolean filtered = urlFilter.filter(this, referringPage, url);
            if (!filtered) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void visit(Page page) {
        String uriPath = page.getWebURL().getPath();
        if (StringUtils.isBlank(uriPath)) {
            LOGGER.warn("blank uri, skip this uri: {}, url: {}", uriPath, page.getWebURL().getURL());
            return;
        }

        boolean isHtmlPage = page.getParseData() instanceof HtmlParseData;
        if (isHtmlPage) {
            findUrl(((HtmlParseData) page.getParseData()).getHtml());
        }

        dataStoreStrategy.store(page);
    }

    @Override
    public void onContentFetchError(Page page) {
        // todo 内容拉取报错
    }

    @Override
    protected void onUnexpectedStatusCode(String urlStr, int statusCode, String contentType,
                                          String description) {
        if (statusCode == HttpStatus.SC_NOT_FOUND) {
            myController.addSeed(urlStr);
        }
    }

    private void findUrl(String htmlString) {
        for (UrlFinder urlFinder : urlFinderList) {
            List<String> urlList = urlFinder.find(htmlString);
            urlList.forEach(url -> myController.addSeed(url));
        }
    }
}
