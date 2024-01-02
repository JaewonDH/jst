package com.jst.domain.post.service;

import com.jst.common.Common;
import com.jst.common.ResultVo;
import com.jst.common.error.ErrorCode;
import com.jst.domain.file.service.FileService;
import com.jst.domain.member.repository.UserEntity;
import com.jst.domain.member.repository.UserRepository;
import com.jst.domain.post.dto.PostDto;
import com.jst.domain.post.repository.CategoryEntity;
import com.jst.domain.post.repository.CategoryRepository;
import com.jst.domain.post.repository.PostEntity;
import com.jst.domain.post.repository.PostRepository;
import com.jst.exception.BaseException;
import jakarta.transaction.Transactional;
import oracle.jdbc.proxy.annotation.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    FileService fileService;

    public ResponseEntity insertPost(PostDto postDto, List<MultipartFile> files) {
        Optional<UserEntity> optional=userRepository.findById(getUserId());
        UserEntity userEntity = optional.orElseThrow(()-> new BaseException(ErrorCode.BAD_REQUEST,"사용자 아이디가 없습니다."));

        if(files!=null){
            fileService.createFiles(files);
        }

        Optional<CategoryEntity> optionalCategory=categoryRepository.findById(String.valueOf(postDto.getCategoryId()));
        CategoryEntity categoryEntity = optionalCategory.orElseThrow(()-> new BaseException(ErrorCode.BAD_REQUEST,"사용자 아이디가 없습니다."));

        PostEntity postEntity = PostEntity.builder()
                .title(postDto.getTitle())
                .createdBy(userEntity)
                .updateBy(userEntity)
                .category(categoryEntity)
                .contents(postDto.getContents())
                .build();

        postRepository.save(postEntity);
        return ResponseEntity.ok(new ResultVo(Common.ResultErrorCode.SUCCESS,"저장 완료"));
    }

    public ResponseEntity getPost() {
        List list=postRepository.findAll();
        return ResponseEntity.ok(new ResultVo(Common.ResultErrorCode.SUCCESS,"",list));
    }

    private String getUserId(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
