package br.com.gbvbahia.cotahist;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import br.com.gbvbahia.cotahist.batch.CotahistService;
import br.com.gbvbahia.cotahist.util.IOHelper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableAutoConfiguration
@SpringBootApplication
@AllArgsConstructor
@Slf4j
public class BatchRunner implements CommandLineRunner {

  private final CotahistService cotahistService;

  @Override
  public void run(String... args) throws Exception {
    log.info("Main request received:{}", String.join(", ", args));

    StopWatch sw = new StopWatch();
    sw.start();

    List<String> files = IOHelper.getFilesNameLike(args, "COTAHIST");
    for (String file : files) {

      Long lines = Files.lines(Paths.get(file)).count();
      String header = StringUtils.trimToEmpty(Files.lines(Paths.get(file)).findFirst().get());
      String trailer =
          StringUtils.trimToEmpty(Files.lines(Paths.get(file)).skip(lines - 1).findFirst().get());

      log.info("Lines:{} Header:{} Trailer:{}", lines, header, trailer);

      JobParameters jobParameters = new JobParametersBuilder()
          .addString("pathToFile", file, false)
          .addString("header", header)
          .addString("trailer", trailer)
          .addLong("lines", lines)
          .toJobParameters();

      cotahistService.startImportCotahistFile(jobParameters);
      IOHelper.zipAndMoveFileToFolder(file, "done");
    }

    sw.stop();
    log.info("BatchRunner finished: " + sw.toString());
  }

}
