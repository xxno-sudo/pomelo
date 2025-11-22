package com.cuckoo.pomelo.core.config;

import com.cuckoo.pomelo.core.util.EnvUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.Properties;

/**
 * 1.可以加载远程配置文件
 * 2.可以对配置文件进行加解密
 */
@Slf4j
public class DynamicEnvironmentPostProcessor implements EnvironmentPostProcessor {
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        try {
            String filename = "properties/spring." + EnvUtil.getEnv() + ".properties";
            ClassPathResource resource = new ClassPathResource(filename);
            InputStream is = resource.getInputStream();
            Properties properties = new Properties();
            properties.load(is);
            PropertiesPropertySource propertySource = new PropertiesPropertySource("dynamic", properties);
            environment.getPropertySources().addLast(propertySource);
        } catch (Exception e) {
            //加载配置文件出错
            log.error("加载配置文件出错:{}",e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
