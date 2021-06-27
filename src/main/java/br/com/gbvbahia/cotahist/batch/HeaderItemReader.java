package br.com.gbvbahia.cotahist.batch;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.batch.item.file.transform.Range;

import br.com.gbvbahia.cotahist.model.Header;
import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class HeaderItemReader implements ItemReader<Header> {

   private final String header;
   private StepExecution stepExecution;
   
   @Override
   public Header read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
      
      if (stepExecution.getExecutionContext().get("HeaderSaved") != null) {
         return null;
      }
      
      LineMapper<Header> mapper = getHeaderMapper();
      stepExecution.getExecutionContext().putString("HeaderSaved", "did");
      return mapper.mapLine(header, 1);
   }

   private LineMapper<Header> getHeaderMapper() {
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
      tokenizer.setNames(new String[]{"tipoRegistro","nomeArquivo","codigoOrigem","dataGeracaoArquivo"});
      
      tokenizer.setColumns(    new Range(1, 2),    new Range(3, 15),   new Range(16, 23),   new Range(24, 31));
      return tokenizer;
   }
   
   @BeforeStep
   public void saveStepExecution(StepExecution stepExecution) {
       this.stepExecution = stepExecution;
   }
}
