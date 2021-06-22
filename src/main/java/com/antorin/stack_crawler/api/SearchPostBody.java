package com.antorin.stack_crawler.api;

import com.antorin.stack_crawler.scraper.ContentType;
import com.antorin.stack_crawler.scraper.HostNameFilterType;

public class SearchPostBody {
    private String q;
    private ContentType contentType;
    private HostNameFilterType hostNameFilterType;
    private String hostName = "searchResults";

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public HostNameFilterType getHostNameFilterType() {
        return hostNameFilterType;
    }

    public void setFilterType(HostNameFilterType hostNameFilterType) {
        this.hostNameFilterType = hostNameFilterType;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

}
