package com.epam.todo.crawler.wrapper.store;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HostStoreStrategy implements DataStoreStrategy {

    private final String hostPath;

    private final PathReplacer pathReplacer;

    public HostStoreStrategy(String hostPath, PathReplacer pathReplacer) {
        this.hostPath = hostPath;
        this.pathReplacer = pathReplacer;
    }

    @Override
    public void store(Page page) {
        String uriPath = page.getWebURL().getPath();
        boolean isHtmlPage = page.getParseData() instanceof HtmlParseData;
        String absFilePath = determinePath(hostPath, uriPath, isHtmlPage);
        Path saveFilePath = Paths.get(absFilePath);
        try {
            if (isHtmlPage) {
                HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
                String html = htmlParseData.getHtml();
                html = pathReplacer.html(html);
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

    public String determinePath(String hostStoreFolder, String uriPath, boolean isHtmlPage) {
        String dir = uriPath;
        if (uriPath.lastIndexOf('.') != -1) {
            dir = uriPath.substring(0, uriPath.lastIndexOf('/'));
            if (StringUtils.contains(dir, ":")) {
                // replace jcr : character to ___
                dir = pathReplacer.path(dir);
            }
        }

        Path dirPath = Paths.get(hostStoreFolder + '/' + dir);
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

        return hostStoreFolder + '/' + dir + '/' + fileName;
    }
}
