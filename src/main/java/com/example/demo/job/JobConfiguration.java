package com.example.demo.job;

import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.web.ManagementContextConfiguration;
import org.springframework.boot.autoconfigure.batch.JobLauncherApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.support.JdbcTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class JobConfiguration{

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .addScript("/org/springframework/batch/core/schema-hsqldb.sql")
                .build();
    }

    @Bean
    public JdbcTransactionManager transactionManager(DataSource dataSource) {
        return new JdbcTransactionManager(dataSource);
    }

    @Bean
    public MiniTask getMiniTask(UnknowService unknowService){
        return new MiniTask(unknowService);
    }

    @Bean
    public UnknowService getUnknowService(){
        return new UnknowService();
    }
    @Bean
    public Step step(JobRepository jobRepository, JdbcTransactionManager transactionManager) {
        return new StepBuilder("step", jobRepository).tasklet(getMiniTask(getUnknowService()),transactionManager).allowStartIfComplete(true).build();
    }

    @Bean
    public Job runJob(JobRepository jobRepository, Step step) {
        return new JobBuilder("job", jobRepository).start(step).build();
    }

    @Bean
    public JobLauncherApplicationRunner jobLauncherApplicationRunner(
            @Autowired JobLauncher jobLauncher, @Autowired JobExplorer jobExplorer, @Autowired JobRepository jobRepository
    ){
        return new JobLauncherApplicationRunner(jobLauncher,jobExplorer,jobRepository);
    }




}
