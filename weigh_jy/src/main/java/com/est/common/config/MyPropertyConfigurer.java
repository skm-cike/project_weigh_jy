package com.est.common.config;

import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.est.common.ext.util.encrypt.Des;

/**
 * @date 2013-04-22
 * @desc 对JDBC配置文件的username,password 数据项目进行DES解密
 * @author heb
 * 
 */
public class MyPropertyConfigurer extends PropertyPlaceholderConfigurer {

	@Override
	protected void processProperties(
			ConfigurableListableBeanFactory beanFactoryToProcess,
			Properties props) throws BeansException {

		String username = props.getProperty("datasource.username");
		String password = props.getProperty("datasource.password");

		try {
			if (username != null) {
				props.setProperty("datasource.username", new Des().decode(username));
			}
			if (password != null) {
				props.setProperty("datasource.password", new Des().decode(password));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		super.processProperties(beanFactoryToProcess, props);
	}

}
