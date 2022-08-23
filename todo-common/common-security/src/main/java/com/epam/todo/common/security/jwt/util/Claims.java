package com.epam.todo.common.security.jwt.util;

import com.alibaba.fastjson.JSONObject;

public class Claims {

    private JSONObject claimJson;

    public void put(String key, Object value) {
        claimJson.put(key, value);
    }

    public Object get(String key) {
        return claimJson.get(key);
    }

    public String getString(String key) {
        return claimJson.getString(key);
    }

    public Long getLong(String key) {
        return claimJson.getLong(key);
    }

    protected void setClaimJson(JSONObject jsonObject) {
        this.claimJson = jsonObject;
    }
}
