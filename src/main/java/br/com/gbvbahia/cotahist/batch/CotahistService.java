package br.com.gbvbahia.cotahist.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CotahistService {

  private final JobLauncher jobLauncher;

  private final Job cotahistJob;

  public JobExecution startImportCotahistFile(JobParameters jobParameters) throws Exception {

    log.info("Receive request to start cotahistJob to file: {}", jobParameters);

    JobExecution execution = jobLauncher.run(cotahistJob, jobParameters);
    
    log.info("Execution Exit Status: {}", execution.getExitStatus().getExitCode());
    return execution;
  }

}
