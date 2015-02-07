package legacycode;

public class PropertyHolder {
  private PropertyHolder() {}

  private static class SingletonHolder {
    private static final PropertyHolder INSTANCE = new PropertyHolder();
  }

  public static PropertyHolder getInstance() {
    return SingletonHolder.INSTANCE;
  }

  public boolean getProperty() {
    return false; // We'll be mocking the opposite of this.
  }
}