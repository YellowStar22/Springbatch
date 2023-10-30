package com.example.demo.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@StepScope
@Component
public class MiniTask implements Tasklet {
    private static Logger LOG = LoggerFactory.getLogger(MiniTask.class);

    private UnknowService unknowService;

    public MiniTask(UnknowService unknowService) {
        this.unknowService = unknowService;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        LOG.info("Lancement de la tasklet");
        return RepeatStatus.FINISHED;
    }


}
