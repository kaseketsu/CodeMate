package com.flower.mianshiflower.esdao;

import com.flower.mianshiflower.model.dto.post.PostEsDTO;
import com.flower.mianshiflower.model.dto.question.QuestionEsDTO;
import com.flower.mianshiflower.model.entity.Question;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * 题目 ES 操作
 *
 * @author <a href="https://github.com/kaseketsu">程序员小花</a>
 * @from <a href="https://f1ower.cn">小花blog</a>
 */
public interface QuestionEsDao extends ElasticsearchRepository<QuestionEsDTO, Long> {

    List<QuestionEsDTO> findByUserId(Long userId);
}