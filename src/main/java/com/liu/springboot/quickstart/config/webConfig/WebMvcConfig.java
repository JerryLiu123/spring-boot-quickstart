package com.liu.springboot.quickstart.config.webConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.liu.springboot.quickstart.config.ConstantsConfig;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter{

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
		registry.addResourceHandler("/res/**").addResourceLocations("classpath:/"+ConstantsConfig.resources);
	}
}
