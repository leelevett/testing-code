package legacycode;

public class LegacyCode {
  private static final boolean conditionalAvailable = ConditionalSingleton.getInstance().isAvailable();
  private static final boolean propertyTrue = PropertyHolder.getInstance().getProperty();
  private final LegacyIdProvider idProvider;

  public LegacyCode() {
    idProvider = new LegacyIdProvider("12345");
  }

  public void doLegacyOperation() {
    if (conditionalAvailable && propertyTrue) {
      if (idProvider.getId().equals("12345")) {
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
