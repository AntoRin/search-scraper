package com.antorin.stack_crawler.scraper;

import java.util.List;

import com.antorin.stack_crawler.models.HostNameFilterType;
import com.antorin.stack_crawler.models.ScrapedContent;

import org.jsoup.select.Elements;

public interface IScraper {
    String getSpecificPath(Elements links, String path);

    Elements getDomNodes(String elementType, String url);

    List<String> filterLinksByPrefferedHost(ScrapedContent searchResult, String hostName);

    ScrapedContent scrape(String searchString, String url);

    ScrapedContent handleDefaultScrape(String searchString, HostNameFilterType hostNameFilterType, String hostName,
            String url);

    ScrapedContent handlePathScrape(String searchString, String url, String pathType);
}
