package com.daiwj.invoker.demo;

import com.daiwj.invoker.annotation.Factory;
import com.daiwj.invoker.annotation.Get;
import com.daiwj.invoker.annotation.Header;
import com.daiwj.invoker.annotation.Param;
import com.daiwj.invoker.annotation.Post;
import com.daiwj.invoker.demo.okhttp.TestInvokerFactory;
import com.daiwj.invoker.runtime.DataCaller;
import com.daiwj.invoker.runtime.SourceCaller;

import java.util.List;

/**
 * author: daiwj on 2020/12/2 21:50
 */
@Factory(TestInvokerFactory.class)
interface TestApi {

    @Header(name = "invoker-user-agent", value = "Invoker")
    @Get("platform/api/sourceCall")
    SourceCaller sourceCall(
            @Param("name") String name,
            @Param("password") String password
    );

    @Header(name = "invoker-user-agent", value = "Invoker")
    @Get("platform/api/dataCall")
    DataCaller<TestInfo> dataCall(
            @Param("name") String name,
            @Param("password") String password
    );

    @Header(name = "invoker-user-agent", value = "Invoker")
    @Post("platform/api/stringListCall")
    DataCaller<List<String>> stringListCall(
            @Param("name") String name,
            @Param("password") String password
    );

}
