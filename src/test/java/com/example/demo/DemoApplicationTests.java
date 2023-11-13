package com.example.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@SpringBatchTest
class DemoApplicationTests {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Test
	void contextLoads() {
	}
	@Test
	public void testJob(@Autowired Job job) throws Exception {
		this.jobLauncherTestUtils.setJob(job);
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();
		Assertions.assertEquals(jobExecution.getExitStatus().getExitCode(), "COMPLETED");
	}

}
