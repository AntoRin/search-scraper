package com.searchscraperserver.server.api;

import com.searchscraperserver.server.models.ApiResponse;
import com.searchscraperserver.server.models.SearchPostBody;
import com.searchscraperserver.server.models.HostNameFilterType;
import com.searchscraperserver.server.models.ScrapedContent;
import com.searchscraperserver.server.scraper.Scraper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/search")
public class SearchController {
    private Scraper scraper;

    @Autowired
    public SearchController(Scraper scraper) {
        this.scraper = scraper;
    }

    @GetMapping("/test")
    public ResponseEntity<ApiResponse<String>> index() {
        ApiResponse<String> res = new ApiResponse<String>("ok", "");
        return ResponseEntity.status(200).body(res);
    }

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<?>> getSearchData(@RequestBody SearchPostBody reqBody) {
        try {
            HostNameFilterType hostNameFilter = reqBody.getHostNameFilterType() == null
                    || reqBody.getHostNameFilterType() == "" ? HostNameFilterType.none
                            : HostNameFilterType.valueOf(reqBody.getHostNameFilterType());

            ScrapedContent result = this.scraper.handleDefaultScrape(reqBody.getQ(), hostNameFilter,
                    reqBody.getHostName(), "https://www.google.com/search?q=", reqBody.getTotalPages());

            if (result == null)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not found - consider refining search");

            return ResponseEntity.status(200).body(new ApiResponse<ScrapedContent>("ok", result));
        } catch (Exception e) {
            ResponseEntity<ApiResponse<?>> response = ResponseEntity.status(400)
                    .body(new ApiResponse<String>("error", e.getMessage()));
            return response;
        }
    }

    @PostMapping(value = "/yt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<?>> getYtData(@RequestBody SearchPostBody reqBody) {
        try {
            ScrapedContent result = this.scraper.handlePathScrape(reqBody.getQ(), "https://www.google.com/search?q=",
                    "Videos", reqBody.getTotalPages());

            if (result == null)
                throw new Exception("Not found");

            return ResponseEntity.status(200).body(new ApiResponse<ScrapedContent>("ok", result));
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
            ResponseEntity<ApiResponse<?>> response = ResponseEntity.status(400)
                    .body(new ApiResponse<Exception>("error", e));
            return response;
        }
    }
}
