package com.dpwgc.muranapi;

import java.util.LinkedHashMap;
import java.util.Set;

public class Params {

    private LinkedHashMap<String, String> map = new LinkedHashMap<>();

    public void setMap(LinkedHashMap<String, String> map) {
        this.map = map;
    }

    public LinkedHashMap<String, String> source() {
        return map;
    }

    public String get(String key) {
        return map.get(key);
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

    public void set(String key, String value) {
        map.put(key, value);
    }

    public Set<String> keySet() {
        return map.keySet();
    }
}
