package com.example.cyberguide2.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;
// JSoN RESPoNSE
//
//{"status":"ok",
//        "totalResults":250,
//        "articles":[]
//        }
public class ResponseModel {

    @SerializedName("status") //  JSON field
    private String status; // Java field
    @SerializedName("totalResults")
    private int totalResults;
    @SerializedName("articles")
//    List  ordered collection of the article array in response
    private List<Article> articles = null;
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public int getTotalResults() {
        return totalResults;
    }
    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }
    public List<Article> getArticles() {
        return articles;
    }
    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}