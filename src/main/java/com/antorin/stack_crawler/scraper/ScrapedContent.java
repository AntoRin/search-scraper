package com.antorin.stack_crawler.scraper;

import java.util.List;

public class ScrapedContent {
    private String title;
    private List<String> paragraphs;
    private List<String> links;

    public ScrapedContent(String title, List<String> paragraphs2, List<String> links2) {
        this.setTitle(title);
        this.setParagraphs(paragraphs2);
        this.setLinks(links2);
    }

    public List<String> getLinks() {
        return links;
    }

    public void setLinks(List<String> links) {
        this.links = links;
    }

    public List<String> getParagraphs() {
        return paragraphs;
    }

    public void setParagraphs(List<String> paragraphs) {
        this.paragraphs = paragraphs;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
