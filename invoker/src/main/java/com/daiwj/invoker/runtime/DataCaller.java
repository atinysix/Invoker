package com.daiwj.invoker.runtime;

/**
 * author: daiwj on 2020/12/3 13:32
 */
public class DataCaller<Data> extends StaticCaller<Data> {

    public DataCaller(MethodVisitor<Data> visitor) {
        super(visitor);
    }

    public SourceCaller asSource() {
        return new SourceCaller(getMethodVisitor().convert(SourceCaller.class));
    }

}
