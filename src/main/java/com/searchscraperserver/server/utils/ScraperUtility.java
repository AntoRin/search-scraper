package com.searchscraperserver.server.utils;

public class ScraperUtility {
    public static String refineLink(String source, String hostDomainRef) {
        if (!source.startsWith("data:")) {
            if (source.startsWith("http"))
                return source;
            else if (source.startsWith("//"))
                return "https:" + source;
            else if (source.startsWith("/"))
                return hostDomainRef + source;
        }
        return null;
    }
}
