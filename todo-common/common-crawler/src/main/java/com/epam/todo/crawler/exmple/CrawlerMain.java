package com.epam.todo.crawler.exmple;

import com.epam.todo.crawler.exmple.config.ExampleConfig;
import com.epam.todo.crawler.exmple.filter.ExternalUrlFilter;
import com.epam.todo.crawler.exmple.filter.ResourceUrlFilter;
import com.epam.todo.crawler.exmple.filter.SpecificUrlFilter;
import com.epam.todo.crawler.exmple.finder.ImageSrcSetUrlFinder;
import com.epam.todo.crawler.exmple.finder.NestUrlFinder;
import com.epam.todo.crawler.exmple.finder.NodeAttrJsonUrlFinder;
import com.epam.todo.crawler.exmple.store.JcrContentReplacer;
import com.epam.todo.crawler.wrapper.WebCrawlerControllerWrapper;
import com.epam.todo.crawler.wrapper.store.HostStoreStrategy;
import com.epam.todo.crawler.wrapper.store.PathReplacer;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import lombok.SneakyThrows;

import java.util.Arrays;

public class CrawlerMain {

    @SneakyThrows
    public static void main(String[] args) {
        String crawlStorageFolder = "c:\\test";
        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);
        config.setUserAgentString("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36");
        config.setIncludeBinaryContentInCrawling(true);
        config.setPolitenessDelay(200);
        config.setIncludeHttpsPages(true);
        config.setMaxDownloadSize(100 * 1024 * 1024);

        ExampleConfig exampleConfig = new ExampleConfig();
        exampleConfig.setDomain("https://www.example.com");
        exampleConfig.setHostStorePath(crawlStorageFolder);
        exampleConfig.setSeeds("https://www.example.com");
        exampleConfig.setAllowedDomains(Arrays.asList("https://www.example.com", "https://cdn.example.com"));

        PathReplacer pathReplacer = new JcrContentReplacer();
        WebCrawlerControllerWrapper wrapper = new WebCrawlerControllerWrapper(1, config,
                new HostStoreStrategy(exampleConfig.getHostStorePath(), pathReplacer));
        configSeed(wrapper, exampleConfig.getSeeds());
        configFilters(wrapper, exampleConfig);
        configFinders(wrapper, exampleConfig);
        wrapper.start();
    }

    private static void configSeed(WebCrawlerControllerWrapper wrapper, String seedStr) {
        String[] seeds = seedStr.split(";");
        wrapper.addSeed(seeds);
    }


    private static void configFinders(WebCrawlerControllerWrapper wrapper, ExampleConfig exampleConfig) {
        ImageSrcSetUrlFinder imageSrcSetUrlFinder = new ImageSrcSetUrlFinder(exampleConfig);
        NestUrlFinder nestUrlFinder = new NestUrlFinder(exampleConfig);
        NodeAttrJsonUrlFinder nodeAttrJsonUrlFinder = new NodeAttrJsonUrlFinder();
        wrapper.addUrlFinder(imageSrcSetUrlFinder, nestUrlFinder, nodeAttrJsonUrlFinder);
    }

    private static void configFilters(WebCrawlerControllerWrapper wrapper, ExampleConfig exampleConfig) {
        ExternalUrlFilter externalUrlFilter = new ExternalUrlFilter(exampleConfig);
        ResourceUrlFilter resourceUrlFilter = new ResourceUrlFilter(exampleConfig);
        SpecificUrlFilter specificUrlFilter = new SpecificUrlFilter(exampleConfig);
        wrapper.addUrlFilter(externalUrlFilter, resourceUrlFilter, specificUrlFilter);
    }
}
