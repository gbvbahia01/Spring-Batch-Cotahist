package br.com.gbvbahia.cotahist.batch;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import br.com.gbvbahia.cotahist.model.Line;
import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class LineSetHeaderIdProcessor implements ItemProcessor<Line, Line> {

  private Long headerId;

  @Override
  public Line process(Line item) throws Exception {

    item.setHeaderId(headerId);
    return item;
  }

  @BeforeStep
  public void retrieveInterstepData(StepExecution stepExecution) {
    JobExecution jobExecution = stepExecution.getJobExecution();
    ExecutionContext jobContext = jobExecution.getExecutionContext();
    this.headerId = jobContext.getLong(CotahistCfg.HEADER_ID_PROMOTION);
  }

}
