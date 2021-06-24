package com.antorin.stack_crawler.models;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ScrapedContent {
    private String title;
    private Set<String> textContent;
    private List<String> links;
    private List<String> imageLinks;
    private List<String> videoLinks;

    public ScrapedContent() {
        this.title = "";

        this.textContent = new LinkedHashSet<String>();
        textContent.add("");

        this.links = this.imageLinks = this.videoLinks = new ArrayList<String>();

        this.links.add("");
        this.imageLinks.add("");
        this.videoLinks.add("");
    }

    public ScrapedContent(String title, Set<String> textContent, List<String> links, List<String> imageLinks,
            List<String> videoLinks) {
        this.title = title;
        this.textContent = textContent;
        this.links = links;
        this.imageLinks = imageLinks;
        this.videoLinks = videoLinks;
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

    public Set<String> getTextContent() {
        return textContent;
    }

    public void setTextContent(Set<String> textContent) {
        this.textContent = textContent;
    }

    public List<String> getImageLinks() {
        return imageLinks;
    }

    public void setImageLinks(List<String> imageLinks) {
        this.imageLinks = imageLinks;
    }
}
