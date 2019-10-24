package com.wifi.wifiscanner.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Calendar;

public class CalendarAdapter implements JsonSerializer<Calendar> {
    @Override
    public JsonElement serialize(Calendar src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getTimeInMillis());
    }
}
