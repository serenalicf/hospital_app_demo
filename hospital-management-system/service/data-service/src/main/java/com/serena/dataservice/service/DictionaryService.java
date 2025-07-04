package com.serena.dataservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.serena.model.model.data.Dictionary;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface DictionaryService extends IService<Dictionary> {
    List<Dictionary> findChildren(Long id);


    void exportData(HttpServletResponse response);

    void importData(MultipartFile file);

}
