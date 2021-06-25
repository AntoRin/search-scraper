package com.searchscraperserver.server.models;

public class SearchPostBody {
    private String q;
    private String hostNameFilterType;
    private String hostName = "searchResults";
    private int totalPages;

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

    public void setHostNameFilterType(String hostNameFilterType) {
        this.hostNameFilterType = hostNameFilterType;
    }

    public int getTotalPages() {
        return this.totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

}
