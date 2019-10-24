package com.wifi.wifiscanner.util;

import com.google.gson.GsonBuilder;
import com.wifi.wifiscanner.dto.Report;
import com.wifi.wifiscanner.serialization.CalendarAdapter;

import java.util.GregorianCalendar;

public class Serializer {

    private static final GsonBuilder REPORT_BUILDER = new GsonBuilder().registerTypeAdapter(GregorianCalendar.class, new CalendarAdapter());
    private static final GsonBuilder DEFAULT_BUILDER = new GsonBuilder();

    public static String reportSerialize(Report o) {
        return REPORT_BUILDER.create().toJson(o);
    }

    public static String serialize(Object o) {
        return DEFAULT_BUILDER.create().toJson(o);
    }

    public static <T> T deserialize(String str, Class<T> clazz) {
        return DEFAULT_BUILDER.create().fromJson(str, clazz);
    }
}
