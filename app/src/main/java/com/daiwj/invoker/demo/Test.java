package com.daiwj.invoker.demo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * author: daiwj on 2020/12/5 12:28
 */
public class Test {

    public static String success() {
        JSONObject root = new JSONObject();
        root.put("status", 1);
        root.put("code", 200);
        root.put("message", "ok");

        JSONObject textInfo = new JSONObject();
        textInfo.put("text", "这是一条请求成功后的数据");

        root.put("data", textInfo);

        return root.toString();
    }

    public static String successList() {
        JSONObject root = new JSONObject();
        root.put("status", 1);
        root.put("code", 200);
        root.put("message", "ok");

        JSONArray array = new JSONArray();
        JSONObject data1 = new JSONObject();
        data1.put("text", "第0条数据");
        array.add(data1);
        JSONObject data2 = new JSONObject();
        data2.put("text", "第1条数据");
        array.add(data2);

        root.put("data", array);

        return root.toString();
    }

    public static String failure() {
        JSONObject root = new JSONObject();
        root.put("status", 0);
        root.put("code", 400);
        root.put("message", "业务错误");
        return root.toString();
    }

    public static String ruBan() {
        String content = "{" +
                "\"status\": 1," +
                "\"code\": 0," +
                "\"message\": \"\"," +
                "\"data\": {" +
                "    \"dataList\": [" +
                "      {" +
                "        \"code\": \"101352991025\"," +
                "        \"key\": \"appXRHPAtmosphere\"," +
                "        \"list\": [" +
                "          {" +
                "            \"iconZC\": \"https://img.gegejia.com/newQqbs/2e636486d2b16.png\"," +
                "            \"width\": 270," +
                "            \"height\": 48," +
                "            \"titleBrand\": \"品牌馆\"," +
                "            \"subTitleBrand\": \"更多品牌好物\"," +
                "            \"urlBrand\": \"http://www.baidu.com\"," +
                "            \"shadow\": true," +
                "            \"id\": 4962," +
                "            \"st\": \"2020-12-01T11:34:50.000+0800\"," +
                "            \"et\": \"2038-01-19T11:14:07.000+0800\"," +
                "            \"gcm\": \"2.1.2514a3ada807800.f3256c7df94d724.203.0.26_4962.\"" +
                "          }" +
                "        ]" +
                "      }," +
                "      {" +
                "        \"code\": \"100427929236\"," +
                "        \"key\": \"appXRIndexBanner\"," +
                "        \"list\": [" +
                "          {" +
                "            \"url\": \"https://img.gegejia.com/newQqbs/6990d4fc9696.png\"," +
                "            \"width\": 1125," +
                "            \"height\": 1074," +
                "            \"href\": \"https://h5test.selfshero.com/live/home\"," +
                "            \"accessibility\": \"1\"," +
                "            \"id\": 4756," +
                "            \"st\": \"2020-11-12T11:07:18.000+0800\"," +
                "            \"et\": \"2038-01-19T11:14:07.000+0800\"," +
                "            \"gcm\": \"2.1.2514a3adc407800.f3256c7df94d724.203.0.26_4756.\"" +
                "          }," +
                "          {" +
                "            \"url\": \"https://img.gegejia.com/newQqbs/11f76f5fe9614.png\"," +
                "            \"width\": 1125," +
                "            \"height\": 1074," +
                "            \"href\": \"https://h5test.selfshero.com/offline/activity/calendar/list\"," +
                "            \"id\": 4757," +
                "            \"st\": \"2020-11-12T11:07:18.000+0800\"," +
                "            \"et\": \"2038-01-19T11:14:07.000+0800\"," +
                "            \"gcm\": \"2.1.2514a3adc407801.f3256c7df94d724.203.0.26_4757.\"" +
                "          }" +
                "        ]" +
                "      }" +
                "    ]" +
                "  }" +
                "}";
        return content;
    }

}
