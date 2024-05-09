package com.moye.oj.judge.strategy;

import cn.hutool.json.JSONUtil;
import com.moye.oj.model.dto.question.JudgeCase;
import com.moye.oj.model.dto.question.JudgeConfig;
import com.moye.oj.model.dto.questionsubmit.JudgeInfo;
import com.moye.oj.model.entity.Question;
import com.moye.oj.model.enums.JudgeInfoEnum;

import java.util.List;

public class DefaultJudgeStrategy implements JudgeStrategy {
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
        if (responseJudgeInfoTime > timeLimit) {
            judgeInfoEnum = JudgeInfoEnum.TIME_LIMIT_EXCEEDED;
            return judgeInfoResponse;
        }
        judgeInfoResponse.setMessage(judgeInfoEnum.getValue());
        return judgeInfoResponse;
    }
}
