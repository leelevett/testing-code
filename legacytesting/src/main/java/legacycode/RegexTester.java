package legacycode;

import java.util.regex.Pattern;

public class RegexTester {
  private static final Pattern PATTERN = Pattern.compile(".*?matching.*?");

  private RegexTester() {}

  public static boolean isParameterMatching(final String parameter) {
    return PATTERN.matcher(parameter).matches();
  }
}