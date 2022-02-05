package br.com.gbvbahia.cotahist.repository;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import br.com.gbvbahia.cotahist.model.Header;
import br.com.gbvbahia.cotahist.model.Trailer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Repository
public class CotahistRepository {

  @Value("${app.header.query.insert}")
  private String headerInsertQuery;
  
  @Value("${app.trailer.query.insert}")
  private String trailerInsertQuery;

  private final DataSource dataSource;

  public Header save(final Header header) {

    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
    namedParameters.addValue("codigoOrigem", header.getCodigoOrigem());
    namedParameters.addValue("dataGeracaoArquivo", header.getDataGeracaoArquivo());
    namedParameters.addValue("nomeArquivo", header.getNomeArquivo());
    namedParameters.addValue("tipoRegistro", header.getTipoRegistro());

    GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();

    NamedParameterJdbcTemplate named = new NamedParameterJdbcTemplate(dataSource);
    named.update(headerInsertQuery, namedParameters, generatedKeyHolder);

    Long headerId = Long.valueOf(generatedKeyHolder.getKeys().get("id").toString());

    log.debug("Header: {} created.", headerId);
    
    return Header.builder().id(headerId)
        .codigoOrigem(header.getCodigoOrigem())
        .dataGeracaoArquivo(header.getDataGeracaoArquivo())
        .nomeArquivo(header.getNomeArquivo())
        .tipoRegistro(header.getTipoRegistro())
        .build();
  }
  
  public Trailer save(final Trailer trailer) {
    
    MapSqlParameterSource namedParameters = new MapSqlParameterSource();
    namedParameters.addValue("codigoOrigem", trailer.getCodigoOrigem());
    namedParameters.addValue("dataGeracaoArquivo", trailer.getDataGeracaoArquivo());
    namedParameters.addValue("nomeArquivo", trailer.getNomeArquivo());
    namedParameters.addValue("tipoRegistro", trailer.getTipoRegistro());
    namedParameters.addValue("totalRegistros", trailer.getTotalRegistros());
    namedParameters.addValue("headerId", trailer.getHeaderId());
    
    GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();

    NamedParameterJdbcTemplate named = new NamedParameterJdbcTemplate(dataSource);
    named.update(trailerInsertQuery, namedParameters, generatedKeyHolder);

    Long trailerId = Long.valueOf(generatedKeyHolder.getKeys().get("id").toString());

    log.debug("Trailer: {} created.", trailerId);
    
    return Trailer.builder().id(trailerId)
        .codigoOrigem(trailer.getCodigoOrigem())
        .dataGeracaoArquivo(trailer.getDataGeracaoArquivo())
        .nomeArquivo(trailer.getNomeArquivo())
        .tipoRegistro(trailer.getTipoRegistro())
        .totalRegistros(trailer.getTotalRegistros())
        .headerId(trailer.getHeaderId())
        .build();
  }

}
