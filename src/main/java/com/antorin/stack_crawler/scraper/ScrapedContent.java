package com.antorin.stack_crawler.scraper;

import java.util.List;

public class ScrapedContent {
    private String title;
    private List<String> textContent;
    private List<String> links;
    private List<String> imageLinks;
    private List<String> videoLinks;

    public ScrapedContent(String title, List<String> textContent, List<String> links, List<String> imageLinks,
            List<String> videoLinks) {
        this.setTitle(title);
        this.setTextContent(textContent);
        this.setLinks(links);
        this.setImageLinks(imageLinks);
        this.setVideoLinks(videoLinks);
    }

    public List<String> getVideoLinks() {
        return videoLinks;
    }

    public void setVideoLinks(List<String> videoLinks) {
        this.videoLinks = videoLinks;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getLinks() {
        return links;
    }

    public void setLinks(List<String> links) {
        this.links = links;
    }

    public List<String> getTextContent() {
        return textContent;
    }

    public void setTextContent(List<String> textContent) {
        this.textContent = textContent;
    }

    public List<String> getImageLinks() {
        return imageLinks;
    }

    public void setImageLinks(List<String> imageLinks) {
        this.imageLinks = imageLinks;
    }
}
