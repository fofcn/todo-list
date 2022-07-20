package com.epam.todo.crawler.wrapper.store;

public interface PathReplacer {

    String html(String htmlString);

    String path(String path);
}
