package legacycode;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;
import static org.powermock.api.mockito.PowerMockito.verifyPrivate;
import static org.mockito.internal.verification.VerificationModeFactory.times;

// 0! Groovy conversion will have a few gotchas. Worth mentioning. Can use Powermockito.
// Only with Rule. Runner needs to remain spock based. Rule incompatible with JAXB, if you need
// to mock that or use it, you may well have problems and should stick with Java based tests for now.

@RunWith(JUnitParamsRunner.class)
@PrepareForTest({
    LegacyCode.class, ConditionalSingleton.class, LegacyIdProvider.class
})
public class LegacyCodeTest {
  @Rule
  public PowerMockRule rule = new PowerMockRule();
  public static Path resourcePath;

  @BeforeClass
  public static void setUpClass() throws IOException {
    resourcePath = Paths.get("target/test-classes/conditional.singleton.properties");
    Files.createFile(resourcePath);
  }
  @AfterClass
  public static void tearDownClass() throws IOException {
    if(Files.exists(resourcePath)){
      FileUtils.deleteQuietly(resourcePath.toFile());
    }
  }

  @Before
  public void setUpTest() throws Exception {
    final ConditionalSingleton spiedConditional = spy(ConditionalSingleton.getInstance());
    doReturn(true).when(spiedConditional).isAvailable();

    mockStatic(ConditionalSingleton.class);
    doReturn(spiedConditional).when(ConditionalSingleton.class, "getInstance");
  }

  @Test
  @Parameters({"matching", "also matching"})
  public void testMatchingInnerMethodCall(final String parameter) throws Exception {
    LegacyCode legacy = givenNewLegacyCodeObjectWithId();

    whenLegacyOperationIsPerformed(legacy, parameter);

    verifyPrivate(legacy, times(1)).invoke("matchingOperation");
    verifyPrivate(legacy, times(0)).invoke("unMatchedOperation");
  }

  @Test
  @Parameters({"unmatched", "not a match"})
  public void testUnMatchedInnerMethodCall(final String parameter) throws Exception {
    LegacyCode legacy = givenNewLegacyCodeObjectWithId();

    whenLegacyOperationIsPerformed(legacy, parameter);

    verifyPrivate(legacy, times(0)).invoke("matchingOperation");
    verifyPrivate(legacy, times(1)).invoke("unMatchedOperation");
  }

  private LegacyCode givenNewLegacyCodeObjectWithId() throws Exception {
    final LegacyIdProvider idProvider = mock(LegacyIdProvider.class);
    when(idProvider.getId()).thenReturn("12345");
    whenNew(LegacyIdProvider.class).withAnyArguments().thenReturn(idProvider);
    return spy(new LegacyCode());
  }

  private void whenLegacyOperationIsPerformed(final LegacyCode legacy, final String parameter) {
    legacy.doLegacyOperation(parameter);
  }
}
