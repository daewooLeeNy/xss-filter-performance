package com.example;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Map;

@RestController
public class TestController {
    @RequestMapping(value = "/custom/test")
    public @ResponseBody
    Map<String, Object> custom(HttpServletRequest request) {
        return convertToMap(request);
    }

    @RequestMapping(value = "/lucy/test")
    public @ResponseBody
    Map<String, Object> lucy(HttpServletRequest request) {
        return convertToMap(request);
    }

    private Map<String, Object> convertToMap(HttpServletRequest request) {
        Map<String, Object> map = new java.util.HashMap();

        for (Enumeration<String> enums = request.getParameterNames(); enums
                .hasMoreElements(); ) {
            String key = enums.nextElement();
            map.put(key, request.getParameter(key));
        }

        return map;
    }
}
