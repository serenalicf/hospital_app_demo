package com.serena.dataservice.controller;

import com.serena.commonutil.result.Result;
import com.serena.dataservice.service.DictionaryService;
import com.serena.model.model.data.Dictionary;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(value = "Data Dictionary Api")
@RestController
@RequestMapping("admin/dataManagement/dictionary")
@CrossOrigin
public class DictionaryController {
    @Autowired
    private DictionaryService dictionaryService;



    @ApiOperation(value = "find children data by parent id")
    @GetMapping("findChildren/{id}")
    public Result findChildren(@PathVariable Long id) {
        List<Dictionary> dictionaryList = dictionaryService.findChildren(id);
        return Result.ok(dictionaryList);
    }

    @GetMapping("export")
    public void exportData(HttpServletResponse response) {
        dictionaryService.exportData(response);
    }

    @PostMapping("import")
    public Result importData(MultipartFile file) {
        dictionaryService.importData(file);
        return Result.ok();
    }

}
