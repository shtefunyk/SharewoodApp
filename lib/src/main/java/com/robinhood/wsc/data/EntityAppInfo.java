package com.robinhood.wsc.data;

public class EntityAppInfo {

    private String url;
    private String url_invisible;
    private Boolean save_last_url;
    private EntityPushInfo push;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getSave_last_url() {
        return save_last_url;
    }

    public void setSave_last_url(Boolean save_last_url) {
        this.save_last_url = save_last_url;
    }

    public EntityPushInfo getPush() {
        return push;
    }

    public void setPush(EntityPushInfo push) {
        this.push = push;
    }

    public String getUrl_invisible() {
        return url_invisible;
    }

    public void setUrl_invisible(String url_invisible) {
        this.url_invisible = url_invisible;
    }
}
