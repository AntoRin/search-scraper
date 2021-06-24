package com.antorin.stack_crawler.scraper;

import com.antorin.stack_crawler.models.ScrapedContent;
import com.antorin.stack_crawler.utils.ScraperUtility;
import com.antorin.stack_crawler.models.HostNameFilterType;

import java.net.URL;
import java.util.ArrayList;
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
    public List<String> filterLinksByPrefferedHost(ScrapedContent searchResult, String hostName) {
        List<String> filteredLinks = new ArrayList<String>();

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
    public ScrapedContent scrape(String searchString, String url) {
        try {
            URL urlRef = new URL(url);
            final String hostDomainRef = urlRef.getProtocol() + "://" + urlRef.getHost();

            if (searchString == null)
                searchString = "";

            Document htmlDoc = Jsoup.connect(url + searchString).get();

            String title = htmlDoc.title();
            Element body = htmlDoc.body();

            Elements linkElements = htmlDoc.select("a");
            Elements textContentElements = body.getElementsByTag("div");
            Elements imageElements = body.getElementsByTag("img");
            Elements videoElements = body.getElementsByTag("video");

            List<String> links = new ArrayList<String>();
            Set<String> textContent = new LinkedHashSet<String>();
            List<String> imageLinks = new ArrayList<String>();
            List<String> videoLinks = new ArrayList<String>();

            linkElements.forEach(element -> {
                String refinedLink = ScraperUtility.refineLink(element.attr("href"), hostDomainRef);
                if (refinedLink != null)
                    links.add(refinedLink);
            });

            textContentElements.forEach(element -> {
                if (element.text() != "")
                    textContent.add(element.text());
            });

            imageElements.forEach(element -> {
                String refinedLink = ScraperUtility.refineLink(element.attr("src"), hostDomainRef);
                if (refinedLink != null)
                    imageLinks.add(refinedLink);
            });
            videoElements.forEach(element -> videoLinks.add(element.attr("abs:src")));

            ScrapedContent result = new ScrapedContent(title, textContent, links, imageLinks, videoLinks);

            return result;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    @Override
    public ScrapedContent handleDefaultScrape(String searchString, HostNameFilterType hostNameFilterType,
            String hostName, String url) {
        try {
            ScrapedContent searchResult = this.scrape(searchString, url);

            if (hostName == null || hostName == "" || hostName == "searchResults")
                return searchResult;

            switch (hostNameFilterType) {
                case page: {
                    List<String> userPreferredHosts = this.filterLinksByPrefferedHost(searchResult, hostName);

                    searchResult.setLinks(userPreferredHosts);

                    return searchResult;
                }
                case follow: {
                    List<String> userPreferredHosts = this.filterLinksByPrefferedHost(searchResult, hostName);

                    String hostUrl = userPreferredHosts.get(0);

                    ScrapedContent preferredHostContent = this.scrape(null, hostUrl);

                    return preferredHostContent;
                }
                case none:
                    return searchResult;
                default:
                    return searchResult;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public ScrapedContent handlePathScrape(String searchString, String url, String pathType) {
        try {
            Elements linkNodes = this.getDomNodes("a", url + searchString);

            String pathLink = this.getSpecificPath(linkNodes, pathType);

            ScrapedContent result = this.handleDefaultScrape(null, HostNameFilterType.page, "youtube", pathLink);
            result.setImageLinks(null);
            result.setTextContent(null);
            result.setVideoLinks(null);

            return result;
        } catch (Exception e) {
            return null;
        }
    }
}
