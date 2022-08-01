package com.epam.todo.crawler.wrapper;

import com.epam.todo.crawler.wrapper.filter.UrlFilter;
import com.epam.todo.crawler.wrapper.finder.UrlFinder;
import com.epam.todo.crawler.wrapper.store.DataStoreStrategy;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

import java.util.ArrayList;
import java.util.List;

public class WebCrawlerControllerWrapper {

    private final DataStoreStrategy dataStoreStrategy;

    private final int numberOfCrawlers;

    private final CrawlController crawlController;

    private final List<UrlFilter> urlFilterList = new ArrayList<>();

    private final List<UrlFinder> urlFinderList = new ArrayList<>();

    public WebCrawlerControllerWrapper(final int numberOfCrawlers, final CrawlConfig crawlConfig, final DataStoreStrategy dataStoreStrategy) {
        this.dataStoreStrategy = dataStoreStrategy;
        this.numberOfCrawlers = numberOfCrawlers;
        PageFetcher pageFetcher = new PageFetcher(crawlConfig);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        try {
            crawlController = new CrawlController(crawlConfig, pageFetcher, robotstxtServer);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }

    }

    public void addSeed(String... urls) {
        for (String url : urls) {
            crawlController.addSeed(url);
        }
    }

    public void start() {
        CrawlController.WebCrawlerFactory<WebCrawlerWrapper> factory = () -> {
            WebCrawlerWrapper webCrawlerWrapper = new WebCrawlerWrapper(dataStoreStrategy);
            webCrawlerWrapper.addUrlFilter(urlFilterList);
            webCrawlerWrapper.addUrlFinder(urlFinderList);
            return webCrawlerWrapper;
        };
        crawlController.start(factory, numberOfCrawlers);
    }

    public CrawlController getCrawlerController() {
        return this.crawlController;
    }

    public void addUrlFilter(UrlFilter... urlFilters) {
        for (UrlFilter urlFilter: urlFilters) {
            urlFilterList.add(urlFilter);
        }
    }

    public void addUrlFinder(UrlFinder... urlFinders) {
        for (UrlFinder urlFinder: urlFinders) {
            urlFinderList.add(urlFinder);
        }
    }
}
