package com.antorin.stack_crawler.api;

public class SearchPostBody {
    private String q;
    private String hostNameFilterType;
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

    public String getHostNameFilterType() {
        return hostNameFilterType;
    }

    public void setFilterType(String hostNameFilterType) {
        this.hostNameFilterType = hostNameFilterType;
    }

}
