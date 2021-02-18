package com.daiwj.invoker.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.daiwj.invoker.Invoker;
import com.daiwj.invoker.demo.okhttp.TestCallback;
import com.daiwj.invoker.runtime.SuccessResult;

import java.util.List;


public class TestFragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_fragment_activity);

        findViewById(R.id.btn_test_SourceCaller).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Invoker.provide(TestApi.class)
                        .sourceCall("MeepoKing", "123456")
                        .mock(Test.success())
                        .call(getActivity(), new TestCallback<String>() {
                            @Override
                            public void onSuccess(SuccessResult<String> result) {
                                toast(result.getData());
                            }
                        });
            }
        });

        findViewById(R.id.btn_test_DataCaller_TestInfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Invoker.provide(TestApi.class)
                        .dataCall("MeepoKing", "123456")
                        .mock(Test.success())
                        .call(getActivity(), new TestCallback<TestInfo>() {
                            @Override
                            public void onSuccess(SuccessResult<TestInfo> result) {
                                toast(result.getData().toString());
                            }
                        });
            }
        });

        findViewById(R.id.btn_test_DataCaller_List).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Invoker.provide(TestApi.class)
                        .stringListCall("MeepoKing", "123456")
                        .mock(Test.successList())
                        .call(getActivity(), new TestCallback<List<String>>() {
                            @Override
                            public void onSuccess(SuccessResult<List<String>> result) {
                                toast(result.getData().toString());
                            }
                        });
            }
        });

        getSupportFragmentManager().
                beginTransaction().
                replace(R.id.fragment_container, new TestFragment())
                .commitAllowingStateLoss();
    }

    private Activity getActivity() {
        return this;
    }

    private void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

}