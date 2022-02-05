package br.com.gbvbahia.cotahist.batch;

import org.springframework.batch.item.file.mapping.DefaultLineMapper;

public class SkipLineDefaultLineMapper<T> extends DefaultLineMapper<T> {

  private final int lineToSkip;

  public SkipLineDefaultLineMapper(int lineToSkip) {
    super();
    this.lineToSkip = lineToSkip;
  }

  @Override
  public T mapLine(String line, int lineNumber) throws Exception {

    if (lineNumber >= lineToSkip) {
      return null;
    }

    return super.mapLine(line, lineNumber);
  }



}
