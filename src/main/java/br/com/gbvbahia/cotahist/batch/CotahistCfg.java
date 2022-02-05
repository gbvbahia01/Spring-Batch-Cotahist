package br.com.gbvbahia.cotahist.batch;

import javax.sql.DataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import br.com.gbvbahia.cotahist.model.Line;
import br.com.gbvbahia.cotahist.repository.CotahistRepository;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class CotahistCfg {

  public static final String HEADER_ID_PROMOTION = "headerId";

  private static final String STRING_OVERRIDDEN_BY_EXPRESSION = null;
  private static final Long LONG_OVERRIDDEN_BY_EXPRESSION = null;
  private static final Integer HEADER_LINE = 1;

  private final JobBuilderFactory jobsFactory;
  private final StepBuilderFactory stepsFactory;
  private final NotificationListener listener;
  private final CotahistRepository cotahistRepository;

  @Bean
  public Job cotahistJob(Step stepHeaderProcess,
      Step stepImportCotahist,
      Step stepTrailerProcess) {
    
    return jobsFactory.get("cotahistJob")
        .listener(listener)
        .start(stepHeaderProcess)
        .next(stepImportCotahist)
        .next(stepTrailerProcess)
        .build();
  }

  // Step 1 Header line
  @Bean
  public Step stepHeaderProcess() {
    return this.stepsFactory.get("stepHeaderProcess")
        .tasklet(headerTasklet(STRING_OVERRIDDEN_BY_EXPRESSION))
        .listener(promotionListener()) 
        .build();
  }

  @Bean
  @StepScope // Needs to be recreated each execution because of jobParameters.
  public HeaderProcessTasklet headerTasklet(@Value("#{jobParameters['header']}") String header) {
    return HeaderProcessTasklet.builder().header(header)
        .cotahistRepository(cotahistRepository)
        .build();
  }

  @Bean
  public ExecutionContextPromotionListener promotionListener() {
    ExecutionContextPromotionListener listener = new ExecutionContextPromotionListener();

    listener.setKeys(new String[] {HEADER_ID_PROMOTION});

    return listener;
  }

  // Step 2 File lines
  @Bean
  public Step stepImportCotahist(JdbcBatchItemWriter<Line> lineItemWriter,
      @Value("${app.line.chunk}") Integer chunk,
      @Value("${app.line.max-concurrent-threads}") Integer maxConcurrentThreads) {

    SimpleAsyncTaskExecutor simpleAsyncTaskExecutor = new SimpleAsyncTaskExecutor();
    simpleAsyncTaskExecutor.setConcurrencyLimit(maxConcurrentThreads);

    return stepsFactory.get("stepImportCotahist").<Line, Line>chunk(chunk)
        .reader(lineItemReader(STRING_OVERRIDDEN_BY_EXPRESSION, LONG_OVERRIDDEN_BY_EXPRESSION))
        .processor(lineSetHeaderIdProcessor())
        .writer(lineItemWriter)
        .taskExecutor(simpleAsyncTaskExecutor).build();
  }

  @Bean
  @StepScope
  public FlatFileItemReader<Line> lineItemReader(
      @Value("#{jobParameters['pathToFile']}") String pathToFile,
      @Value("#{jobParameters['lines']}") Long lines) {

    FlatFileItemReader<Line> reader = new FlatFileItemReaderBuilder<Line>()
        .name("lineItemReader")
        .resource(new FileSystemResource(pathToFile))
        .lineMapper(getLineMapper(lines.intValue()))
        .linesToSkip(HEADER_LINE).build();

    return reader;
  }
  
  @Bean
  public LineSetHeaderIdProcessor lineSetHeaderIdProcessor() {
    return LineSetHeaderIdProcessor.builder().build();
  }
  
  @Bean
  public JdbcBatchItemWriter<Line> lineItemWriter(final DataSource dataSource,
      @Value("${app.line.query.insert}") String sql) {
    return new JdbcBatchItemWriterBuilder<Line>()
        .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>()).sql(sql)
        .dataSource(dataSource).build();
  }

  private LineMapper<Line> getLineMapper(Integer trailerLineToSkip) {
    SkipLineDefaultLineMapper<Line> mapper = new SkipLineDefaultLineMapper<>(trailerLineToSkip);
    mapper.setLineTokenizer(getLineTokenizer());
    BeanWrapperFieldSetMapper<Line> fieldSetMapper = new BeanWrapperFieldSetMapper<Line>();
    fieldSetMapper.setTargetType(Line.class);
    fieldSetMapper.setConversionService(StringToDate.createDateConversionService());
    mapper.setFieldSetMapper(fieldSetMapper);
    return mapper;
  }

  private LineTokenizer getLineTokenizer() {
    FixedLengthTokenizer tokenizer = new FixedLengthTokenizer();
    tokenizer.setNames(new String[] {"tipoRegistro", "dataPregao", "bdi", "codNeg", "tpMerc",
        "nomRes", "especi", "prazot", "modref", "preAbe", "preMax", "preMin", "preMed", "preUlt",
        "preOfc", "preOfv", "totNeg", "quaTot", "volTot", "preExe", "indOpc", "datVen", "fatCot",
        "ptoExe", "codIsi", "dismes"});

    tokenizer.setColumns(new Range(1, 2), new Range(3, 10), new Range(11, 12), new Range(13, 24),
        new Range(25, 27), new Range(28, 39), new Range(40, 49), new Range(50, 52),
        new Range(53, 56), new Range(57, 69), new Range(70, 82), new Range(83, 95),
        new Range(96, 108), new Range(109, 121), new Range(122, 134), new Range(135, 147),
        new Range(148, 152), new Range(153, 170), new Range(171, 188), new Range(189, 201),
        new Range(202, 202), new Range(203, 210), new Range(211, 217), new Range(218, 230),
        new Range(231, 242), new Range(243, 245));
    return tokenizer;
  }

  // Step 3 Trailer line
  @Bean
  public Step stepTrailerProcess() {
    return this.stepsFactory.get("stepTrailerProcess")
        .tasklet(trailerProcessTasklet(STRING_OVERRIDDEN_BY_EXPRESSION))
        .build();
  }

  @Bean
  @StepScope // Needs to be recreated each execution because of jobParameters.
  public TrailerProcessTasklet trailerProcessTasklet(@Value("#{jobParameters['trailer']}") String trailer) {
    return TrailerProcessTasklet.builder().trailer(trailer)
        .cotahistRepository(cotahistRepository)
        .build();
  }
}
