package com.daiwj.invoker.parser;

import com.daiwj.invoker.runtime.DataParser;
import com.daiwj.invoker.runtime.Parser;
import com.daiwj.invoker.runtime.StringParser;
import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * author: daiwj on 2020/12/25 14:02
 */
public class GsonParserFactory implements Parser.Factory {

    private Gson mGson;

    public GsonParserFactory() {
        mGson = new Gson();
    }

    @Override
    public DataParser dataParser() {
        return new GsonDataParser();
    }

    @Override
    public StringParser stringParser() {
        return new GsonStringParser();
    }

    private class GsonDataParser implements DataParser {

        @Override
        public <T> T parse(String from, Type c) {
            try {
                return mGson.fromJson(from, c);
            } catch (Exception e) {
                return null;
            }
        }
    }

    private class GsonStringParser implements StringParser {

        @Override
        public String parse(Object from) {
            try {
                return mGson.toJson(from);
            } catch (Exception e) {
                return null;
            }
        }
    }
}
