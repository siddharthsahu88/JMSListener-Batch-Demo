package com.jmslistener.demo;

import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@ComponentScan(basePackages = "com.jmslistener.demo")
@EnableJms
public class JmsListenerDemoApplication {

	public static void main(String[] args) throws JobExecutionAlreadyRunningException, JobRestartException,
			JobInstanceAlreadyCompleteException, JobParametersInvalidException, InterruptedException {
		SpringApplication app = new SpringApplication(JmsListenerDemoApplication.class);

		ConfigurableApplicationContext ctx = app.run(args);

		JobLauncher jobLauncher = ctx.getBean(JobLauncher.class);

		if ("jmsListenJob".equals(args[0])) {
			Job jmsListenJob = ctx.getBean("jmsListenJob", Job.class);

			JobParameters jobParameters = new JobParametersBuilder().addDate("date", new Date()).toJobParameters();

			JobExecution jobExecution = jobLauncher.run(jmsListenJob, jobParameters);

			while (jobExecution.getStatus().isRunning()) {
				Thread.sleep(5000);
			}

			System.exit(0);

		}
	}

}
