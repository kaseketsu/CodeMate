package com.flower.mianshiflower.model.dto.questionBankQuestion;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 批量移除题库题目关联请求
 *
 * @author <a href="https://github.com/kaseketsu">程序员小花</a>
 * @from <a href="https://www.code-nav.cn">编程导航学习圈</a>
 */
@Data
public class QuestionBankQuestionBatchRemoveRequest implements Serializable {

    /**
     * 题库 id
     */
    private Long questionBankId;

    /**
     * 题目 id列表
     */
    private List<Long> questionIdList;

    private static final long serialVersionUID = 1L;
}