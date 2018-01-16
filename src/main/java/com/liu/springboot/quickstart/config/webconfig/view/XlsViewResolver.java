package com.liu.springboot.quickstart.config.webconfig.view;

import java.util.Locale;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

/**
 * excel view 解析器
 *
 */
public class XlsViewResolver implements ViewResolver {

    @Override
    public View resolveViewName(String viewName, Locale locale) throws Exception {
        // TODO Auto-generated method stub
        return new XlsView();
    }

}
