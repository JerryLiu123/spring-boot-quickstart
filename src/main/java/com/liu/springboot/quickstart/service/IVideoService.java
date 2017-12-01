package com.liu.springboot.quickstart.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.liu.springboot.quickstart.model.BiVideoInfo;
import com.liu.springboot.quickstart.model.BiZoneInfo;


public interface IVideoService {

	/**
	 * 插入视频信息，并返回主键
	 * @param info
	 * @return
	 */
	public int insertVideo(BiVideoInfo info);
	
	/**
	 * 更新视频信息表信息
	 * @param info
	 * @return
	 */
	public int updateVideo(BiVideoInfo info);
	
	/**
	 * 插入视频分片信息数据，并返回主键
	 * @param info
	 * @return
	 */
	public int insertZoneVider(BiZoneInfo info);
	
	/**
	 * 测试aop异常切面拦截
	 * @throws Exception
	 */
	public void testException() throws Exception;
	
	
	public void testException2() throws Exception;
}
