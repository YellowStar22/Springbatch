package com.example.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.sql.DataSource;

@SpringJUnitConfig(JobConfigurationTest.class)
class DemoApplicationTests {

	 @Autowired
	 private JobLauncherTestUtils jobLauncherTestUtils;

	@Test
	void contextLoads() {
	}

	@Test
	public void testJob(@Autowired Job job , @Autowired JobLauncher jobLauncher) throws Exception {
		this.jobLauncherTestUtils.setJob(job);
		this.jobLauncherTestUtils.setJobLauncher(jobLauncher);
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();


		Assertions.assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode());
	}

}
