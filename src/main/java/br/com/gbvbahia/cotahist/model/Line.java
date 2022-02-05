package br.com.gbvbahia.cotahist.model;

import java.math.BigDecimal;
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
@EqualsAndHashCode(of = {"dataPregao", "codNeg", "codIsi"})
public class Line {

  private Long id;

  /**
   * TIPREG - TIPO DE REGISTRO<br>
   * FIXO "01"
   */
  // @FileMap(order = 1, start = 1, length = 2)
  private int tipoRegistro;

  /**
   * DATA DO PREGAO<br>
   * FORMATO "AAAAMMDD"
   */
  // @FileMap(order = 2, start = 3, length = 8)
  private Date dataPregao;

  /**
   * CODBDI - CODIGO BDI<br>
   * UTILIZADO PARA CLASSIFICAR OS PAPEIS NA EMISSAO DO BOLETIM DIARIO DE INFORMACOES <br>
   * VTA
   */
  // @FileMap(order = 3, start = 11, length = 2)
  private int bdi;

  /**
   * CODNEG - CODIGO DE NEGOCIACAO DO PAPEL
   */
  // @FileMap(order = 4, start = 13, length = 12)
  private String codNeg;

  /**
   * TPMERC - TIPO DE MERCADO<br>
   * COD. DO MERCADO EM QUE O PAPEL ESTA CADASTRADO <br>
   * VTA
   */
  // @FileMap(order = 5, start = 25, length = 3)
  private String tpMerc;

  /**
   * NOMRES - NOME RESUMIDO DA EMPRESA EMISSORA DO PAPEL
   */
  // @FileMap(order = 6, start = 28, length = 12)
  private String nomRes;

  /**
   * ESPECI - ESPECIFICACAO DO PAPEL <br>
   * VTA
   */
  // @FileMap(order = 7, start = 40, length = 10)
  private String especi;

  /**
   * PRAZOT - PRAZO EM DIAS DO MERCADO A TERMO
   */
  // @FileMap(order = 8, start = 50, length = 3)
  private String prazot;

  /**
   * MODREF - MOEDA DE REFERENCIA<br>
   * MOEDA USADA NA DATA DO PREGAO
   */
  // @FileMap(order = 9, start = 53, length = 4)
  private String modref;

  /**
   * PREABE - PRECO DE ABERTURA DO PAPEL-MERCADO NO PREGAO
   */
  // @FileMap(order = 10, length = 13, start = 57, fraction = 100)
  private BigDecimal preAbe;

  /**
   * PREMAX - PRECO MAXIMO DO PAPEL-MERCADO NO PREGAO
   */
  // @FileMap(order = 11, start = 70, length = 13, fraction = 100)
  private BigDecimal preMax;

  /**
   * PREMIN - PRECO MINIMO DO PAPELMERCADO NO PREGAO
   */
  // @FileMap(order = 12, start = 83, length = 13, fraction = 100)
  private BigDecimal preMin;

  /**
   * PREMED - PRECO MEDIO DO PAPELMERCADO NO PREGAO
   */
  // @FileMap(order = 13, start = 96, length = 13, fraction = 100)
  private BigDecimal preMed;

  /**
   * PREULT - PRECO DO ULTIMO NEGOCIO DO PAPEL-MERCADO NO PREGAO
   */
  // @FileMap(order = 14, start = 109, length = 13, fraction = 100)
  private BigDecimal preUlt;

  /**
   * PREOFC - PRECO DA MELHOR OFERTA DE COMPRA DO PAPEL-MERCADO
   */
  // @FileMap(order = 15, start = 122, length = 13, fraction = 100)
  private BigDecimal preOfc;

  /**
   * PREOFV - PRECO DA MELHOR OFERTA DE VENDA DO PAPEL-MERCADO
   */
  // @FileMap(order = 16, start = 135, length = 13, fraction = 100)
  private BigDecimal preOfv;

  /**
   * TOTNEG - NEG. - NUMERO DE NEGOCIOS EFETUADOS COM O PAPELMERCADO NO PREGAO
   */
  // @FileMap(order = 17, start = 148, length = 5)
  private int totNeg;

  /**
   * QUATOT - QUANTIDADE TOTAL DE TITULOS NEGOCIADOS NESTE PAPELMERCADO
   */
  // @FileMap(order = 18, start = 153, length = 18)
  private long quaTot;

  /**
   * VOLTOT - VOLUME TOTAL DE TITULOS NEGOCIADOS NESTE PAPELMERCADO
   */
  // @FileMap(order = 19, start = 171, length = 18, fraction = 100)
  private long volTot;

  /**
   * PREEXE - PRECO DE EXERCICIO PARA O MERCADO DE OPCOES OU VALOR DO CONTRATO PARA O MERCADO DE
   * TERMO SECUNDARIO
   */
  // @FileMap(order = 20, start = 189, length = 13, fraction = 100)
  private BigDecimal preExe;

  /**
   * INDOPC - INDICADOR DE CORRECAO DE PRECOS DE EXERCICIOS OU VALORES DE CONTRATO PARA OS MERCADOS
   * DE OPCOES OU TERMO SECUNDARIO <br>
   * VTA
   */
  // @FileMap(order = 21, start = 202, length = 1)
  private String indOpc;

  /**
   * DATVEN - DATA DO VENCIMENTO PARA OS MERCADOS DE OPCOES OU TERMO SECUNDARIO<br>
   * FORMATO "AAAAMMDD"
   */
  // @FileMap(order = 22, start = 203, length = 8)
  private Date datVen;

  /**
   * FATCOT - FATOR DE COTACAO DO PAPEL <br>
   * '1' = COTACAO UNITARIA<br>
   * '1000' = COTACAO POR LOTE DE MIL ACOES
   */
  // @FileMap(order = 23, start = 211, length = 7)
  private int fatCot;

  /**
   * PTOEXE - PRECO DE EXERCICIO EM PONTOS PARA OPCOES REFERENCIADAS EM DOLAR OU VALOR DE CONTRATO
   * EM PONTOS PARA TERMO SECUNDARIO<br>
   * PARA OS REFERENCIADOS EM DOLAR, CADA PONTO EQUIVALE AO VALOR, NA MOEDA CORRENTE, DE UM
   * CENTESIMO DA TAXA MEDIA DO DOLAR COMERCIAL INTERBANCARIO DE FECHAMENTO DO DIA ANTERIOR, OU
   * SEJA, 1 PONTO = 1/100 US$
   */
  // @FileMap(order = 24, start = 218, length = 13, fraction = 1000000)
  private BigDecimal ptoExe;

  /**
   * CODISI - CODIGO DO PAPEL NO SISTEMA ISIN OU CODIGO INTERNO DO PAPEL<br>
   * CODIGO DO PAPEL NO SISTEMA ISIN A PARTIR DE 15-05-1995
   */
  // @FileMap(order = 25, start = 231, length = 12)
  private String codIsi;

  /**
   * DISMES - NUMERO DE DISTRIBUICAO DO PAPEL<br>
   * NUMERO DE SEQUENCIA DO PAPEL CORRESPONDENTE AO ESTADO DE DIREITO VIGENTE
   */
  // @FileMap(order = 26, start = 243, length = 3)
  private int dismes;
  /**
   * It is NOT at file, is for helping to FK with header file
   */
  private Long headerId;

}
