package legacycode;

public class LegacyCode {
  private static final boolean conditionalAvailable = ConditionalSingleton.getInstance().isAvailable();
  private final LegacyIdProvider idProvider;

  public LegacyCode() {
    idProvider = new LegacyIdProvider("");
  }

  public void doLegacyOperation(final String parameter) {
    if (conditionalAvailable && idProvider.getId().equals("12345")) {
      if (RegexTester.isParameterMatching(parameter)) {
        matchingOperation();
      } else {
        unMatchedOperation();
      }
    }
  }

  private void matchingOperation() {
    return;
  }

  private void unMatchedOperation() {
    return;
  }
}
