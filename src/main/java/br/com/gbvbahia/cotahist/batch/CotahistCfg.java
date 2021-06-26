package br.com.gbvbahia.cotahist.batch;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import br.com.gbvbahia.cotahist.model.Line;

@Configuration
@EnableBatchProcessing
public class CotahistCfg {

   private static final String OVERRIDDEN_BY_EXPRESSION = null;
   
   @Autowired
   private JobBuilderFactory jobsFactory;

   @Autowired
   private StepBuilderFactory stepsFactory;
   
   @Autowired
   private NotificationListener listener;

   @Bean
   public Job cotahistJob(Step stepImportCotahist){
       return jobsFactory.get("cotahistJob")
               .incrementer(new RunIdIncrementer())
               .listener(listener)
               .start(stepImportCotahist)
               .build();
   }
   
   @Bean
   public Step stepImportCotahist(JdbcBatchItemWriter<Line> lineItemWriter,
                     @Value("${app.line.chunk}") Integer chunk,
                     @Value("${app.line.max-concurrent-threads}") Integer maxConcurrentThreads){
      
      SimpleAsyncTaskExecutor simpleAsyncTaskExecutor = new SimpleAsyncTaskExecutor();
      simpleAsyncTaskExecutor.setConcurrencyLimit(maxConcurrentThreads);
      
       return stepsFactory.get("step1").
               <Line,Line>chunk(chunk)
               .reader(lineItemReader(OVERRIDDEN_BY_EXPRESSION))
               .writer(lineItemWriter)
               .taskExecutor(simpleAsyncTaskExecutor)
               .build();
   }
   
   @Bean
   @StepScope
   public FlatFileItemReader<Line> lineItemReader(@Value("#{jobParameters['pathToFile']}") String pathToFile) {
      
      FlatFileItemReader<Line>reader = new FlatFileItemReaderBuilder<Line>()
      .name("lineItemReader")
      .resource(new FileSystemResource(pathToFile))
      .lineMapper(getLineMapper())
      .build();
      
      reader.setLinesToSkip(1);
      
       return reader;
                
   }
   @Bean
   public JdbcBatchItemWriter<Line> lineItemWriter(final DataSource dataSource, @Value("${app.line.query.insert}") String sql) {
       return new JdbcBatchItemWriterBuilder<Line>()
               .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
               .sql(sql)
               .dataSource(dataSource)
               .build();
   }
   
   private LineMapper<Line> getLineMapper() {
      DefaultLineMapper<Line> mapper = new DefaultLineMapper<>();
      mapper.setLineTokenizer(getLineTokenizer());
      BeanWrapperFieldSetMapper<Line> fieldSetMapper = new BeanWrapperFieldSetMapper<Line>();
      fieldSetMapper.setTargetType(Line.class);
      fieldSetMapper.setConversionService(StringToDate.createDateConversionService());
      mapper.setFieldSetMapper(fieldSetMapper);
      return mapper;
   }
               
   private LineTokenizer getLineTokenizer() {
      FixedLengthTokenizer tokenizer = new FixedLengthTokenizer();
      tokenizer.setNames(new String[]{"tipoRegistro","dataPregao","bdi","codNeg","tpMerc",
                                      "nomRes","especi","prazot","modref","preAbe","preMax",
                                      "preMin", "preMed", "preUlt", "preOfc", "preOfv",
                                      "totNeg","quaTot", "volTot", "preExe", "indOpc",
                                      "datVen", "fatCot", "ptoExe", "codIsi", "dismes"});
      
      tokenizer.setColumns(    new Range(1, 2),    new Range(3, 10),   new Range(11, 12),   new Range(13, 24),
                             new Range(25, 27),   new Range(28, 39),   new Range(40, 49),   new Range(50, 52),
                             new Range(53, 56),   new Range(57, 69),   new Range(70, 82),   new Range(83, 95),
                            new Range(96, 108), new Range(109, 121), new Range(122, 134), new Range(135, 147),
                           new Range(148, 152), new Range(153, 170), new Range(171, 188), new Range(189, 201),
                           new Range(202, 202), new Range(203, 210), new Range(211, 217), new Range(218, 230),
                           new Range(231, 242), new Range(243, 245));
      return tokenizer;
   }
   
}
