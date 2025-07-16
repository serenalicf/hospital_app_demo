package com.serena.dataservice.controller;

import com.serena.dataservice.service.DictionaryService;
import com.serena.model.model.data.Dictionary;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/redis")
@Api(value = "test")
public class RedisTestController {

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedisTemplate<Object, Object> objectRedisTemplate;

    @GetMapping("/test/{id}")
    public Map<String, Object> testCache(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();

        System.out.println("First call to findChildren(" + id + ")");
        long start = System.currentTimeMillis();
        List<Dictionary> list1 = dictionaryService.findChildren(id);
        long firstTime = System.currentTimeMillis() - start;

        System.out.println("Second call to findChildren(" + id + ")");
        start = System.currentTimeMillis();
        List<Dictionary> list2 = dictionaryService.findChildren(id);
        long secondTime = System.currentTimeMillis() - start;

        // Check various key patterns
        Set<String> allKeys = redisTemplate.keys("*");
        Set<String> dictionaryKeys = redisTemplate.keys("*dictionary*");
        Set<String> findChildrenKeys = redisTemplate.keys("*findChildren*");
        Set<String> serviceKeys = redisTemplate.keys("*DictionaryService*");

        result.put("firstCallTime", firstTime + "ms");
        result.put("secondCallTime", secondTime + "ms");
        result.put("speedImprovement", firstTime > 0 ? (double)firstTime/secondTime : 0);
        result.put("cacheHit", secondTime < firstTime);
        result.put("resultSize", list1.size());
        result.put("allKeys", allKeys);
        result.put("dictionaryKeys", dictionaryKeys);
        result.put("findChildrenKeys", findChildrenKeys);
        result.put("serviceKeys", serviceKeys);

        // Try a more specific key pattern that might match our cache key
        String expectedKeyPattern = "*DictionaryServiceImpl:findChildren:" + id + "*";
        result.put("specificPatternKeys", redisTemplate.keys(expectedKeyPattern));

        return result;
    }

    @GetMapping("/manual-check/{id}")
    public Map<String, Object> manualCacheCheck(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();

        // Try various cache key patterns that might match
        String[] possibleKeyPatterns = {
                "dictionary::com.serena.dataservice.service.DictionaryServiceImpl:findChildren:" + id + ":",
                "dictionary::" + id,
                "dictionary::DictionaryServiceImpl:findChildren:" + id,
                "dictionary::com.serena.dataservice.service.DictionaryServiceImpl*"
        };

        for (String pattern : possibleKeyPatterns) {
            result.put("pattern_" + pattern, redisTemplate.keys(pattern));
        }

        return result;
    }

    @GetMapping("/redis-info")
    public Map<String, Object> getRedisInfo() {
        Map<String, Object> result = new HashMap<>();

        // Get all keys in Redis
        Set<String> allKeys = redisTemplate.keys("*");
        result.put("totalKeys", allKeys.size());
        result.put("allKeys", allKeys);

        // Get Redis stats
        String info = redisTemplate.execute((RedisCallback<String>) connection ->
                String.valueOf(connection.info()));
        result.put("redisInfo", info);

        return result;
    }
}