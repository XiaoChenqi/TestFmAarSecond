package com.facilityone.wireless.a.arch.base;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

/**
 * Author：gary
 * Email: xuhaozv@163.com
 * description:
 * Date: 2019/4/25 5:29 PM
 */
public class FloatDefault0Adapter implements JsonSerializer<Float>, JsonDeserializer<Float> {
    @Override
    public Float deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            if (json.getAsString().equals("")) {//定义为double类型,如果后台返回"",则返回null
                return null;
            }
        } catch (Exception ignore) {
        }
        try {
            return json.getAsFloat();
        } catch (NumberFormatException e) {
            throw new JsonSyntaxException(e);
        }
    }

    @Override
    public JsonElement serialize(Float src, Type typeOfSrc, JsonSerializationContext context) {
        if(src == src.longValue()){
            return new JsonPrimitive(src.longValue());
        }
        return new JsonPrimitive(src);
    }
}