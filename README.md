# Spring Batch COTAHIST B3  

#### This application imports the Brazilian Market Stocks B3 quotations file.   

> Spring batch application created as example how to pass parameters between job steps.   

More information about this file visit: [Bovespa Cotações Históricas](https://www.b3.com.br/pt_br/market-data-e-indices/servicos-de-dados/market-data/historico/mercado-a-vista/cotacoes-historicas/)

#### Eclipse [Code Formatter](https://github.com/google/styleguide/blob/gh-pages/eclipse-java-google-style.xml)   
> The formatter style is in folder src/main/resources/google_styleguide.xml.   
To import: _preferences/Java/Code Style/Formatter/Import..._   


#### Java execution example:   
java -jar -Dspring.profiles.active=dev cotahist-1.0.0.jar "/folder/to/get/B3/COTAHIST_D04022022.TXT"

