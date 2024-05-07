package com.moye.oj.model.dto.questionsubmit;

import com.moye.oj.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionSubmitQueryRequest extends PageRequest implements Serializable {
    /**
     * 题目 id
     */
    private Long questionId;

    private String language;

    private Integer status;

    private Long userId;


    private static final long serialVersionUID = 1L;

}