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

// 10. Note: Groovy mocks and Java don't mix. Find out why and explain. Use powermocks.
// 8 So we can't use PowerMockRunner any more if we want to use parameters for testing. New runner is...
// And now we need the x-stream thing. This is in preference to the JUnit rule for params, which are class wide.
// This can use several tests.
//@RunWith(PowerMockRunner.class)
@RunWith(JUnitParamsRunner.class)

// Legacy code cos that's where the statics are used. The statics themselves... Ordering matters.
@PrepareForTest({
    LegacyCode.class, ConditionalSingleton.class, LegacyIdProvider.class
})
public class LegacyCodeTest {
  // 8.2: Can't use PowerMockRunner? Use PowerMockRule instead :)
  @Rule
  public PowerMockRule rule = new PowerMockRule();

  // 3. We can not use @Rule TemporaryFolder as this is properties and we *need* it to be in "resources". Not tmp.
  // Hence this fudging.
  public static Path resourcePath;

  @BeforeClass
  public static void setUpClass() throws IOException {
    // 4. note: test-classes. That took an age to figure out. For when you haven't got anywhere to put resources to be compiled into test-classes.
    resourcePath = Paths.get("target/test-classes/conditional.singleton.properties");
    Files.createFile(resourcePath);
  }
  @AfterClass
  public static void tearDownClass() throws IOException {
    if(Files.exists(resourcePath)){
      FileUtils.deleteQuietly(resourcePath.toFile());
    }
  }

  // 5. Mock the singleton. static mocking to get mocked object which we make return true for the call.
  @Before
  public void setUpTest() throws Exception {
    final ConditionalSingleton spiedConditional = spy(ConditionalSingleton.getInstance());
    doReturn(true).when(spiedConditional).isAvailable(); // 6. doReturn required for spying. See docs.

    mockStatic(ConditionalSingleton.class); // 7. PrepareForTest!
    doReturn(spiedConditional).when(ConditionalSingleton.class, "getInstance");
  }

  @Test
  // 8.3: Add Annotation, which fills the new parameter in test method.
  @Parameters({"matching", "also matching"})
  public void testMatchingInnerMethodCall(final String parameter) throws Exception {
    LegacyCode legacy = givenNewLegacyCodeObjectWithId();

    whenLegacyOperationIsPerformed(legacy, parameter);

    // 9. Inner class method verification. What powermock is supposed to do...
    verifyPrivate(legacy, times(1)).invoke("matchingOperation");
    //verifyPrivate(legacy, VerificationModeFactory.times(0)).invoke("unMatchedOperation");
  }

  @Test
  @Parameters({"unmatched", "not a match"})
  public void testUnMatchedInnerMethodCall(final String parameter) throws Exception {
    LegacyCode legacy = givenNewLegacyCodeObjectWithId();

    whenLegacyOperationIsPerformed(legacy, parameter);

    // 9. Inner class method verification. What powermock is supposed to do...
    //verifyPrivate(legacy, VerificationModeFactory.times(0)).invoke("matchingOperation");
    verifyPrivate(legacy, times(1)).invoke("unMatchedOperation");
  }

  private LegacyCode givenNewLegacyCodeObjectWithId() throws Exception {
    // 1. mock id provider. Constructors.
    // 2. PowerMockRunner
    final LegacyIdProvider idProvider = mock(LegacyIdProvider.class);
    when(idProvider.getId()).thenReturn("12345");
    whenNew(LegacyIdProvider.class).withAnyArguments().thenReturn(idProvider); // 2.1 constructor mocking!
    return new LegacyCode();
  }

  private void whenLegacyOperationIsPerformed(final LegacyCode legacy, final String parameter) {
    legacy.doLegacyOperation(parameter);
  }
}
