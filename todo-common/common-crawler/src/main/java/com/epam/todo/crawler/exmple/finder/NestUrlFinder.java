package com.epam.todo.crawler.exmple.finder;

import com.epam.todo.crawler.exmple.config.ExampleConfig;
import com.epam.todo.crawler.wrapper.finder.UrlFinder;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NestUrlFinder implements UrlFinder {

    private static final Pattern NEST_URL_PATTERN = Pattern.compile("url(?:\\(['\"]?)(.*?)(?:['\"]?\\))");

    private final ExampleConfig exampleConfig;

    public NestUrlFinder(ExampleConfig exampleConfig) {
        this.exampleConfig = exampleConfig;
    }

    @Override
    public List<String> find(String htmlString) {
        List<String> urlList = new ArrayList<>();
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
                String url = exampleConfig.getDomain() + uri;
                urlList.add(url);
            }
        }

        return urlList;
    }
}
