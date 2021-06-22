package com.antorin.stack_crawler.api;

import com.antorin.stack_crawler.scraper.ScrapedContent;
import com.antorin.stack_crawler.scraper.Scraper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping(value = "/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<ScrapedContent> scrape(@RequestBody SearchPostBody reqBody) {

        try {
            ScrapedContent result = this.scraper.handleScrape(reqBody.getQ(), reqBody.getContentType(),
                    reqBody.getHostNameFilterType(), reqBody.getHostName());

            if (result == null)
                throw new Exception("Not found");

            return new ApiResponse<ScrapedContent>("ok", result);
        } catch (Exception e) {
            return new ApiResponse<ScrapedContent>("error", null);
        }
    }
}
