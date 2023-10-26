package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.lang.NonNull;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.batch.test.JobLauncherTestUtils;

import javax.sql.DataSource;

@Configuration
public class JobConfigurationTest extends DefaultBatchConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(JobConfigurationTest.class);

    @Bean(value = "job")
    public Job getJob(JobRepository jobRepository , PlatformTransactionManager transactionManager) {
        return new JobBuilder("jobTest", jobRepository)
                .start(miniStep(jobRepository, transactionManager))
                .build();
    }

    @Bean(value = "jobLauncher")
    public JobLauncher getJobLauncher() throws Exception {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(getJobRepository());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

    @Bean
    public JobRepository getJobRepository() throws Exception {
        JobRepositoryFactoryBean factoryBean = new JobRepositoryFactoryBean();
        factoryBean.setDataSource(getDataSource());
        factoryBean.setTransactionManager(getTransactionManager());
        factoryBean.afterPropertiesSet();
        return factoryBean.getObject();
    }

    @NonNull
    @Bean
    public PlatformTransactionManager getTransactionManager() {
        return new ResourcelessTransactionManager();
    }

    @NonNull
    @Bean
    public DataSource getDataSource() {
        return DataSourceBuilder.create().driverClassName("org.h2.Driver").url("jdbc:h2:mem:AZ;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE").username("user1").password("pass").build();
    }

    @Bean(name="jobLauncherTestUtils")
    public JobLauncherTestUtils jobLauncherTestUtils() throws Exception {
        JobLauncherTestUtils jl = new JobLauncherTestUtils();
        jl.setJobLauncher(jobLauncher());
        return jl;
    }

    @Bean
    public Step miniStep(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        return new StepBuilder("ministep", jobRepository).tasklet(new MiniStep() , transactionManager).build();
    }


}
