package br.com.gbvbahia.cotahist.batch;

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

import br.com.gbvbahia.cotahist.model.Header;
import br.com.gbvbahia.cotahist.repository.CotahistRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class HeaderProcessTasklet implements Tasklet {

  private final String header;
  private final CotahistRepository cotahistRepository;

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
      throws Exception {

    LineMapper<Header> mapper = getHeaderLineMapper();
    Header saved = cotahistRepository.save(mapper.mapLine(header, 1));
    ExecutionContext execContext = contribution.getStepExecution().getExecutionContext();
    execContext.put(CotahistCfg.HEADER_ID_PROMOTION, saved.getId());

    return RepeatStatus.FINISHED;
  }

  private LineMapper<Header> getHeaderLineMapper() {
    DefaultLineMapper<Header> mapper = new DefaultLineMapper<>();
    mapper.setLineTokenizer(getHeaderTokenizer());
    BeanWrapperFieldSetMapper<Header> fieldSetMapper = new BeanWrapperFieldSetMapper<Header>();
    fieldSetMapper.setTargetType(Header.class);
    fieldSetMapper.setConversionService(StringToDate.createDateConversionService());
    mapper.setFieldSetMapper(fieldSetMapper);
    return mapper;
  }

  private LineTokenizer getHeaderTokenizer() {
    FixedLengthTokenizer tokenizer = new FixedLengthTokenizer();
    tokenizer.setNames(
        new String[] {"tipoRegistro", "nomeArquivo", "codigoOrigem", "dataGeracaoArquivo"});
    tokenizer.setColumns(new Range(1, 2), new Range(3, 15), new Range(16, 23), new Range(24, 31));
    return tokenizer;
  }
}
