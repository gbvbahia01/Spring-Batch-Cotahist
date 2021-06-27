package br.com.gbvbahia.cotahist.batch;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import br.com.gbvbahia.cotahist.model.Header;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Builder
public class HeaderItemWriter implements ItemWriter<Header> {

   private final DataSource dataSource;
   private final String insertQuery;
   
   private StepExecution stepExecution;
   
   @Override
   public void write(List<? extends Header> items) throws Exception {

      Header header = items.stream().findFirst().get();
      
      MapSqlParameterSource namedParameters = new MapSqlParameterSource();
      namedParameters.addValue("codigoOrigem", header.getCodigoOrigem());
      namedParameters.addValue("dataGeracaoArquivo", header.getDataGeracaoArquivo());
      namedParameters.addValue("nomeArquivo", header.getNomeArquivo());
      namedParameters.addValue("tipoRegistro", header.getTipoRegistro());
      
      GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
      
      NamedParameterJdbcTemplate named = new NamedParameterJdbcTemplate(dataSource);
      named.update(insertQuery, namedParameters, generatedKeyHolder);
      
      Long headerId = Long.valueOf(generatedKeyHolder.getKeys().get("id").toString());
      
      stepExecution.getExecutionContext().put("headerId", headerId);
      
      log.info("Header id is:{}", header);
   }

   @BeforeStep
   public void saveStepExecution(StepExecution stepExecution) {
       this.stepExecution = stepExecution;
   }
   
}
