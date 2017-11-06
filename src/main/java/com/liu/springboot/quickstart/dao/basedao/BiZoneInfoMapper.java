package com.liu.springboot.quickstart.dao.basedao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.liu.springboot.quickstart.model.BiZoneInfo;

public interface BiZoneInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BiZoneInfo record);

    int insertSelective(BiZoneInfo record);

    BiZoneInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BiZoneInfo record);

    int updateByPrimaryKey(BiZoneInfo record);

    /*=======================================*/
    /**
     * 插入信息，并返回主键
     * */
    int insertReturnKey(BiZoneInfo record);
    
    /**
     * 获得已经上传hdfs但是还未删除本地文件的文件列表
     * */
    List<BiZoneInfo> selectUpdateHDFS();
    
    /**
     * 批量更新
     * */
    int updateBatch(List<BiZoneInfo> record);
}