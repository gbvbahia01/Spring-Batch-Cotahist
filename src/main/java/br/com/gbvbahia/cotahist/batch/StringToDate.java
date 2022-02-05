package br.com.gbvbahia.cotahist.batch;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringToDate {

  static ConversionService createDateConversionService() {
    
    DefaultConversionService stringToLocalDateconversionService = new DefaultConversionService();
    DefaultConversionService.addDefaultConverters(stringToLocalDateconversionService);
    stringToLocalDateconversionService.addConverter(new Converter<String, Date>() {

      @Override
      public Date convert(String text) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        try {
          return sdf.parse(text);
        } catch (Exception e) {
          log.error(e.getMessage(), e);
          return null;
        }
      }
    });

    return stringToLocalDateconversionService;

  }

}
