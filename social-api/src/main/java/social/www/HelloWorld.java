package social.www;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import social.repository.TestRepository;

import java.util.Map;

@RestController
public class HelloWorld {

    private final TestRepository testRepository;

    @Autowired
    public HelloWorld(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    @GetMapping("/hello")
    public Map<String, String> sayHello(@RequestParam(name="name", required=false, defaultValue="World") String name) {
        Map<String, String> map = Maps.newHashMap();
        map.put("woot", "foo");
        map.put("boo", "blah");
        map.put("name", name);
        return map;
    }

    @GetMapping("/count")
    public Long  count() {
        return testRepository.dummy();
    }
}
