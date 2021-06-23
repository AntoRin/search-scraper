package com.antorin.stack_crawler.api;

import com.antorin.stack_crawler.scraper.HostNameFilterType;
import com.antorin.stack_crawler.scraper.ScrapedContent;
import com.antorin.stack_crawler.scraper.Scraper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
    public ResponseEntity<ApiResponse<?>> getSearchData(@RequestBody SearchPostBody reqBody) {
        try {
            HostNameFilterType hostNameFilter = reqBody.getHostNameFilterType() == null
                    || reqBody.getHostNameFilterType() == "" ? HostNameFilterType.none
                            : HostNameFilterType.valueOf(reqBody.getHostNameFilterType());

            ScrapedContent result = this.scraper.handleScrape(reqBody.getQ(), hostNameFilter, reqBody.getHostName());

            if (result == null)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not found - consider refining search");

            return ResponseEntity.status(200).body(new ApiResponse<ScrapedContent>("ok", result));
        } catch (Exception e) {
            ResponseEntity<ApiResponse<?>> response = ResponseEntity.status(400)
                    .body(new ApiResponse<String>("error", e.getMessage()));
            return response;
        }
    }
}
