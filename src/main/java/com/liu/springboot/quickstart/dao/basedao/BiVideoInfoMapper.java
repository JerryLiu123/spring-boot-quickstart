package com.liu.springboot.quickstart.dao.basedao;

import org.apache.ibatis.annotations.Mapper;

import com.liu.springboot.quickstart.model.BiVideoInfo;

public interface BiVideoInfoMapper {
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