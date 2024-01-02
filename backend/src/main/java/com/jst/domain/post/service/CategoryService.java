package com.jst.domain.post.service;
import com.jst.common.Common;
import com.jst.common.ResultVo;
import com.jst.domain.post.repository.CategoryEntity;
import com.jst.domain.post.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    public ResponseEntity getCategorys(){
        List<CategoryEntity> categoryList =categoryRepository.findAll();
        return ResponseEntity.ok(new ResultVo(Common.ResultErrorCode.SUCCESS,"",categoryList));
    }
}
