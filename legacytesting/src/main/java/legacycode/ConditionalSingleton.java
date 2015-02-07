package legacycode;

import javassist.NotFoundException;

import org.mockito.internal.creation.instance.InstantationException;

public class ConditionalSingleton {
  private ConditionalSingleton() {
    // Simulate some legacy code behaviour.
    if (this.getClass().getClassLoader().getResource("conditional.singleton.properties") == null) {
      throw new InstantationException("ConditionalSingleton couldn't be created...",
          new NotFoundException("Didn't find conditional.singleton.properties"));
    }
  }

  private static class SingletonHolder {
    private static final ConditionalSingleton INSTANCE = new ConditionalSingleton();
  }

  public static ConditionalSingleton getInstance() {
    return SingletonHolder.INSTANCE;
  }
}