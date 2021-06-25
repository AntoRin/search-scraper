package com.antorin.stack_crawler.scraper;

import com.antorin.stack_crawler.models.ScrapedContent;
import com.antorin.stack_crawler.utils.ScraperUtility;
import com.antorin.stack_crawler.models.HostNameFilterType;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
public class Scraper implements IScraper {
    @Override
    public String getSpecificPath(Elements linkElements, String path) {
        String requiredLink = null;

        for (Element link : linkElements) {
            if (link.wholeText().equals(path)) {
                requiredLink = link.attr("abs:href");
                break;
            }
        }

        return requiredLink;
    }

    @Override
    public Elements getDomNodes(String elementType, String url) {
        try {
            return Jsoup.connect(url).get().body().getElementsByTag("a");
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Set<String> filterLinksByPrefferedHost(ScrapedContent searchResult, String hostName) {
        Set<String> filteredLinks = new LinkedHashSet<String>();

        searchResult.getLinks().forEach(link -> {
            try {
                URL url = new URL(link);
                String host = url.getHost();
                if (host.indexOf(hostName) != -1)
                    filteredLinks.add(link);
            } catch (Exception e) {

            }
        });

        return filteredLinks;
    }

    @Override
    public ScrapedContent scrape(String searchString, String url, int totalPages,
            ScrapedContent currentScrapedContent) {
        if (totalPages == 0)
            return currentScrapedContent;

        Set<String> currentLinks = currentScrapedContent.getLinks();
        Set<String> currentTextContent = currentScrapedContent.getTextContent();
        List<String> currentImageLinks = currentScrapedContent.getImageLinks();
        List<String> currentVideoLinks = currentScrapedContent.getVideoLinks();

        try {
            URL urlRef = new URL(url);
            final String hostDomainRef = urlRef.getProtocol() + "://" + urlRef.getHost();

            if (searchString == null)
                searchString = "";

            Document htmlDoc = Jsoup.connect(url + searchString).userAgent(
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36")
                    .get();

            String title = htmlDoc.title();

            currentScrapedContent.setTitle(title);

            Element body = htmlDoc.body();

            Elements linkElements = htmlDoc.select("a");
            Elements textContentElements = body.getElementsByTag("div");
            Elements imageElements = body.getElementsByTag("img");
            Elements videoElements = body.getElementsByTag("video");

            linkElements.forEach(element -> {
                String refinedLink = ScraperUtility.refineLink(element.attr("href"), hostDomainRef);
                if (refinedLink != null) {
                    currentLinks.add(refinedLink);
                    currentScrapedContent.setLinks(currentLinks);
                }
            });

            textContentElements.forEach(element -> {
                if (element.text() != "") {
                    currentTextContent.add(element.text());
                    currentScrapedContent.setTextContent(currentTextContent);
                }
            });

            imageElements.forEach(element -> {
                String refinedLink = ScraperUtility.refineLink(element.attr("src"), hostDomainRef);
                if (refinedLink != null) {
                    currentImageLinks.add(refinedLink);
                    currentScrapedContent.setImageLinks(currentImageLinks);
                }
            });
            videoElements.forEach(element -> {
                currentVideoLinks.add(element.attr("abs:src"));
                currentScrapedContent.setVideoLinks(currentVideoLinks);
            });

            String nextPageUrl = htmlDoc.select("#pnnext").attr("abs:href");

            System.out.println(nextPageUrl);

            Thread.sleep(1000);

            return this.scrape(searchString, nextPageUrl, --totalPages, currentScrapedContent);
        } catch (IOException IOE) {
            System.out.println(IOE);
            return currentScrapedContent;
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public ScrapedContent handleDefaultScrape(String searchString, HostNameFilterType hostNameFilterType,
            String hostName, String url, int totalPages) {
        try {
            ScrapedContent searchResult = new ScrapedContent();

            searchResult = this.scrape(searchString, url, totalPages, searchResult);

            if (hostName == null || hostName == "" || hostName == "searchResults")
                return searchResult;

            switch (hostNameFilterType) {
                case page: {
                    Set<String> userPreferredHosts = this.filterLinksByPrefferedHost(searchResult, hostName);

                    searchResult.setLinks(userPreferredHosts);

                    return searchResult;
                }
                case follow: {
                    Set<String> userPreferredHosts = this.filterLinksByPrefferedHost(searchResult, hostName);

                    String hostUrl = "";

                    for (String link : userPreferredHosts) {
                        hostUrl = link;
                        break;
                    }

                    ScrapedContent preferredHostContent = new ScrapedContent();
                    preferredHostContent = this.scrape(null, hostUrl, totalPages, preferredHostContent);

                    return preferredHostContent;
                }
                case none:
                    return searchResult;
                default:
                    return searchResult;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public ScrapedContent handlePathScrape(String searchString, String url, String pathType, int totalPages) {
        try {
            Elements linkNodes = this.getDomNodes("a", url + searchString);

            String pathLink = this.getSpecificPath(linkNodes, pathType);

            ScrapedContent result = this.handleDefaultScrape(null, HostNameFilterType.page, "youtube", pathLink,
                    totalPages);

            result.setImageLinks(null);
            result.setTextContent(null);
            result.setVideoLinks(null);

            return result;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
