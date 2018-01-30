package com.liu.springboot.quickstart.util.dynamicdatasource;

import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.liu.springboot.quickstart.util.SpringUtil;

@Component
public class DataUtil {

    public static JdbcTemplate getDataTemplate(DBEnum data) {
        Map<String, JdbcTemplate> datas = SpringUtil.getBeans(JdbcTemplate.class);
        return null;
    }
}
