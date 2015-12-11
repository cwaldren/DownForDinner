package com.caseywaldren.downfordinner.data;

import com.parse.ParseObject;

/**
 * Created by William on 12/11/2015.
 */
public class Choice {

    private String title;
    private String time;
    private ParseObject object;

    public Choice(ParseObject object) {
        this.title = object.get("restaurant").toString();
        this.time = object.get("time").toString();
        this.object = object;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ParseObject getObject() {
        return object;
    }

    public void setObject(ParseObject object) {
        this.object = object;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
