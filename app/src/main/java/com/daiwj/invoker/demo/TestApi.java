package com.daiwj.invoker.demo;

import com.daiwj.invoker.annotation.CallFactory;
import com.daiwj.invoker.annotation.Provider;
import com.daiwj.invoker.annotation.Get;
import com.daiwj.invoker.annotation.Header;
import com.daiwj.invoker.annotation.Param;
import com.daiwj.invoker.annotation.Post;
import com.daiwj.invoker.annotation.SourceFactory;
import com.daiwj.invoker.call.okhttp3.OkHttpCallFactory;
import com.daiwj.invoker.demo.okhttp.TestCallFactory;
import com.daiwj.invoker.demo.okhttp.TestInvokerProvider;
import com.daiwj.invoker.demo.okhttp.TestSource;
import com.daiwj.invoker.runtime.DataCaller;
import com.daiwj.invoker.runtime.SourceCaller;

import java.util.List;

/**
 * author: daiwj on 2020/12/2 21:50
 */
@Provider(TestInvokerProvider.class)
interface TestApi {

    @Header(name = "invoker-user-agent", value = "Invoker")
    @Get("platform/api/sourceCall")
    SourceCaller sourceCall(
            @Param("name") String name,
            @Param("password") String password
    );

    @Header(name = "invoker-user-agent", value = "Invoker")
    @Get("platform/api/dataCall")
    @SourceFactory(TestSource.class)
//    @CallFactory(TestCallFactory.class)
    DataCaller<TestInfo> dataCall(
            @Param("name") String name,
            @Param("password") String password
    );

    @Header(name = "invoker-user-agent", value = "Invoker")
    @Post("platform/api/stringListCall")
    DataCaller<List<TestInfo>> listCall(
            @Param("name") String name,
            @Param("password") String password
    );

}
