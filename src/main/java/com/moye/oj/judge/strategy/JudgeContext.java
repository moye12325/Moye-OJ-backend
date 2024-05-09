package com.moye.oj.judge.strategy;

import com.moye.oj.model.dto.question.JudgeCase;
import com.moye.oj.model.dto.questionsubmit.JudgeInfo;
import com.moye.oj.model.entity.Question;
import com.moye.oj.model.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

/**
 * 上下文 用于定义传递的参数
 */
@Data
public class JudgeContext {

    private JudgeInfo judgeInfo;

    private List<String> inputList;

    private List<String> outputList;

    private Question question;

    private List<JudgeCase> judgeCaseList;

    private QuestionSubmit questionSubmit;
}
