package edu.nanoracket.npr.model;

public class Newscast {

    private String mTitle;
    private String mPubDate;
    private String mUrl;
    private String mGuid;
    private String mDuration;

    public String getTitle() {
        return mTitle;
    }
    public void setTitle(String title) {
        mTitle = title;
    }
    public String getPubDate() {
        return mPubDate;
    }
    public void setPubDate(String pubDate) {
        mPubDate = pubDate;
    }
    public String getUrl() {
        return mUrl;
    }
    public void setUrl(String url) {
        mUrl = url;
    }
    public String getGuid() {
        return mGuid;
    }
    public void setGuid(String guid) {
        mGuid = guid;
    }
    public String getDuration() {
        return mDuration;
    }
    public void setDuration(String duration) {
        mDuration = duration;
    }

    @Override
    public String toString(){
        return mTitle;
    }
}
