package com.moye.oj.judge;

import com.moye.oj.judge.strategy.DefaultJudgeStrategy;
import com.moye.oj.judge.strategy.JavaLanguageJudgeStrategy;
import com.moye.oj.judge.strategy.JudgeContext;
import com.moye.oj.judge.strategy.JudgeStrategy;
import com.moye.oj.judge.codesandbox.model.JudgeInfo;
import com.moye.oj.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

/**
 * 判题管理 简化调用
 */
@Service
public class JudgeManager {

    JudgeInfo doJudge(JudgeContext judgeContext) {
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if (language.equals("java")) {
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }
}
