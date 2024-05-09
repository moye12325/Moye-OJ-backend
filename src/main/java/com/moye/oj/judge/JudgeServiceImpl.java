package com.moye.oj.judge;

import cn.hutool.json.JSONUtil;
import com.moye.oj.common.ErrorCode;
import com.moye.oj.exception.BusinessException;
import com.moye.oj.judge.codesandbox.CodeSandbox;
import com.moye.oj.judge.codesandbox.CodeSandboxFactory;
import com.moye.oj.judge.codesandbox.CodeSandboxProxy;
import com.moye.oj.judge.codesandbox.model.ExecuteCodeRequest;
import com.moye.oj.judge.codesandbox.model.ExecuteCodeResponse;
import com.moye.oj.judge.strategy.DefaultJudgeStrategy;
import com.moye.oj.judge.strategy.JavaLanguageJudgeStrategy;
import com.moye.oj.judge.strategy.JudgeContext;
import com.moye.oj.judge.strategy.JudgeStrategy;
import com.moye.oj.model.dto.question.JudgeCase;
import com.moye.oj.model.dto.question.JudgeConfig;
import com.moye.oj.model.dto.questionsubmit.JudgeInfo;
import com.moye.oj.model.entity.Question;
import com.moye.oj.model.entity.QuestionSubmit;
import com.moye.oj.model.enums.JudgeInfoEnum;
import com.moye.oj.model.enums.QuestionSubmitLanguageEnum;
import com.moye.oj.model.enums.QuestionSubmitStatusEnum;
import com.moye.oj.model.vo.QuestionSubmitVO;
import com.moye.oj.service.QuestionService;
import com.moye.oj.service.QuestionSubmitService;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.eclipse.parsson.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class JudgeServiceImpl implements JudgeService {

    @Autowired
    private QuestionSubmitService questionSubmitService;

    @Autowired
    private QuestionService questionService;

    @Value("${codesandbox.type:example}")
    private String type;

    @Autowired
    private JudgeManager judgeManager;


    /**
     * 判题
     *
     * @param submitId
     * @return
     */
    @Override
    public QuestionSubmit doJudge(long submitId) {

//        1）传入题目的提交 id，获取到对应的题目、提交信息（包含代码、编程语言等）
        QuestionSubmit questionSubmit = questionSubmitService.getById(submitId);
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交信息不存在");
        }

        long questionId = questionSubmit.getQuestionId();
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目信息不存在");
        }

//        2）如果题目提交状态不为等待中，就不用重复执行了~
        if (!questionSubmit.getStatus().equals(QuestionSubmitStatusEnum.WAITING.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "正在判题");
        }

//        3）更改判题（题目提交）的状态为 “判题中”，防止重复执行，也能让用户即时看到状态
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmit.getId());
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        boolean update = questionSubmitService.updateById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目状态更新失败");
        }

//        4）调用沙箱，获取到执行结果
        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(type);

        CodeSandboxProxy codeSandboxProxy = new CodeSandboxProxy(codeSandbox);

        String language = questionSubmit.getLanguage();
        String code = questionSubmit.getCode();

        //获取输入用例
        String judgeCaseStr = question.getJudgeCase();
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());

        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();

        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
//        根据沙箱的执行结果，判断运行是否正确
        List<String> outputList = executeCodeResponse.getOutputList();

//        5）根据沙箱的执行结果，设置题目的判题状态和信息
        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setJudgeInfo(executeCodeResponse.getJudgeInfo());
        judgeContext.setInputList(inputList);
        judgeContext.setOutputList(outputList);
        judgeContext.setQuestion(question);
        judgeContext.setJudgeCaseList(judgeCaseList);
        judgeContext.setQuestionSubmit(questionSubmit);

        JudgeInfo judgeInfo = judgeManager.doJudge(judgeContext);

        //修改数据库中的判题结果
        questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmit.getId());
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        update = questionSubmitService.updateById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目状态更新失败");
        }

        QuestionSubmit questionSubmitResult = questionSubmitService.getById(questionSubmit.getId());
        return questionSubmitResult;
    }
}
