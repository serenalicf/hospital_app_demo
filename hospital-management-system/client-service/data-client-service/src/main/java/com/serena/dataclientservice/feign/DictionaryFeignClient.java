package com.serena.dataclientservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("data-service")
@Repository
public interface DictionaryFeignClient {

    @GetMapping("admin/dataManagement/dictionary/getName/{dictionaryCode}/{value}")
    public String getName(@PathVariable("dictionaryCode") String dictionaryCode, @PathVariable("value") String value);

    @GetMapping("admin/dataManagement/dictionary/getName/{value}")
    public String getName(@PathVariable("value") String value);

}