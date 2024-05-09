package com.moye.oj.judge.codesandbox.model;

import com.moye.oj.model.dto.question.JudgeConfig;
import com.moye.oj.model.dto.questionsubmit.JudgeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteCodeResponse {

    private String code;

    private List<String> outputList;

    /**
     * 执行信息
     */
    private JudgeInfo judgeInfo;

    /**
     * 接口信息
     */
    private String message;

    /**
     * 执行状态
     */
    private Integer status;
}
