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
      log.info("Job:{} START", jobExecution.getJobConfigurationName());

   }

   @Override
   public void afterJob(JobExecution jobExecution) {
      log.info("Job:{} FINISHED: {}-{}", jobExecution.getJobConfigurationName(),
                                         jobExecution.getExitStatus().getExitCode(),
                                         jobExecution.getExitStatus().getExitDescription());
   }

}
