package com.flower.mianshiflower.mapper;

import com.flower.mianshiflower.model.entity.Question;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
* @author 75574
* @description 针对表【question(题目)】的数据库操作Mapper
* @createDate 2025-02-24 22:40:12
* @Entity com.flower.mianshiflower.model.entity.Question
*/
public interface QuestionMapper extends BaseMapper<Question> {


    /**
     * 查询最近5分钟内的数据
     * @param minUpdateTime
     * @return
     */
    @Select("select * from question where updateTime >= #{minUpdateTime}")
    List<Question> listQuestionWithDelete(Date minUpdateTime);
}




