package com.moye.oj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moye.oj.model.dto.question.QuestionQueryRequest;
import com.moye.oj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.moye.oj.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.moye.oj.model.entity.Question;
import com.moye.oj.model.entity.QuestionSubmit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moye.oj.model.entity.User;
import com.moye.oj.model.vo.QuestionSubmitVO;
import com.moye.oj.model.vo.QuestionVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author 19423
* @description 针对表【question_submit(题目提交)】的数据库操作Service
* @createDate 2024-05-07 18:31:25
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {

    /**
     * 提交题目
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);

    /**
     * 帖子点赞（内部服务）
     *
     * @param userId
     * @param questionId
     * @return
     */
    int doQuestionSubmitInner(long userId, long questionId);

    /**
     * 获取查询条件
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);

    /**
     * 获取题目封装
     *
     * @param questionSubmit
     * @param request
     * @return
     */
    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit,  User loginUser);

    /**
     * 分页获取题目封装
     *
     * @param questionSubmitPage
     * @param request
     * @return
     */
    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage,  User loginUser);
}
