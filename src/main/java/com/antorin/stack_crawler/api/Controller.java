package com.antorin.stack_crawler.api;

import com.antorin.stack_crawler.scraper.ScrapedContent;
import com.antorin.stack_crawler.scraper.Scraper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    private Scraper scraper;

    @Autowired
    public Controller(Scraper scraper) {
        this.scraper = scraper;
    }

    @GetMapping("/")
    public ApiResponse<String> index() {
        ApiResponse<String> res = new ApiResponse<String>("ok", "");
        return res;
    }

    @GetMapping("/scrape")
    public ApiResponse<ScrapedContent> scrape() {
        ScrapedContent result = this.scraper.scrape("nodejs stack overflow");

        return new ApiResponse<ScrapedContent>("ok", result);
    }
}
