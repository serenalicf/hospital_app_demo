package com.serena.dataservice.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.serena.dataservice.mapper.DictionaryMapper;
import com.serena.model.dto.data.DictionaryDto;
import com.serena.model.model.data.Dictionary;
import org.springframework.beans.BeanUtils;

public class DictionaryListener extends AnalysisEventListener<DictionaryDto> {
    private DictionaryMapper dictionaryMapper;

    public DictionaryListener(DictionaryMapper dictionaryMapper) {
        this.dictionaryMapper = dictionaryMapper;
    }

    @Override
    public void invoke(DictionaryDto dictionaryDto, AnalysisContext analysisContext) {
        //read line by line
        Dictionary dictionary = new Dictionary();
        BeanUtils.copyProperties(dictionaryDto, dictionary);
        dictionaryMapper.insert(dictionary);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
