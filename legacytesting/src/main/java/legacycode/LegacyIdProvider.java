package legacycode;

public class LegacyIdProvider {
  private final String id;

  public LegacyIdProvider(String args) {
    this.id = args;
  }

  public String getId() {
    return id;
  }
}
