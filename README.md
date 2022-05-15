# Spring Batch COTAHIST B3  

#### This application imports the Brazilian Market Stocks B3 quotations file.   

> This Spring batch application was created as an example of how to pass parameters between job steps.     

For more information about the imported file, visit: [Bovespa Cotações Históricas](https://www.b3.com.br/pt_br/market-data-e-indices/servicos-de-dados/market-data/historico/mercado-a-vista/cotacoes-historicas/)

Spring Batch [Reference Documentation](https://docs.spring.io/spring-batch/docs/current-SNAPSHOT/reference/html/index-single.html)

#### Eclipse [Code Formatter](https://github.com/google/styleguide/blob/gh-pages/eclipse-java-google-style.xml)   
> The formatter style is in folder src/main/resources/google_styleguide.xml.   
To import: _preferences/Java/Code Style/Formatter/Import..._   


#### Java execution example:   
java -jar -Dspring.profiles.active=dev cotahist-1.0.0.jar "/folder/to/get/B3/COTAHIST_D04022022.TXT" "/folder/to/get/B3/COTAHIST_D03022022.TXT" 

### This application is an example of how to pass and get small parameters between Spring Batch Steps.

Most Spring Batch jobs have more than one step. And sometimes you need to save and recover some parameters between then, like:

	- A JWT token to be used in header requests on some processes.     
	- A row ID that will be used as a foreign key.   
	- The number of rows in a file.   

The possibilities for need are enormous.Fortunately, Spring Batch has a mechanism to do that.   
I wrote this to make it easy to understand how to save and where to recover those values.

To have any data available for next steps, it is necessary to give a promotion listener on step that will insert the values in the step execution context. To do this, it is necessary to create an ExecutionContextPromotionListener bean like this:

```java
  @Bean
  public ExecutionContextPromotionListener promotionListener() {
    ExecutionContextPromotionListener listener = new ExecutionContextPromotionListener();

    listener.setKeys(new String[] {HEADER_ID_PROMOTION, TOKEN_JSW_PROMOTION});

    return listener;
  }
```

When a step ends and has defined an ExecutionContextPromotionListener, any data inserted on the step execution context, where the key is in the listener keys array, will be stored in the job execution context.   
This is important to get: **only at the end**.   
If you store a variable on Reader and need to get on Writer or Processor at the same Step, the data will not be available at the Job Execution Context, but will be available at the Step Execution Context.   
Only when the Step is finished will Spring Batch promote the values in the Job Execution Context.

The way to configure ExecutionContextPromotionListener is informed as a listener on Step:

```java
@Bean
  public Step stepHeaderProcess() {
    return this.stepsFactory.get("stepHeaderProcess")
        .tasklet(headerTasklet(STRING_OVERRIDDEN_BY_EXPRESSION))
        .listener(promotionListener())
        .build();
  }
```

How do you insert a value on Step Execution Context depends on where you are:
* Tasklet Step

```java
public class HeaderTasklet implements Tasklet {
private String header;

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
      throws Exception {


    ExecutionContext executionContext = contribution.getStepExecution().getExecutionContext();
    executionContext.put(HEADER_ID_PROMOTION, header);

    return RepeatStatus.FINISHED;
  }
```


* Reader and Writer Step

```java
public class HeaderReader implements ItemReader<SomeDTO> {

StepExecution stepExecution;

 public SomeDTO read() throws Exception {

    ExecutionContext executionContext = stepExecution.getExecutionContext();
    executionContext.put(HEADER_ID_PROMOTION, header);
    
    return new SomeDTO();
  }

@BeforeStep
    public void beforeSpepExecution(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }
}
```

The way to recover any value in an Execution Context depends on whether you are on the same Step that you inserted it or on another one:

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
    this.headerId = stepExecution.getExecutionContext().getLong(CotahistCfg.HEADER_ID_PROMOTION);
    
  }
}
```

* Future Step

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

The important thing is to remember that there are two **ExecutionContext**.

> One is for Step and another is for the Job.   

You have to get from _JobExecution_ when the data was stored by any Step that ran before that is running now.   

When the data is stored in the same step, you must get from _StepExecution_.
It does not matter if it was stored on the Reader and you need to get onto the Process or Writer.


