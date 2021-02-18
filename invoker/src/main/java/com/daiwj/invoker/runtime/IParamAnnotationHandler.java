package com.daiwj.invoker.runtime;

import android.text.TextUtils;

import com.daiwj.invoker.annotation.FileParam;
import com.daiwj.invoker.annotation.Param;
import com.daiwj.invoker.annotation.ParamMap;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

/**
 * author: daiwj on 2020/12/15 20:43
 */
public interface IParamAnnotationHandler<T> {

    void handle(Annotation source, Object arg, List<T> list);

    IParamAnnotationHandler<RequestParam> PARAM = new IParamAnnotationHandler<RequestParam>() {

        @Override
        public void handle(Annotation source, Object arg, List<RequestParam> list) {
            Param target = (Param) source;
            String name = target.value();
            if (TextUtils.isEmpty(name)) {
                InvokerUtil.error("@Param: name cannot be null!");
            }
            String value = arg != null ? arg.toString() : null;
            boolean encode = target.encode();
            list.add(new RequestParam(name, value, encode));
        }
    };

    IParamAnnotationHandler<RequestParam> PARAM_MAP = new IParamAnnotationHandler<RequestParam>() {

        @Override
        public void handle(Annotation source, Object arg, List<RequestParam> list) {
            ParamMap target = (ParamMap) source;
            boolean encode = target.encode();
            if (arg == null) {
                return;
            }
            if (!(arg instanceof Map)) {
                InvokerUtil.error("@ParamMap: value must be a sub type of Map!");
            }
            Map<String, ?> map = (Map<String, ?>) arg;
            for (Map.Entry<String, ?> entry : map.entrySet()) {
                list.add(new RequestParam(entry.getKey(), entry.getValue().toString(), encode));
            }
        }

    };

    IParamAnnotationHandler<FilePart> FILE = new IParamAnnotationHandler<FilePart>() {

        @Override
        public void handle(Annotation source, Object arg, List<FilePart> list) {
            FileParam target = (FileParam) source;
            if (arg == null) {
                return;
            }
            String name = target.value();
            String fileName = target.fileName();
            list.add(new FilePart(name, fileName, arg.toString()));
        }

    };

}
