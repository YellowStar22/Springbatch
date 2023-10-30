package com.example.demo;

import com.example.demo.job.MiniTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.lang.NonNull;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.batch.test.JobLauncherTestUtils;

import javax.sql.DataSource;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;

@Configuration
@EnableBatchProcessing
public class JobConfigurationTest extends DefaultBatchConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(JobConfigurationTest.class);

    private MiniTask miniTask;

    public JobConfigurationTest(@Autowired  MiniTask miniTask) {
        this.miniTask = miniTask;
    }

    @Bean(value = "job")
    public Job getJob(JobRepository jobRepository , PlatformTransactionManager transactionManager, Step miniStep) {
        return new JobBuilder("jobTest", jobRepository)
                .start(miniStep)
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
        return new EmbeddedDatabaseBuilder().generateUniqueName(true)
                .setType(H2)
                .setScriptEncoding("UTF-8")
                .ignoreFailedDrops(true)
                .addScript("classpath:org/springframework/batch/core/schema-h2.sql")
                .build();
    }

    @Bean(name="jobLauncherTestUtils")
    public JobLauncherTestUtils jobLauncherTestUtils() throws Exception {
        JobLauncherTestUtils jl = new JobLauncherTestUtils();
        jl.setJobLauncher(jobLauncher());
        return jl;
    }

    @Bean
    public Step miniStep(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        return new StepBuilder("ministep", jobRepository).tasklet(this.miniTask , transactionManager).build();
    }


}
