package com.wifi.wifiscanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Serializer {

  public static String Serialize(Object o) {
    GsonBuilder gsonBuilder = new GsonBuilder();
    Gson gson = gsonBuilder.create();
    return gson.toJson(o);
  }

  public static <T> T Deserialize(String str, Class<T> clazz) {
    GsonBuilder gsonBuilder = new GsonBuilder();
    Gson gson = gsonBuilder.create();
    return gson.fromJson(str, clazz);
  }
}
