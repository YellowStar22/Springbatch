package com.example.demo.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class JobConfiguration {
    @Bean
    public Job job(JobRepository jobRepository, PlatformTransactionManager transactionManager ,@Autowired MiniTask miniTask){
        return new JobBuilder("job", jobRepository)
                .start(new StepBuilder("step", jobRepository)
                        .tasklet(miniTask, transactionManager)
                        .build())
                .build();
    }

}
