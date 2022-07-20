package com.epam.todo.crawler.exmple.store;

import com.epam.todo.crawler.wrapper.store.PathReplacer;
import org.apache.commons.lang3.StringUtils;

public class JcrContentReplacer implements PathReplacer {

    @Override
    public String html(String htmlString) {
        htmlString = StringUtils.replace(htmlString, "https://example.com", ".");
        htmlString = StringUtils.replace(htmlString, "jcr:content", "jcr___content");
        return htmlString;
    }

    @Override
    public String path(String path) {
        return path.replace(":", "___");
    }
}
