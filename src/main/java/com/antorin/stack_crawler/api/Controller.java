package com.antorin.stack_crawler.api;

import com.antorin.stack_crawler.scraper.ResultType;
import com.antorin.stack_crawler.scraper.ScrapedContent;
import com.antorin.stack_crawler.scraper.Scraper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ApiResponse<ScrapedContent> scrape(@RequestParam(value = "q", defaultValue = "") String q,
            @RequestParam(value = "type", defaultValue = "standard") ResultType type) {

        try {
            ScrapedContent result = this.scraper.scrape(q, type);

            if (result == null)
                throw new Exception("Not found");

            return new ApiResponse<ScrapedContent>("ok", result);
        } catch (Exception e) {
            return new ApiResponse<ScrapedContent>("error", null);
        }
    }
}
