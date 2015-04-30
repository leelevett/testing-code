package legacycode;

public class LegacyCode {
  private static final boolean conditionalAvailable = ConditionalSingleton.getInstance().isAvailable();
  private final LegacyIdProvider idProvider;
  private GroovyCondition groovyConditional;

  public LegacyCode() {
    idProvider = new LegacyIdProvider("");
    groovyConditional = new GroovyCondition();
  }

  public void setGroovyConditional(GroovyCondition conditional) {
    this.groovyConditional = conditional;
  }

  public void doLegacyOperation(final String parameter) {
    System.out.println(groovyConditional.isAllowed());
    if (groovyConditional.isAllowed()) {
      if (conditionalAvailable && idProvider.getId().equals("12345")) {
        if (RegexTester.isParameterMatching(parameter)) {
          matchingOperation();
        } else {
          unMatchedOperation();
        }
      }
    }
  }

  private void matchingOperation() {}

  private void unMatchedOperation() {}
}
