package br.com.gbvbahia.cotahist.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(of = {"nomeArquivo"})
public class Trailer {

  private Long id;

  /**
   * TIPO DE REGISTRO<br>
   * FIXO "99"
   */
  // @FileMap(order = 1, start = 1, length = 2)
  private int tipoRegistro;

  /**
   * NOME DO ARQUIVO<br>
   * FIXO "COTAHIST.AAAA"
   */
  // @FileMap(order = 2, start = 3, length = 13)
  private String nomeArquivo;

  /**
   * CODIGO DA ORIGEM <br>
   * FIXO "BOVESPA"
   */
  // @FileMap(order = 3, start = 16, length = 8)
  private String codigoOrigem;

  /**
   * DATA DA GERACAO DO ARQUIVO<br>
   * FORMATO "AAAAMMDD"
   */
  // @FileMap(order = 4, start = 24, length = 8)
  private Date dataGeracaoArquivo;

  /**
   * TOTAL DE REGISTROS<br>
   * INCLUIR TAMBEM OS REGISTROS HEADER E TRAILER.
   */
  // @FileMap(order = 5, start = 32, length = 11)
  private long totalRegistros;
  /**
   * It is NOT at file, is for helping to FK with header file
   */
  private long headerId;

}
