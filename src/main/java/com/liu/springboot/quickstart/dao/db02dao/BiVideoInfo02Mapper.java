package com.liu.springboot.quickstart.dao.db02dao;

import org.apache.ibatis.annotations.Mapper;

import com.liu.springboot.quickstart.model.BiVideoInfo;

public interface BiVideoInfo02Mapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BiVideoInfo record);

    int insertSelective(BiVideoInfo record);

    BiVideoInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BiVideoInfo record);

    int updateByPrimaryKey(BiVideoInfo record);
    
    /*====================================*/
    /**
     * 插入信息，并返回主键
     * */
    int insertReturnKey(BiVideoInfo record);
}