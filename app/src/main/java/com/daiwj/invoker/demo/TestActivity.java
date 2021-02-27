package com.daiwj.invoker.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.daiwj.invoker.Invoker;
import com.daiwj.invoker.demo.okhttp.TestCallback;
import com.daiwj.invoker.demo.okhttp.TestError;
import com.daiwj.invoker.demo.okhttp.TestFailure;
import com.daiwj.invoker.runtime.SuccessResult;

import java.util.List;


public class TestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_activity);

        findViewById(R.id.btn_test_SourceCaller).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Invoker.invoke(TestApi.class)
                        .sourceCall("MeepoKing", "123456")
                        .asDynamic()
                        .mock(Test.success())
                        .call(getActivity(), new TestCallback<String>() {
                            @Override
                            public void onSuccess(SuccessResult<String> result) {
                                toast(result.getData());
                            }

                            @Override
                            public void onFail(TestFailure failure) {
                                toast("fail: " + failure.getMessage());
                            }

                            @Override
                            public void onError(TestError error) {
                                toast("error: " + error.getMessage());
                            }
                        });
            }
        });

        findViewById(R.id.btn_test_DataCaller_TestInfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Invoker.invoke(TestApi.class)
                        .dataCall("MeepoKing", "123456")
                        .mock(Test.success())
                        .call(getActivity(), new TestCallback<TestInfo>() {
                            @Override
                            public void onSuccess(SuccessResult<TestInfo> result) {
                                toast(result.getData().toString());
                            }

                            @Override
                            public void onFail(TestFailure failure) {
                                toast("fail: " + failure.getMessage());
                            }

                            @Override
                            public void onError(TestError error) {
                                toast("error: " + error.getMessage());
                            }
                        });
            }
        });

        findViewById(R.id.btn_test_DataCaller_List).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Invoker.invoke(TestApi.class)
                        .listCall("MeepoKing", "123456")
                        .mock(Test.successList())
                        .call(getActivity(), new TestCallback<List<TestInfo>>() {
                            @Override
                            public void onSuccess(SuccessResult<List<TestInfo>> result) {
                                toast(result.getData().toString());
                            }

                            @Override
                            public void onFail(TestFailure failure) {
                                toast("fail: " + failure.getMessage());
                            }

                            @Override
                            public void onError(TestError error) {
                                toast("error: " + error.getMessage());
                            }
                        });
            }
        });
    }

    public Activity getActivity() {
        return this;
    }

    private void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

}