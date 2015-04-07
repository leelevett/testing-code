package legacycode

import org.apache.commons.io.FileUtils
import org.junit.Rule
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.rule.PowerMockRule
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Paths

import static org.mockito.internal.verification.VerificationModeFactory.times
import static org.mockito.internal.verification.VerificationModeFactory.times
import static org.powermock.api.mockito.PowerMockito.doReturn
import static org.powermock.api.mockito.PowerMockito.doReturn
import static org.powermock.api.mockito.PowerMockito.mock
import static org.powermock.api.mockito.PowerMockito.mockStatic
import static org.powermock.api.mockito.PowerMockito.spy
import static org.powermock.api.mockito.PowerMockito.verifyPrivate
import static org.powermock.api.mockito.PowerMockito.verifyPrivate
import static org.powermock.api.mockito.PowerMockito.when
import static org.powermock.api.mockito.PowerMockito.whenNew

// 1. All the POM things. 1.6.2, try it at work. Went wibbly at home.
// 2. Rename test class to avoid clash.
//6. Do the powermocking...
@PrepareForTest([ ConditionalSingleton, LegacyIdProvider, LegacyCode ])
class LegacyCodeTestGroovy extends Specification {
  @Rule PowerMockRule rule = new PowerMockRule()
  def static resourcePath

  // 5. deal with this.
  def setupSpec() {
    resourcePath = Paths.get("target/test-classes/conditional.singleton.properties");
    Files.createFile(resourcePath);
  }
  def cleanupSpec() {
    if (Files.exists(resourcePath)){
      FileUtils.deleteQuietly(resourcePath.toFile());
    }
  }

  def setupX() {
    final spiedConditional = spy(ConditionalSingleton.getInstance())
    doReturn(true).when(spiedConditional).isAvailable()

    mockStatic(ConditionalSingleton.class)
    doReturn(spiedConditional).when(ConditionalSingleton, "getInstance")
  }

  // 4. copy this lot in.
  def "test matching inner method call" (paramValue) {
    given:
      setupX()
      def legacyCodeSpy = newSpiedLegacyCodeWithId()
    when:
      legacyCodeSpy.doLegacyOperation(paramValue)
    then:
      verifyPrivate(legacyCodeSpy, times(1)).invoke("matchingOperation")
      verifyPrivate(legacyCodeSpy, times(0)).invoke("unMatchedOperation")
    where:
      paramValue << ["matching", "also matching"]
  }

  // 3 copy this lot in,
  private LegacyCode newSpiedLegacyCodeWithId() throws Exception {
    final idProvider = mock(LegacyIdProvider.class)
    when(idProvider.getId()).thenReturn("12345")
    whenNew(LegacyIdProvider.class).withAnyArguments().thenReturn(idProvider)

    return spy(new LegacyCode())
  }
}