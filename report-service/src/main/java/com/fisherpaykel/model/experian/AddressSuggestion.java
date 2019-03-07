package com.fisherpaykel.model.experian;

import java.util.List;

public class AddressSuggestion {

    String suggestion;
    String format;


    List<Object> matched;

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public List<Object> getMatched() {
        return matched;
    }

    public void setMatched(List<Object> matched) {
        this.matched = matched;
    }

}
