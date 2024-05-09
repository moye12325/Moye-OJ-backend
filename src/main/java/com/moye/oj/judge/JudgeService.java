package com.moye.oj.judge;

import com.moye.oj.judge.codesandbox.model.ExecuteCodeRequest;
import com.moye.oj.judge.codesandbox.model.ExecuteCodeResponse;
import com.moye.oj.model.entity.QuestionSubmit;
import com.moye.oj.model.vo.QuestionSubmitVO;

public interface JudgeService {

    QuestionSubmit doJudge(long submitId);
}
