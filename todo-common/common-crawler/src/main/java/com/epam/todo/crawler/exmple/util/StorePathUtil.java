package com.epam.todo.crawler.exmple.util;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class StorePathUtil {

    public static String determinePath(String hostStoreFolder, String uriPath, boolean isHtmlPage) {
        String dir = uriPath;
        if (uriPath.lastIndexOf('.') != -1) {
            dir = uriPath.substring(0, uriPath.lastIndexOf('/'));
            if (StringUtils.contains(dir, ":")) {
                // replace jcr : character to ___
                dir = dir.replace(":", "___");
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
