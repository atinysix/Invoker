package com.daiwj.invoker.runtime;

/**
 * author: daiwj on 2020/12/3 13:32
 */
public class SourceCaller extends StaticCaller<String> {

    private boolean mDataOnly;

    public SourceCaller(MethodVisitor<String> visitor) {
        super(visitor);
    }

    public SourceCaller dataOnly() {
        mDataOnly = true;
        return this;
    }

    public boolean isDataOnly() {
        return mDataOnly;
    }
}
