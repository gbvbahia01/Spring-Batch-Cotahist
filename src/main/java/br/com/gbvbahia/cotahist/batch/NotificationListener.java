package br.com.gbvbahia.cotahist.batch;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class NotificationListener implements JobExecutionListener {

  @Override
  public void beforeJob(JobExecution jobExecution) {
    log.debug("Job:{} START", jobExecution.getJobInstance().getJobName());

  }

  @Override
  public void afterJob(JobExecution jobExecution) {
    log.debug("Job:{} FINISHED: {}", jobExecution.getJobInstance().getJobName(),
        jobExecution.getExitStatus().getExitCode());
  }

}
