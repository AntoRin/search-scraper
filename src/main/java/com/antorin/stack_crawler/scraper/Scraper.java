package com.antorin.stack_crawler.scraper;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
public class Scraper {

    private void filterContent(ScrapedContent content) {
    }

    private List<String> filterLinksByPrefferedHost(ScrapedContent searchResult, String hostName) {
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

    private ScrapedContent scrape(String searchString, String url) {
        try {
            if (url == null)
                url = "https://www.google.com/search?q=";

            if (searchString == null)
                searchString = "";

            Document htmlDoc = Jsoup.connect(url + searchString).get();

            String title = htmlDoc.title();
            Element body = htmlDoc.body();

            Elements linkElements = body.getElementsByTag("a");
            Elements textContentElements = body.getElementsByTag("div");
            Elements imageElements = body.getElementsByTag("img");

            List<String> links = new ArrayList<String>();
            List<String> textContent = new ArrayList<String>();
            List<String> imageLinks = new ArrayList<String>();

            linkElements.forEach(element -> links.add(element.attr("abs:href")));
            textContentElements.forEach(element -> textContent.add(element.text()));
            imageElements.forEach(element -> imageLinks.add(element.attr("src")));

            ScrapedContent result = new ScrapedContent(title, textContent, links, imageLinks);

            this.filterContent(result);

            return result;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public ScrapedContent handleScrape(String searchString, HostNameFilterType hostNameFilterType, String hostName) {
        try {
            if (searchString == null || searchString == "")
                throw new Exception("No query");

            ScrapedContent searchResult = this.scrape(searchString, null);

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
}
