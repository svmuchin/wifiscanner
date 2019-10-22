package com.wifi.wifiscanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

public class Serializer {

  public static String serialize(Object o) {
    GsonBuilder gsonBuilder = new GsonBuilder();
    Gson gson = gsonBuilder.create();
    return gson.toJson(o);
  }

  public static <T> T deserialize(String str, Class<T> clazz) {
    GsonBuilder gsonBuilder = new GsonBuilder();
    Gson gson = gsonBuilder.create();
    return gson.fromJson(str, clazz);
  }
}
