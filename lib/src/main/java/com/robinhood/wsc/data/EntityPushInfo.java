package com.robinhood.wsc.data;

public class EntityPushInfo {

    private String text;
    private Integer start;
    private Integer interval;
    private Integer max_count;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public Integer getMax_count() {
        return max_count;
    }

    public void setMax_count(Integer max_count) {
        this.max_count = max_count;
    }
}
