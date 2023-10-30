package com.example.demo;

import com.example.demo.job.MiniTask;
import com.example.demo.job.UnknowService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringBatchTest
@ComponentScans({@ComponentScan(basePackages = "com.example.demo.job")})
@SpringJUnitConfig(classes = { JobConfigurationTest.class })
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DemoApplicationTests {

	 @Autowired
	 private JobLauncherTestUtils jobLauncherTestUtils;

	@Test
	void contextLoads() {
	}

	@BeforeAll
	private void beforeAll(){
		System.out.println("Before all");
	}

	@Test
	public void testJob(Job job , JobLauncher jobLauncher) throws Exception {
		this.jobLauncherTestUtils.setJob(job);
		this.jobLauncherTestUtils.setJobLauncher(jobLauncher);
		JobExecution jobExecution = jobLauncherTestUtils.launchJob(new JobParameters());


		Assertions.assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode());
	}

}
