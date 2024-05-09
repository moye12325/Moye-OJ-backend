package com.moye.oj.judge.strategy;

import cn.hutool.json.JSONUtil;
import com.moye.oj.model.dto.question.JudgeCase;
import com.moye.oj.model.dto.question.JudgeConfig;
import com.moye.oj.judge.codesandbox.model.JudgeInfo;
import com.moye.oj.model.entity.Question;
import com.moye.oj.model.enums.JudgeInfoEnum;

import java.util.List;

public class JavaLanguageJudgeStrategy implements JudgeStrategy {
    /**
     * @param judgeContext
     * @return
     */
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {

        JudgeInfo executeCodeResponseJudgeInfo = judgeContext.getJudgeInfo();
        long responseJudgeInfoMemory = executeCodeResponseJudgeInfo.getMemory();
        long responseJudgeInfoTime = executeCodeResponseJudgeInfo.getTime();
        List<String> inputList = judgeContext.getInputList();
        List<String> outputList = judgeContext.getOutputList();
        Question question = judgeContext.getQuestion();
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();

        JudgeInfoEnum judgeInfoEnum = JudgeInfoEnum.ACCEPTED;

        JudgeInfo judgeInfoResponse = new JudgeInfo();
        judgeInfoResponse.setMemory(responseJudgeInfoMemory);
        judgeInfoResponse.setTime(responseJudgeInfoTime);
        judgeInfoResponse.setMessage(judgeInfoEnum.getValue());

        if (outputList.size() != inputList.size()) {
            judgeInfoEnum = JudgeInfoEnum.WRONG_ANSWER;
            return judgeInfoResponse;
        }
        for (int i = 0; i < outputList.size(); i++) {
            JudgeCase judgeCase = judgeCaseList.get(i);
            if (!judgeCase.getOutput().equals(outputList.get(i))) {
                judgeInfoEnum = JudgeInfoEnum.WRONG_ANSWER;
                judgeInfoResponse.setMessage(JudgeInfoEnum.WRONG_ANSWER.getValue());
                return judgeInfoResponse;
            }
        }

        String judgeConfigStr = question.getJudgeConfig();
        JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);

        long memoryLimit = judgeConfig.getMemoryLimit();
        long timeLimit = judgeConfig.getTimeLimit();
        if (responseJudgeInfoMemory > memoryLimit) {
            judgeInfoEnum = JudgeInfoEnum.MEMORY_LIMIT_EXCEEDED;
            return judgeInfoResponse;
        }
        //假设Java执行需要少计算10s
        long JAVA_PROGRAM_TIME_COST = 10000L;
        if ((responseJudgeInfoTime - JAVA_PROGRAM_TIME_COST) > timeLimit) {
            judgeInfoEnum = JudgeInfoEnum.TIME_LIMIT_EXCEEDED;
            return judgeInfoResponse;
        }
        judgeInfoResponse.setMessage(judgeInfoEnum.getValue());
        return judgeInfoResponse;
    }
}
