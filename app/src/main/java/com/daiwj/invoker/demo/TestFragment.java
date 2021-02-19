package com.daiwj.invoker.demo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.daiwj.invoker.Invoker;
import com.daiwj.invoker.demo.okhttp.TestCallback;
import com.daiwj.invoker.runtime.SuccessResult;

import java.util.List;

/**
 * author: daiwj on 1/12/21 19:13
 */
public class TestFragment extends Fragment {

    @Nullable
    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_test_androidx_fragment, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        findViewById(R.id.btn_test_SourceCaller).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Invoker.invoke(TestApi.class)
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
                Invoker.invoke(TestApi.class)
                        .dataCall("MeepoKing", "123456")
                        .asDynamic()
                        .setRelativeUrl("")
                        .addParam("", "")
                        .addHeader("", "")
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
                Invoker.invoke(TestApi.class)
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
    }

    private View findViewById(@IdRes int id) {
        return getView().findViewById(id);
    }

    private void toast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }


}
