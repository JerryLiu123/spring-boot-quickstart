package com.liu.springboot.quickstart.config.webconfig.view;

import java.util.Locale;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.xml.MarshallingView;

import com.liu.springboot.quickstart.bean.DemoObj;

/**
 * xml view 解析器
 *
 */
public class XmlViewResolver implements ViewResolver {

    @Override
    public View resolveViewName(String viewName, Locale locale) throws Exception {
        // TODO Auto-generated method stub
        MarshallingView view = new MarshallingView();
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(DemoObj.class);
        marshaller.setPackagesToScan("com.liu.springboot.quickstart.bean");
        view.setMarshaller(marshaller);
        return view;
    }

}
