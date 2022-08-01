package com.epam.todo.crawler.wrapper.finder;

import java.util.List;

public interface UrlFinder {

    /**
     * find urls by html data
     * @param html html data
     * @return url list if found, null or empty list otherwise
     */
    List<String> find(String html);
}
