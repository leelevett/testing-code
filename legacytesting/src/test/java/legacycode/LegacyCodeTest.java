package legacycode;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class LegacyCodeTest {

  // Availability monitor (ConditionalSingleton) *only* starts up without exception if props file is present.
  // We only need the thing there. We don't care what's in it. So we'll fake it
  // With a rule. Maybe? For some reason not in the actual code though?

  @Test
  public void test() throws Exception {
    LegacyCode legacy = givenNewLegacyCodeObjectWithId();
    whenLegacyOperationIsPerformed(legacy);

    // verify doThat & inner method called.
  }

  private void whenLegacyOperationIsPerformed(LegacyCode legacy) {
    legacy.doLegacyOperation();
  }

  private LegacyCode givenNewLegacyCodeObjectWithId() throws Exception {
    // 1. mock id provider. Constructors.
    // 2. PowerMockRunner
    final LegacyIdProvider idProvider = mock(LegacyIdProvider.class);
    when(idProvider.getId()).thenReturn("98765");
    whenNew(LegacyIdProvider.class).withAnyArguments().thenReturn(idProvider);
    return new LegacyCode();
  }
}
