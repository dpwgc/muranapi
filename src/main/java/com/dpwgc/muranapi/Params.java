package com.dpwgc.muranapi;

import java.util.LinkedHashMap;
import java.util.Set;

public class Params {

    private final LinkedHashMap<String, String> map = new LinkedHashMap<>();

    public Params() {

    }

    public Params(String key, String value) {
        map.put(key, value);
    }

    public LinkedHashMap<String, String> map() {
        return map;
    }

    public Integer getInt(String key) {
        try {
            return Integer.parseInt(map.get(key));
        } catch (Throwable e) {
            return null;
        }
    }

    public Long getLong(String key) {
        try {
            return Long.parseLong(map.get(key));
        } catch (Throwable e) {
            return null;
        }
    }

    public Double getDouble(String key) {
        try {
            return Double.parseDouble(map.get(key));
        } catch (Throwable e) {
            return null;
        }
    }

    public String[] getArray(String key, String split) {
        if (!map.containsKey(key)) {
            return null;
        }
        return map.get(key).split(split);
    }

    public Params set(String key, String value) {
        map.put(key, value);
        return this;
    }

    public String get(String key) {
        return map.get(key);
    }

    public Set<String> keySet() {
        return map.keySet();
    }
}
