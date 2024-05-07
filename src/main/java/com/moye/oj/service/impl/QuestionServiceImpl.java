package com.moye.oj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moye.oj.model.entity.Question;
import com.moye.oj.service.QuestionService;
import com.moye.oj.mapper.QuestionMapper;
import org.springframework.stereotype.Service;

/**
* @author 19423
* @description 针对表【question(题目)】的数据库操作Service实现
* @createDate 2024-05-07 18:29:29
*/
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
    implements QuestionService{

}




