package com.serena.dataservice.service;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.serena.dataservice.listener.DictionaryListener;
import com.serena.dataservice.mapper.DictionaryMapper;
import com.serena.model.vo.data.DictionaryDto;
import com.serena.model.model.data.Dictionary;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DictionaryServiceImpl extends ServiceImpl<DictionaryMapper, Dictionary> implements DictionaryService {

    @Override
    @Cacheable(value = "dictionary", keyGenerator = "keyGenerator")
    public List<Dictionary> findChildren(Long id) {

        QueryWrapper<Dictionary> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", id);
        List<Dictionary> dictionaryList = baseMapper.selectList(queryWrapper);

        for (Dictionary dictionary : dictionaryList) {
            Long dictionaryId = dictionary.getId();
            System.out.println("Calling hasChildren for ID: " + dictionaryId);
            boolean hasChildren = this.hasChildren(dictionaryId);
            dictionary.setHasChildren(hasChildren);
        }
        return dictionaryList;
    }



    @Override
    public void exportData(HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String fileName = "dictionary";
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

            List<Dictionary> dictionaryList = baseMapper.selectList(null);
            //convert dictionaryList to DictionaryDto
            List<DictionaryDto> dictionaryDtoList = new ArrayList<>();
            for(Dictionary dictionary : dictionaryList) {
                DictionaryDto dictionaryDto = new DictionaryDto();
                BeanUtils.copyProperties(dictionary, dictionaryDto); //dictionaryDto.setId(dictionary.getId());
                dictionaryDtoList.add(dictionaryDto);
            }

            EasyExcel.write(response.getOutputStream(), DictionaryDto.class).sheet("dictionary")
                    .doWrite(dictionaryDtoList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @CacheEvict(value = "dictionary", allEntries = true)
    public void importData(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), DictionaryDto.class, new DictionaryListener((baseMapper)))
                    .sheet()
                    .doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName(String dictionaryCode, String value) {
        if(StringUtils.isEmpty(dictionaryCode)) {
            Dictionary dictionary = baseMapper.selectOne(new QueryWrapper<Dictionary>().eq("value", value));
            return dictionary.getName();
        } else {

            //get dictionary id by dictionaryCode
            Dictionary dictionary = this.getDictionaryByDictionaryCode(dictionaryCode);
            Long parentId = dictionary.getId();

            //search by parentId and value
            Dictionary finalDictionary = baseMapper.selectOne(new QueryWrapper<Dictionary>()
                    .eq("parent_id", parentId)
                    .eq("value", value));
            return finalDictionary.getName();
        }
    }

    @Override
    public List<Dictionary> findByDictionaryCode(String dictionaryCode) {
        // find id by dictionaryCode
        Dictionary dictionary = this.getDictionaryByDictionaryCode(dictionaryCode);

        // find next node by id
        List<Dictionary> dictionaryList = this.findChildren(dictionary.getId());
        return dictionaryList;
    }

    private Dictionary getDictionaryByDictionaryCode(String dictionaryCode) {
        QueryWrapper<Dictionary> queryWrapper = new QueryWrapper<>();
        //get dictionary id by dictionaryCode
        queryWrapper.eq("dictionary_code", dictionaryCode);
        Dictionary dictionary = baseMapper.selectOne(queryWrapper);
        return dictionary;
    }


    private boolean hasChildren(Long id) {
        QueryWrapper<Dictionary> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", id);
        Integer count = baseMapper.selectCount(queryWrapper);
        return count > 0;
    }

}
