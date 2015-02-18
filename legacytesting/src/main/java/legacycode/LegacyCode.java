package legacycode;

public class LegacyCode {
  private static final boolean conditionalAvailable = ConditionalSingleton.getInstance().isAvailable();
  private final LegacyIdProvider idProvider;

  public LegacyCode() {
    idProvider = new LegacyIdProvider("");
  }

  public void doLegacyOperation(final String parameter) {
    if (conditionalAvailable) {
      if (idProvider.getId().equals("12345") && RegexTester.isParameterMatching(parameter)) {
        doThis();
      } else {
        doThat();
      }
    }
  }

  private void doThis() {
    System.out.println("Don't do this");
  }

  private void doThat() {
    System.out.println("Do that");

  }
}
