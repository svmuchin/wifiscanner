package com.wifi.wifiscanner.rest.header;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HeaderElement;
import cz.msebera.android.httpclient.ParseException;

public class SimpleHeader implements Header {

    private String name;
    private String value;
    private HeaderElement[] elements = new HeaderElement[0];

    public SimpleHeader(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public SimpleHeader(String name, String value, HeaderElement[] elements) {
        this.name = name;
        this.value = value;
        this.elements = elements;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public HeaderElement[] getElements() throws ParseException {
        return this.elements;
    }
}
