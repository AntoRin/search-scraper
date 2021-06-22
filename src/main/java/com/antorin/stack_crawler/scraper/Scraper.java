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

    private void filterContent(ScrapedContent content, ContentType type) {
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

    private ScrapedContent scrape(String searchString, ContentType type) {
        try {
            Document htmlDoc = Jsoup.connect("https://www.google.com/search?q=" + searchString).get();

            String title = htmlDoc.title();
            Element body = htmlDoc.body();

            Elements linkElements = body.getElementsByTag("a");
            Elements paragraphElements = body.getElementsByTag("p");

            List<String> links = new ArrayList<String>();
            List<String> paragraphs = new ArrayList<String>();

            linkElements.forEach(element -> links.add(element.attr("abs:href")));
            paragraphElements.forEach(element -> paragraphs.add(element.text()));

            ScrapedContent result = new ScrapedContent(title, paragraphs, links);

            this.filterContent(result, type);

            return result;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public ScrapedContent handleScrape(String searchString, ContentType type, HostNameFilterType hostNameFilterType,
            String hostName) {
        try {
            if (searchString == null || searchString == "")
                throw new Exception("No query");

            ScrapedContent searchResult = this.scrape(searchString, type);

            if (hostName == null || hostName == "" || hostName == "searchResults")
                return searchResult;

            List<String> userPreferredHosts = this.filterLinksByPrefferedHost(searchResult, hostName);

            searchResult.setLinks(userPreferredHosts);

            return searchResult;

        } catch (Exception e) {
            return null;
        }
    }
}
