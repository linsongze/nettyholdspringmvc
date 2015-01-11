package org.imlsz.nettyspringmvc.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.Serializable;

@Controller
public class TestController {
    //@Autowired
    RedisTemplate<String, Serializable> redisTemplate ;


	@RequestMapping("/foo")
	@ResponseBody
	public String handleFoo() {
		return "Hello world";
	}

    @RequestMapping(value="/foo2",method= RequestMethod.DELETE)
    @ResponseBody
    public String handleFoo2() {
        return "Hello world";
    }


    @RequestMapping(value="/foo3",method= RequestMethod.GET)
    @ResponseBody
    public String handleFoo3() {

        ValueOperations<String, Serializable> ops = null;
        ops = redisTemplate.opsForValue();
        ops.set("1","2");

        return  ops.get("1").toString();
    }
}
