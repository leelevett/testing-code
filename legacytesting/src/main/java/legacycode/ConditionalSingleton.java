package legacycode;

public class ConditionalSingleton {
  private ConditionalSingleton() {
    // Simulate some legacy code behaviour.
    if (getClass().getClassLoader().getResource("conditional.singleton.properties") == null) {
      throw new InstantiationError(
          "ConditionalSingleton couldn't be created: Didn't find conditional.singleton.properties");
    }
  }

  private static class SingletonHolder {
    private static final ConditionalSingleton INSTANCE = new ConditionalSingleton();
  }

  public static ConditionalSingleton getInstance() {
    return SingletonHolder.INSTANCE;
  }

  public boolean isAvailable() {
    return false; // We will mock this to true.
  }
}