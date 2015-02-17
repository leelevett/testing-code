package legacycode;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RunWith(PowerMockRunner.class)
public class LegacyCodeTest {
  // 3. We can not use @Rule TemporaryFolder as this is properties and we *need* it to be in "resources". Not tmp.
  // Hence this fudging.
  public static Path resourcePath;

  @BeforeClass
  public static void setUp() throws IOException {
    // 4. note: test-classes. That took an age to figure out. For when you haven't got anywhere to put resources to be compiled into test-classes.
    resourcePath = Paths.get("target/test-classes/conditional.singleton.properties");
    Files.createFile(resourcePath);
  }
  @AfterClass
  public static void tearDown() throws IOException {
    if(Files.exists(resourcePath)){
      FileUtils.deleteQuietly(resourcePath.toFile());
    }
  }

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
