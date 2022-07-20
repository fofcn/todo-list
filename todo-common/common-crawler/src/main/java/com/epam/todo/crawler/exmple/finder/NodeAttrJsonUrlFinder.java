package com.epam.todo.crawler.exmple.finder;

import com.epam.todo.crawler.wrapper.finder.UrlFinder;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NodeAttrJsonUrlFinder implements UrlFinder {
    private static final Pattern IMG_SRC_SET_PATTERN = Pattern.compile("srcset=\\\"(.*?)\\\"");

    @Override
    public List<String> find(String htmlString) {
        List<String> urlList = new ArrayList<>();
        Matcher matcher = IMG_SRC_SET_PATTERN.matcher(htmlString);
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
                    urlList.add(url);
                }

            }
        }

        return urlList;
    }
}
