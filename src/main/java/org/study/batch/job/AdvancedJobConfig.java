package org.study.batch.job;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.study.batch.job.validator.LocalDateParameterValidator;

import java.time.LocalDate;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AdvancedJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job advancedJob(Step advancedStep, JobExecutionListener jobExecutionListener) {
        return jobBuilderFactory.get("advancedJob")
                .incrementer(new RunIdIncrementer())
                .validator(new LocalDateParameterValidator("targetDate"))
                .listener(jobExecutionListener)
                .start(advancedStep)
                .build();
    }

    // job 실행 전 상태 확인
    @JobScope
    @Bean
    public JobExecutionListener jobExecutionListener() {
        return new JobExecutionListener() {
            @Override
            public void beforeJob(JobExecution jobExecution) {
                log.info("[jobExecution before] jobExecution is {}", jobExecution.getStatus());
            }

            @Override
            public void afterJob(JobExecution jobExecution) {
                if (jobExecution.getStatus() == BatchStatus.FAILED) {
                    log.error("[jobExecution#afterJob] Jobexecution is Failed!");
                }
                //log.info("[jobExecution after] jobExecution is {}", jobExecution);
            }
        };
    }

    @JobScope
    @Bean
    public Step advancedStep(Tasklet advancedTasklet) {
        return stepBuilderFactory.get("advancedStep")
                .tasklet(advancedTasklet)
                .build();
    }

    @StepScope
    @Bean
    public Tasklet advancedTasklet(@Value("#{jobParameters['targetDate']}") String targetDate ) {
        return (contribution, chunkContext) -> {
            log.info("JobParmeter targetDate : {}", targetDate);
            LocalDate executionDate = LocalDate.parse(targetDate);
            log.info("=== advancedTasklet executed ===");
            // 테스트 에러 발생
            // throw new RuntimeException("TEST ERROR");
            return RepeatStatus.FINISHED;
        };
    }

}
