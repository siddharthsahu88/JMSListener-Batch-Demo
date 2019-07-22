package com.jmslistener.demo.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

@Configuration
public class JMSConfig {

	@Value("${activemq.broker-url}")
	private String brokerURL;

	@Value("${activemq.queue}")
	private String queue;

	@Bean
	public ActiveMQConnectionFactory activeMQConnectionFactory() {
		ActiveMQConnectionFactory confactory = new ActiveMQConnectionFactory();
		confactory.setBrokerURL(brokerURL);
		return confactory;
	}

	@Bean
	public JmsTemplate jmsTemplate() {
		JmsTemplate jt = new JmsTemplate(activeMQConnectionFactory());
		jt.setReceiveTimeout(10000);
		jt.setDefaultDestinationName(queue);
		return jt;
	}

}
