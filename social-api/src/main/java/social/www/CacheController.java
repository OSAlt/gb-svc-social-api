package social.www;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class CacheController {

   private final CacheManager cacheManager;

   @Autowired
    public CacheController(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @RequestMapping(value = "v1.0/cache/clear", method = RequestMethod.GET)
    public String  clearCache() {
        for (String cacheName : cacheManager.getCacheNames()) {
            try {
                cacheManager.getCache(cacheName).clear();
            }catch (Exception e) {
                log.error("Failed to clear cache: {}", cacheName);
            }
        }

        return String.format("Cleared %s caches", cacheManager.getCacheNames().size());

    }


}
