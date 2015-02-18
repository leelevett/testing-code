package legacycode;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

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

@RunWith(PowerMockRunner.class)
// Legacy code cos that's where the statics are used. The statics themselves... Ordering matters.
@PrepareForTest({
    LegacyCode.class, ConditionalSingleton.class
})
public class LegacyCodeTest {
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
  public void testInnerMethodCall() throws Exception {
    LegacyCode legacy = givenNewLegacyCodeObjectWithId();

    whenLegacyOperationIsPerformed(legacy);

    // verify doThat & inner method called.
  }

  private LegacyCode givenNewLegacyCodeObjectWithId() throws Exception {
    // 1. mock id provider. Constructors.
    // 2. PowerMockRunner
    final LegacyIdProvider idProvider = mock(LegacyIdProvider.class);
    when(idProvider.getId()).thenReturn("12345");
    whenNew(LegacyIdProvider.class).withAnyArguments().thenReturn(idProvider); // 2.1 constructor mocking!
    return new LegacyCode();
  }

  private void whenLegacyOperationIsPerformed(LegacyCode legacy) {
    legacy.doLegacyOperation("matching"); //8 TODO PARAMETERS!
  }
}
