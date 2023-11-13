package com.example.demo.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("miniTask")
public class MiniTask implements Tasklet {
    private static Logger LOG = LoggerFactory.getLogger(MiniTask.class);

    private UnknowService unknowService;

    public MiniTask(@Autowired UnknowService unknowService) {
        this.unknowService = unknowService;
        LOG.info("Tasklet constructor");
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        LOG.info("Launch task");
        return RepeatStatus.FINISHED;
    }
}
