package social.www;

import com.google.common.collect.Maps;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HelloWorld {

    @GetMapping("v1.0//hello")
    public Map<String, String> sayHello(@RequestParam(name = "name", required = false, defaultValue = "World") String name) {
        Map<String, String> map = Maps.newHashMap();
        map.put("woot", "foo");
        map.put("boo", "blah");
        map.put("name", name);
        return map;
    }

}
