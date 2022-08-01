package com.epam.todo.crawler.wrapper.store;

import edu.uci.ics.crawler4j.crawler.Page;

public interface DataStoreStrategy {

    /**
     * store a page data
     * @param page page
     */
    void store(Page page);
}
