//> using jvm 17
//> using dep "org.assertj:assertj-core:3.25.3"
//> using dep "org.apache.commons:commons-lang3:3.14.0"

import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;

public class Boom {

  public static void main(String[] args) {
    Assertions.assertThat(StringUtils.isBlank("  ")).isFalse();
  }
}
