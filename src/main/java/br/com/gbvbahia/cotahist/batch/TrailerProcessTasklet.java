package br.com.gbvbahia.cotahist.batch;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.batch.repeat.RepeatStatus;
import br.com.gbvbahia.cotahist.model.Trailer;
import br.com.gbvbahia.cotahist.repository.CotahistRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class TrailerProcessTasklet implements Tasklet {

  private Long headerId;
  private final String trailer;
  private final CotahistRepository cotahistRepository;
  
  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
      throws Exception {
    
    retrieveInterstepData(contribution);
    
    LineMapper<Trailer> mapper = getTrailerLineMapper();
    Trailer toSave = mapper.mapLine(trailer, 1);
    toSave.setHeaderId(headerId);
    cotahistRepository.save(toSave);
    
    return RepeatStatus.FINISHED;
  }

  private LineMapper<Trailer> getTrailerLineMapper() {
    DefaultLineMapper<Trailer> mapper = new DefaultLineMapper<>();
    mapper.setLineTokenizer(getTrailerTokenizer());
    BeanWrapperFieldSetMapper<Trailer> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
    fieldSetMapper.setTargetType(Trailer.class);
    fieldSetMapper.setConversionService(StringToDate.createDateConversionService());
    mapper.setFieldSetMapper(fieldSetMapper);
    return mapper;
  }
  
  private LineTokenizer getTrailerTokenizer() {
    FixedLengthTokenizer tokenizer = new FixedLengthTokenizer();
    tokenizer.setNames(
        new String[] {"tipoRegistro", "nomeArquivo", "codigoOrigem", "dataGeracaoArquivo", "totalRegistros"});
    tokenizer.setColumns(new Range(1, 2), new Range(3, 15), new Range(16, 23), new Range(24, 31), new Range(32, 42));
    return tokenizer;
  }
  
  public void retrieveInterstepData(StepContribution contribution) {
    JobExecution jobExecution = contribution.getStepExecution().getJobExecution();
    ExecutionContext jobContext = jobExecution.getExecutionContext();
    this.headerId = jobContext.getLong(CotahistCfg.HEADER_ID_PROMOTION);
  }
}
