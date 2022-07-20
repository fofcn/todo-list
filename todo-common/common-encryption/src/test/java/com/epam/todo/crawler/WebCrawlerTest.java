package com.epam.todo.crawler;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.url.WebURL;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * this is a demo of crawler4j.
 * this crawler can not crawler resource these nested in div tag and etc.
 */
public class WebCrawlerTest extends WebCrawler {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebCrawlerTest.class);

    private static final Pattern MEDIA_FILTERS = Pattern.compile(
            "(css|js|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf|rm|smil|wmv|swf|wma|zip|rar|gz|jpg|jpeg|png|gif|bmp|svg|woff2)");

    private static final String crawlStorageFolder = "C:\\Users\\Ford_Ji\\Downloads\\demo";

    private static final Pattern NEST_URL_PATTERN = Pattern.compile("url(?:\\(['\"]?)(.*?)(?:['\"]?\\))");

    private static final Pattern JSON_IMG_URL_PATTERN = Pattern.compile("image&#34;:&#34;(.*?)&#34");

    private static final Pattern IMG_SRC_SET_PATTERN = Pattern.compile("srcset=\\\"(.*?)\\\"");

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        // todo 这里增加url过滤器
        LOGGER.debug("should visit URL: {}", url.getURL());
        String href = url.getURL().toLowerCase();
        if (!href.startsWith("https://example.com")) {
            LOGGER.info("External link, skip this url: {}", href);
            return false;
        }

        // check if media filter type
        Matcher matcher = MEDIA_FILTERS.matcher(href);
        if (matcher.find()) {
            // check if local path has downloaded
            String saveFilePath = determinePath(url.getPath(), false);
            if (Files.exists(Paths.get(saveFilePath))) {
                LOGGER.info("The url downloaded we should not visit URL: {}", href);
                return false;
            }
        }

        LOGGER.info("We should visit this URL: {}", href);
        if (!href.startsWith("https://example.com/fr-fr")) {
            return false;
        }
        return true;
    }

    @Override
    public void visit(Page page) {
        String uriPath = page.getWebURL().getPath();
        if (StringUtils.isBlank(uriPath)) {
            return;
        }

        // todo 这里增加内容解析
        // todo 这里增加存储策略，封装一个整站还原存储或自定义存储策略

        boolean isHtmlPage = page.getParseData() instanceof HtmlParseData;
        String absFilePath = determinePath(uriPath, isHtmlPage);
        Path saveFilePath = Paths.get(absFilePath);
        try {
            if (isHtmlPage) {
                HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
                String html = htmlParseData.getHtml();

                // todo 这里增加链接解析器链
                parseSrcSet(html);
                html = html.replace("https://cdn-statics-new.example.com", ".");
                html = StringUtils.replace(html, "jcr:content", "jcr___content");
                Files.write(saveFilePath, html.getBytes(StandardCharsets.UTF_8));
            } else {
                if (Files.exists(saveFilePath)) {
                    return;
                }
                Files.write(saveFilePath, page.getContentData());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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

    private String determinePath(String uriPath, boolean isHtmlPage) {
        String dir = uriPath;
        if (uriPath.lastIndexOf('.') != -1) {
            dir = uriPath.substring(0, uriPath.lastIndexOf('/'));
            if (StringUtils.contains(dir, ":")) {
                // replace jcr : character to ___
                dir = dir.replace(":", "___");
            }
        }

        Path dirPath = Paths.get(crawlStorageFolder + '/' + dir);
        if (!Files.exists(dirPath)) {
            try {
                Files.createDirectories(dirPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // determine file name
        String fileName = "";
        if (!uriPath.equals(dir)) {
            String suffix = uriPath.substring(uriPath.lastIndexOf('.'));
            String fileNamePart = uriPath.substring(uriPath.lastIndexOf('/') + 1, uriPath.lastIndexOf('.'));
            fileName = fileNamePart + suffix;
        } else {
            if (isHtmlPage) {
                fileName = "index.html";
            }
        }

        return crawlStorageFolder + '/' + dir + '/' + fileName;
    }

    public void parseSrcSet(String htmlString) {
        Matcher matcher = NEST_URL_PATTERN.matcher(htmlString);
        while (matcher.find()) {
            String uri = htmlString.substring(matcher.start(), matcher.end());
            uri = StringUtils.removeStart(uri, "url(");
            uri = StringUtils.removeStart(uri, "'");
            uri = StringUtils.removeStart(uri, "\"");
            uri = StringUtils.removeEnd(uri, ")");
            uri = StringUtils.removeEnd(uri, "\"");
            uri = StringUtils.removeEnd(uri, "\'");
            uri = StringUtils.replace(uri, "2f", "");
            uri = StringUtils.replace(uri, "2f ", "");
            uri = StringUtils.replace(uri, " ", "");
            uri = StringUtils.replace(uri, "\\", "/");
            if (!uri.startsWith("http://") && !uri.startsWith("https://")) {
                String url = "https://example.com" + uri;
                this.myController.addSeed(url);
            }
        }

        matcher = JSON_IMG_URL_PATTERN.matcher(htmlString);
        while (matcher.find()) {
            String uri = htmlString.substring(matcher.start(), matcher.end());
            if (!uri.startsWith("http://") && !uri.startsWith("https://")) {
                uri = StringUtils.replace(uri, "image&#34;:&#34;", "");
                uri = StringUtils.replace(uri, "&#34", "");
                String url = "https://example.com" + uri;
                this.myController.addSeed(url);
            }
        }

        matcher = IMG_SRC_SET_PATTERN.matcher(htmlString);
        while (matcher.find()) {
            String uri = htmlString.substring(matcher.start(), matcher.end());
            uri = StringUtils.replace(uri, "srcset=\"", "");
            uri = StringUtils.replace(uri, "\"", "");
            // split
            String[] srcWidth = uri.split(",");
            if (ArrayUtils.isEmpty(srcWidth)) {
                continue;
            }

            for (String src: srcWidth) {
                if (StringUtils.isBlank(src)) {
                    continue;
                }

                String[] imgUri = src.trim().split(" ");
                if (ArrayUtils.isEmpty(imgUri)) {
                    continue;
                }

                if (!imgUri[0].startsWith("http://") && !imgUri[0].startsWith("https://")) {
                    String url = "https://example.com";
                    if (!imgUri[0].startsWith("/")) {
                        url = url + "/";
                    }
                    url = url + imgUri[0];
                    this.myController.addSeed(url);
                }

            }
        }
    }

    public static void main(String[] args) throws Exception {

        int numberOfCrawlers = 7;

        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);
        config.setUserAgentString("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36");
        config.setIncludeBinaryContentInCrawling(true);
        config.setPolitenessDelay(200);
        config.setIncludeHttpsPages(true);
        config.setMaxDownloadSize(100 * 1024 * 1024);

        // Instantiate the controller for this crawl.
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

        // For each crawl, you need to add some seed urls. These are the first
        // URLs that are fetched and then the crawler starts following links
        // which are found in these pages
        controller.addSeed("https://example.com/fr-fr");

        // The factory which creates instances of crawlers.
        CrawlController.WebCrawlerFactory<WebCrawlerTest> factory = WebCrawlerTest::new;

        // Start the crawl. This is a blocking operation, meaning that your code
        // will reach the line after this only when crawling is finished.
        controller.start(factory, numberOfCrawlers);
    }
}
