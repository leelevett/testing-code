package legacycode;

public class LegacyIdProvider {
  private String id;

  public LegacyIdProvider(String args) {
    this.id = args;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
