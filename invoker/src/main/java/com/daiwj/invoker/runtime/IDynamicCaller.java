package com.daiwj.invoker.runtime;

import java.util.HashMap;
import java.util.Map;

/**
 * author: daiwj on 4/21/21 21:55
 */
public interface IDynamicCaller<Data> extends Caller<Data> {

    IDynamicCaller<Data> setHttpMethod(String method);

    IDynamicCaller<Data> setRelativeUrl(String url);

    IDynamicCaller<Data> addParam(String name, String value);

    IDynamicCaller<Data> addParam(String name, String value, boolean encode);

    IDynamicCaller<Data> addParam(HashMap<String, Object> map);

    IDynamicCaller<Data> addParam(Map<String, Object> map, boolean encode);

    IDynamicCaller<Data> addHeader(String name, String value);

    IDynamicCaller<Data> addHeader(Map<String, String> headers);

    IDynamicCaller<Data> setSourceType(Class<? extends ISource> sourceType);

    IDynamicCaller<Data> setCallFactory(Call.CallFactory factory);

    @Override
    IDynamicCaller<Data> mock(String content);
}
