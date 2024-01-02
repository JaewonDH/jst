package com.jst.domain.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PostDto {
    @NotBlank()
    @Schema(description = "게시글 제목", example = "안녕하세요")
    private String title;
    @NotBlank
    @Schema(description = "게시글 내용", example = "안녕하세요")
    private String contents;
    @NotBlank
    @Schema(description = "카테고리 id 입력", example = "1")
    private int categoryId;
}
