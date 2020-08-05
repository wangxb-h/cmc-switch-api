package com.feihu.controller;

import com.alibaba.fastjson.JSONObject;
import com.feihu.common.JsonData;
import com.feihu.common.RedisUtil;
import com.feihu.entity.TypeInfo;
import com.feihu.service.TypeService;
import com.feihu.utils.RedisUse;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("type")
@CrossOrigin
@Api(description = "类型Controller")
public class TypeController {
    @Autowired
    private TypeService typeService;

    @RequestMapping("findType")
    public JsonData findType() {
        System.out.println("hellow word");
        if (RedisUse.get("type_json_wxb") != null) {
            String type_json_wxb = RedisUse.get("type_json_wxb");
            return JsonData.getJsonSuccess(type_json_wxb);
        } else {
            List<TypeInfo> list = typeService.findType();
            String string = JSONObject.toJSONString(list);
            RedisUse.set("type_json_wxb", string);
            return JsonData.getJsonSuccess(list);
        }
    }

    @RequestMapping("toUpdate")
    public Map toUpdate(Integer id) {
        Map map = new HashMap();
        try {
            TypeInfo typeInfo = typeService.toUpdate(id);
            map.put("data", typeInfo);
            map.put("message", "回显成功");
            map.put("code", 200);
        } catch (Exception e) {
            map.put("message", "回显失败");
            map.put("code", 500);
        }
        return map;
    }

    @RequestMapping("update")
    public Map update(TypeInfo typeInfo) {
        Map map = new HashMap();
        try {
            typeService.update(typeInfo);
            map.put("message", "修改成功");
            map.put("code", 200);
        } catch (Exception e) {
            map.put("message", "修改失败");
            map.put("code", 500);
        }
        return map;
    }

    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public Map delete(Integer id) {
        Map map = new HashMap();
        try {
            typeService.delete(id);
            map.put("message", "删除成功");
            map.put("code", 200);
        } catch (Exception e) {
            map.put("message", "删除失败");
            map.put("code", 500);
        }
        return map;
    }
}
