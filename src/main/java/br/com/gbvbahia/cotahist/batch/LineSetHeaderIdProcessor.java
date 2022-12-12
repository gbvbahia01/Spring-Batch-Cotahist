package br.com.gbvbahia.cotahist.batch;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    if (item.getPreAbe() != null)
      item.setPreAbe(item.getPreAbe().divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
    
    if (item.getPreMax() != null)
      item.setPreMax(item.getPreMax().divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
    
    if (item.getPreMin() != null)
      item.setPreMin(item.getPreMin().divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
    
    if (item.getPreMed() != null)
      item.setPreMed(item.getPreMed().divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
    
    if (item.getPreUlt() != null)
      item.setPreUlt(item.getPreUlt().divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
    
    if (item.getPreOfc() != null)
      item.setPreOfc(item.getPreOfc().divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
    
    if (item.getPreOfv() != null)
      item.setPreOfv(item.getPreOfv().divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
    
    if (item.getPreExe() != null)
      item.setPreExe(item.getPreExe().divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
    
    if (item.getPtoExe() != null) 
      item.setPtoExe(item.getPtoExe().divide(new BigDecimal(1000000), 2, RoundingMode.HALF_UP));
    
    return item;
  }

  @BeforeStep
  public void retrieveInterstepData(StepExecution stepExecution) {
    JobExecution jobExecution = stepExecution.getJobExecution();
    ExecutionContext jobContext = jobExecution.getExecutionContext();
    this.headerId = jobContext.getLong(CotahistCfg.HEADER_ID_PROMOTION);
  }

}
