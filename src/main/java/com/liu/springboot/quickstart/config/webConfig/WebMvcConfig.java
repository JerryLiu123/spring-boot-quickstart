package com.liu.springboot.quickstart.config.webConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

@Configuration
public class WebMvcConfig {

	/**
	 * 设置参数类型转换器(RequestBody)
	 * @return MappingJackson2HttpMessageConverter
	 */
	@Bean
	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
		MappingJackson2HttpMessageConverter httpMessageConverter = new MappingJackson2HttpMessageConverter();
		httpMessageConverter.setSupportedMediaTypes(new ArrayList<MediaType>() {{
			add(MediaType.valueOf("application/json;charset=UTF-8"));
			add(MediaType.valueOf("text/html;charset=UTF-8"));
		}});
		return httpMessageConverter;
	}
	
	@Bean
	public ObjectMapper jsonMapper(){
	    ObjectMapper objectMapper = new ObjectMapper();
	    //null输出空字符串
	    objectMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
	        @Override
	        public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
	            jgen.writeString("");
	        }
	    });
	    return objectMapper;
	}
	
	@Bean
	public StringHttpMessageConverter stringHttpMessageConverter() {
		StringHttpMessageConverter converter = new StringHttpMessageConverter();
		converter.setSupportedMediaTypes(new ArrayList<MediaType>() {{
			add(MediaType.valueOf("text/plain;charset=UTF-8"));
		}});
		return converter;
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
}
