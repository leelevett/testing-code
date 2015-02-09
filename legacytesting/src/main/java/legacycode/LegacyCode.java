package legacycode;

public class LegacyCode {
  private static final boolean conditionalAvailable = ConditionalSingleton.getInstance().isAvailable();
  private static final boolean property = PropertyHolder.getInstance().getProperty();
  private final LegacyIdProvider idProvider;

  public LegacyCode() {
    idProvider = new LegacyIdProvider("12345");
  }

  public void doLegacyOperation() {

  }
}
