package com.jst.domain.post.controller;

import com.jst.common.ResultVo;
import com.jst.domain.post.dto.PostDto;
import com.jst.domain.post.service.CategoryService;
import com.jst.domain.post.service.PostService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Tag(name="게시판", description = "게시판 관련 API")
public class PostController {
    @Autowired
    private PostService postService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping(value="api/post")
    public ResponseEntity<ResultVo> createPost(
            @Parameter(description = "게시글 데이터") @RequestPart(value="post") PostDto postDto,
            @Parameter(description = "파일 업로드") @RequestPart(required = false,value = "file") @ArraySchema(schema = @Schema(type = "string", format = "binary"))  List<MultipartFile> files){
        return postService.insertPost(postDto,files);
    }

    @GetMapping("api/posts")
    public ResponseEntity<ResultVo> getPosts(){
        return postService.getPost();

    }

//    @GetMapping("api/posts/categorys")
//    public Callable<ResponseEntity> getCategorys(){
//        return ()->categoryService.getCategorys();
//    }

    @RequestMapping(value = "api/posts/categorys", method = RequestMethod.GET)
    public ResponseEntity<ResultVo> getCategorys() {
        return categoryService.getCategorys();
    }
}
