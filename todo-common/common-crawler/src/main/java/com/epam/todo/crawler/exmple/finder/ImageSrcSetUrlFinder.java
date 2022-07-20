package com.epam.todo.crawler.exmple.finder;

import com.epam.todo.crawler.exmple.config.ExampleConfig;
import com.epam.todo.crawler.wrapper.finder.UrlFinder;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageSrcSetUrlFinder implements UrlFinder {

    private static final Pattern JSON_IMG_URL_PATTERN = Pattern.compile("image&#34;:&#34;(.*?)&#34");

    private final ExampleConfig exampleConfig;

    public ImageSrcSetUrlFinder(ExampleConfig exampleConfig) {
        this.exampleConfig = exampleConfig;
    }

    @Override
    public List<String> find(String htmlString) {
        List<String> urlList = new ArrayList<>();
        Matcher matcher = JSON_IMG_URL_PATTERN.matcher(htmlString);
        while (matcher.find()) {
            String uri = htmlString.substring(matcher.start(), matcher.end());
            if (!uri.startsWith("http://") && !uri.startsWith("https://")) {
                uri = StringUtils.replace(uri, "image&#34;:&#34;", "");
                uri = StringUtils.replace(uri, "&#34", "");

                if (!uri.startsWith("http://") && !uri.startsWith("https://")) {
                    String url = exampleConfig.getDomain() + uri;
                    urlList.add(url);
                }
            }
        }

        return urlList;
    }
}
