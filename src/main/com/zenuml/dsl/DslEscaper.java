package com.zenuml.dsl;

import java.util.function.Function;

public class DslEscaper {
  public static Function<String, String> removeBacktick = s -> s.replaceAll("`", "");
}
