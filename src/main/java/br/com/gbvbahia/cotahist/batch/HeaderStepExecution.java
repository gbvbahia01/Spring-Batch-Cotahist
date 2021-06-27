package br.com.gbvbahia.cotahist.batch;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class HeaderStepExecution implements StepExecutionListener {

   @Override
   public void beforeStep(StepExecution stepExecution) {
   }

   @Override
   public ExitStatus afterStep(StepExecution stepExecution) {
      stepExecution.getJobExecution().getExecutionContext().put("headerId", stepExecution.getExecutionContext().get("headerId"));
      return ExitStatus.EXECUTING;
   }

}
