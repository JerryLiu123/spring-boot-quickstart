package com.liu.springboot.quickstart.config.webconfig.view;

import java.util.Locale;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

/**
 * json view 解析器，用的是MappingJackson2JsonView
 *
 */
public class JsonViewResolver implements ViewResolver {

    @Override
    public View resolveViewName(String viewName, Locale locale) throws Exception {
        // TODO Auto-generated method stub
        MappingJackson2JsonView view = new MappingJackson2JsonView();
        view.setModelKey("demoObj");//指定解析那个model
        view.setPrettyPrint(true);
        return view;
    }

}
