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
@EqualsAndHashCode(of = { "nomeArquivo" })
public class Header {

   private Long id;

   // @FileMap(order = 1, start = 1, length = 2)
   private int tipoRegistro;

   // @FileMap(order = 2, start = 3, length = 13)
   private String nomeArquivo;

   // @FileMap(order = 3, start = 16, length = 8)
   private String codigoOrigem;

   // @FileMap(order = 4, start = 24, length = 8)
   private Date dataGeracaoArquivo;

}
