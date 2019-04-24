package com.jmslistener.demo.config;

import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.jms.JmsItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private StepBuilderFactory steps;

	@Autowired
	private JobBuilderFactory jobs;

	@Autowired
	private JmsTemplate jmsTemplate;

	@Bean
	public SimpleJobLauncher jobLauncher() {
		SimpleJobLauncher launcher = new SimpleJobLauncher();
		launcher.setJobRepository(jobRepository);
		return launcher;
	}

	@Bean
	public JmsItemReader<String> jmsItemReader() {
		JmsItemReader<String> reader = new JmsItemReader<>();
		reader.setJmsTemplate(jmsTemplate);
		return reader;
	}

	@Bean
	public ItemWriter<String> itemWriter() {
		ItemWriter<String> writer = new ItemWriter<String>() {

			@Override
			public void write(List<? extends String> items) throws Exception {
				System.out.println(items);
			}
		};

		return writer;
	}

	@Bean
	public Step jmsListenStep() {
		return steps.get("JmsListenStep").<String, String>chunk(10).reader(jmsItemReader()).writer(itemWriter())
				.build();
	}

	@Bean
	public Job jmsListenJob() {
		return jobs.get("jmsListenJob").start(jmsListenStep()).build();
	}

}
