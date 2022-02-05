# Spring Batch COTAHIST B3  

#### This application imports the Brazilian Market Stocks B3 quotations file.   

> Spring batch application created as example how to pass parameters between job steps.   

More information about this file visit: [Bovespa Cotações Históricas](https://www.b3.com.br/pt_br/market-data-e-indices/servicos-de-dados/market-data/historico/mercado-a-vista/cotacoes-historicas/)

Spring Batch [Reference Documentation](https://docs.spring.io/spring-batch/docs/current-SNAPSHOT/reference/html/index-single.html)

#### Eclipse [Code Formatter](https://github.com/google/styleguide/blob/gh-pages/eclipse-java-google-style.xml)   
> The formatter style is in folder src/main/resources/google_styleguide.xml.   
To import: _preferences/Java/Code Style/Formatter/Import..._   


#### Java execution example:   
java -jar -Dspring.profiles.active=dev cotahist-1.0.0.jar "/folder/to/get/B3/COTAHIST_D04022022.TXT"

### This application is a example how to pass and get small parameters between Spring Batch Steps.

Most Spring Batch jobs have more than one step. And some times you need to pass some parameters between then, like:   

	- A JWT to use on rest request on some process.   
	- A row Id that will be used as foreign key.   
	- A number of rows in a file.   

The possibilities of needs are huge. Fortunately Spring Batch has a mechanism to do that.   
I am written this to make easy to understand how to save and where to recover those values.

To be any data available to next steps is necessary to give a promotion listener on Step that will insert the values on Step Execution Context. To do this is necessary to create a ExecutionContextPromotionListener bean like this:

```java
  @Bean
  public ExecutionContextPromotionListener promotionListener() {
    ExecutionContextPromotionListener listener = new ExecutionContextPromotionListener();

    listener.setKeys(new String[] {HEADER_ID_PROMOTION, TOKEN_JSW_PROMOTION});

    return listener;
  }
```

When a Step **ends** and has defined a ExecutionContextPromotionListener, any data inserted on the Step ExecutionContext where the key is in the listener keys array, will be stored on the Job Execution Context. This is important to get, **only at the end**. If you store a variable on Reader and needs to get on Writer or Processor at the same Step, the data will not be available at Job Execution Context, but will be available on Step Execution Context. Only when the Step is finished Spring Batch will promote the values at Job Execution Context. Only values linked with listener keys informed on promotion listener.

The way to configure ExecutionContextPromotionListener is inform as listener on Step:

```java
@Bean
  public Step stepHeaderProcess() {
    return this.stepsFactory.get("stepHeaderProcess")
        .tasklet(headerTasklet(STRING_OVERRIDDEN_BY_EXPRESSION))
        .listener(promotionListener())
        .build();
  }
```

How do you insert a value on Step Execution Context depends where you are:
* Tasklet Step

```java
public class HeaderTasklet implements Tasklet {
private String header;

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
      throws Exception {


    ExecutionContext executionContext = contribution.getStepExecution().getExecutionContext();
    executionContext(HEADER_ID_PROMOTION, header);

    return RepeatStatus.FINISHED;
  }
```


* Reader and Writer Step

```java
public class HeaderReader implements ItemReader<SomeDTO> {

StepExecution stepExecution;

 public SomeDTO read() throws Exception {

    ExecutionContext executionContext = stepExecution.getExecutionContext();
    executionContext(HEADER_ID_PROMOTION, header);
    
    return new SomeDTO();
  }

@BeforeStep
    public void beforeSpepExecution(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }
}
```

The way to recover any value on Execution Context depends if you are on the same Step that you insert it or in another one:

* Same Step

```java
public class LineSetHeaderIdProcessor implements ItemProcessor<Line, Line> {
  private Long headerId;

  public Line process(Line item) throws Exception {
    item.setHeaderId(headerId);
    return item;
  }

  @BeforeStep
  public void retrieveInterstepData(StepExecution stepExecution) {
    //Same Step recover
    this.headerId = 	 stepExecution.getExecutionContext().getLong(CotahistCfg.HEADER_ID_PROMOTION);
    
  }
}
```

Future Step

```java
public class LineSetHeaderIdProcessor implements ItemProcessor<Line, Line> {
  private Long headerId;

  public Line process(Line item) throws Exception {
    item.setHeaderId(headerId);
    return item;
  }

  @BeforeStep
  public void retrieveInterstepData(StepExecution stepExecution) {
    //Recovering data inserted a step before the actual Step
    JobExecution jobExecution = stepExecution.getJobExecution();
    ExecutionContext jobContext = jobExecution.getExecutionContext();
    this.headerId = jobContext.getLong(CotahistCfg.HEADER_ID_PROMOTION);
  }
}
```

The important thing is to remember that exists two ExecutionContext. One is for Step and another is for the Job.   
You have to get from _JobExecution_ when the data was stored at any step before that is running now.   
You have to get from _StepExecution_ when the data was stored in the same Step. Does not matter if was stored on Reader and you need to get on the Process or Writer.    


