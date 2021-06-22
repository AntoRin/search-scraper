package com.antorin.stack_crawler.scraper;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
public class Scraper {

    public ScrapedContent scrape(String searchString) {
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

            return result;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}
