package br.com.gbvbahia.cotahist.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
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
   
   public JobExecution startImportCotahistFile(String pathToFile, Long trailerToSkip) throws Exception {
      
      log.info("Receive request to start cotahistJob to file: {}", pathToFile);
      
      JobParameters jobParameters= new JobParametersBuilder()
            .addString("pathToFile", pathToFile)
            .addLong("trailerToSkip", trailerToSkip)
            .toJobParameters();
      
      JobExecution execution = jobLauncher.run(cotahistJob, jobParameters);
      return execution;
   }
   
}
