package com.liu.springboot.quickstart.config.webconfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.accept.ContentNegotiationManagerFactoryBean;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.springframework.web.servlet.view.xslt.XsltViewResolver;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.liu.springboot.quickstart.config.ConstantsConfig;
import com.liu.springboot.quickstart.config.webconfig.view.JsonViewResolver;
import com.liu.springboot.quickstart.config.webconfig.view.XlsViewResolver;
import com.liu.springboot.quickstart.config.webconfig.view.XmlViewResolver;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter{

    
//    @Autowired
//    private SpringTemplateEngine templateEngine;
    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;
    
	/**
	 * 设置json转换器
	 * @return MappingJackson2HttpMessageConverter
	 */
	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
		MappingJackson2HttpMessageConverter httpMessageConverter = new MappingJackson2HttpMessageConverter();
		httpMessageConverter.setSupportedMediaTypes(new ArrayList<MediaType>() {{
			add(MediaType.valueOf("application/json;charset=UTF-8"));
			add(MediaType.valueOf("text/html;charset=UTF-8"));
		}});
		ObjectMapper objectMapper = httpMessageConverter.getObjectMapper();
		//null显示为""字符串
		objectMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
			@Override
			public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
				jgen.writeString("");
			}
		});
		httpMessageConverter.setObjectMapper(objectMapper);
		return httpMessageConverter;
	}
	
	/**
	 * 设置String 转换器
	 * @return
	 */
	public StringHttpMessageConverter stringHttpMessageConverter() {
		StringHttpMessageConverter converter = new StringHttpMessageConverter();
		converter.setSupportedMediaTypes(new ArrayList<MediaType>() {{
			add(MediaType.valueOf("text/plain;charset=UTF-8"));
		}});
		return converter;
	}
	
	/**
	 *  重写消息转换器链
	 *  如果转换器链顺序错乱的话可以重写方法extendMessageConverters()，extendMessageConverters中加入转换器之前执行converters.clear();
	 */
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		// TODO Auto-generated method stub
		//super.configureMessageConverters(converters);
		converters.add(0, new ByteArrayHttpMessageConverter());//byte类型转换器
		converters.add(1, stringHttpMessageConverter());//String 类型转换器
		converters.add(2, mappingJackson2HttpMessageConverter());//json类型转换器
	}
	
	/**
	 * 设置jsonp 跨域支持
	 * @return
	 */
	@Bean
	public MappingJackson2JsonView mappingJackson2JsonView() {
		MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
		jsonView.setJsonpParameterNames(new HashSet<String>(){{
			add("jsonp");
			add("callback");
		}});
		return jsonView;
	}
	
	/**
	 * 增加静态资源映射
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// TODO Auto-generated method stub
		registry.addResourceHandler("/res/**").addResourceLocations("classpath:"+ConstantsConfig.resources+"/");
	}
	
	/**
	 * 视图解析器
	 */
//	@Bean(name="contentNegotiationManager")
//	public ContentNegotiationManagerFactoryBean contentNegotiationManagerFactoryBean() {
//	    ContentNegotiationManagerFactoryBean a = new ContentNegotiationManagerFactoryBean();
//	    a.setIgnoreAcceptHeader(true);
//	    a.setFavorPathExtension(false);
//	    a.setFavorParameter(true);
//	    a.setParameterName("format");
//	    a.setDefaultContentType(MediaType.TEXT_HTML);
//	    Properties props = new Properties();
//	    props.setProperty("html", "text/html");
//	    props.setProperty("xml", "application/xml");
//	    props.setProperty("json", "application/json");
//	    a.setMediaTypes(props);
//	    return a;
//	}
	/**
	 * 在此---配置ContentNegotiationManager,在无后缀名情况下默认为html view resolver
	 */
    @Override
    public void configureContentNegotiation(
            ContentNegotiationConfigurer configurer) {
        configurer.ignoreAcceptHeader(true).defaultContentType(
                MediaType.TEXT_HTML);
    }
	
	/**
	 * 在此---配置ContentNegotiatingViewResolver,通过此代理到不同的viewResolover
	 * 感觉这种方式局限性很大~比如说当是不同的bean返回的xml/excel时就要多写一个bean的解析 view,所以感觉不如在返回ModeAndView时执行View解析器
	 * @param manager
	 * @return
	 */
    @Bean
    public ViewResolver contentNegotiatingViewResolver(
            ContentNegotiationManager manager) {
        ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
        resolver.setContentNegotiationManager(manager);
        //保存所有的view解析器
        List<ViewResolver> resolvers = new ArrayList<ViewResolver>();
        resolvers.add(thymeleafViewResolver);//默认的html
        /*
         * 以下解析器都要指定要解析的mode名称，也就是说对应的类解析~怎么弄个通用的那~~~
         * 当然json也可以不行名称，但是不指定mode的话当碰上复杂的bean时可能会解析失败~~~~
         * */
        resolvers.add(jsonViewResolver());//json
        resolvers.add(xmlViewResolver());//xml
        resolvers.add(xlsViewResolver());//xls
        
        resolver.setViewResolvers(resolvers);
        return resolver;
    }
    
    @Bean
    public ViewResolver xmlViewResolver() {
        return new XmlViewResolver();
    }
    @Bean
    public ViewResolver jsonViewResolver() {
        return new JsonViewResolver();
    }
    @Bean
    public ViewResolver xlsViewResolver() {
        return new XlsViewResolver();
    }
//    @Bean
//    public ThymeleafViewResolver thymeleafViewResolver() {
//        ThymeleafViewResolver a = new ThymeleafViewResolver();
//        a.setTemplateEngine(templateEngine);
//        a.setCharacterEncoding("utf-8");
//        a.setOrder(1);
//        return a;
//    }
}
