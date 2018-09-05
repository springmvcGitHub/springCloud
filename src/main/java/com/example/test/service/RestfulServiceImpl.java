package com.example.test.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Title:
 * Description:
 * Copyright:
 * Company:
 * Project: SrpingBootTest
 * Create User: TRS-chen
 * Create Time:2018/8/7 18:04
 */
@Service
public class RestfulServiceImpl {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 测试ribbon，消费者入口
     *
     * @param name
     * @return
     */
    public String getRestData(String name) {
        String url = "http://S2/testB/hello/" + name;
        String resultMsg = restTemplate.getForObject(url, String.class);
        return resultMsg;
    }

    /**
     * ribbon测试,消费者入口，传递jsonObject
     *
     * @return
     */
    public String getListData() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("str", "testStr");
        JSONArray jsonArray = new JSONArray();
        jsonArray.add("arr1");
        jsonArray.add("arr2");
        jsonObject.put("arr", jsonArray);

        String url = "http://S2/testB/getListData";

        MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
        parameters.add("username", "aa");
        parameters.add("password", jsonObject.toString());

        ResponseEntity<String> response = restTemplate.postForEntity(url, parameters, String.class);
        String body = response.getBody();
        return body;
    }
}
