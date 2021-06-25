package com.searchscraperserver.server.scraper;

import java.util.Set;

import com.searchscraperserver.server.models.HostNameFilterType;
import com.searchscraperserver.server.models.ScrapedContent;

import org.jsoup.select.Elements;

public interface IScraper {
    String getSpecificPath(Elements links, String path);

    Elements getDomNodes(String elementType, String url);

    Set<String> filterLinksByPrefferedHost(ScrapedContent searchResult, String hostName);

    ScrapedContent scrape(String searchString, String url, int totalPages, ScrapedContent currentScrapedContent);

    ScrapedContent handleDefaultScrape(String searchString, HostNameFilterType hostNameFilterType, String hostName,
            String url, int totalPages);

    ScrapedContent handlePathScrape(String searchString, String url, String pathType, int totalPages);
}
