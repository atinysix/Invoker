package com.daiwj.invoker.parser;

import com.alibaba.fastjson.JSON;
import com.daiwj.invoker.runtime.DataParser;
import com.daiwj.invoker.runtime.ParserFactory;
import com.daiwj.invoker.runtime.StringParser;

import java.lang.reflect.Type;

/**
 * author: daiwj on 2020/12/5 12:13
 */
public class FastJsonParserFactory implements ParserFactory {

    @Override
    public DataParser dataParser() {
        return new FastJsonDataParser();
    }

    @Override
    public StringParser stringParser() {
        return new FastStringParser();
    }

    private static class FastJsonDataParser implements DataParser {

        @Override
        public <T> T parse(String from, Type c) {
            try {
                return JSON.parseObject(from, c);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private static class FastStringParser implements StringParser {

        @Override
        public String parse(Object from) {
            try {
                return JSON.toJSONString(from);
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }
    }

}
