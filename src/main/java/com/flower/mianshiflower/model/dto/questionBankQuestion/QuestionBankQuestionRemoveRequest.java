package com.flower.mianshiflower.model.dto.questionBankQuestion;

import lombok.Data;

import java.io.Serializable;

/**
 * 提出题目题库关联
 */
@Data
public class QuestionBankQuestionRemoveRequest implements Serializable {

    private static final long serialVersionUID = -881914346092023284L;

    /**
     * 题库id
     */
    private Long questionBankId;

    /**
     * 题目Id
     */
    private Long questionId;
}
