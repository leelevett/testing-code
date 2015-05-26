package legacycode

import org.apache.commons.io.FileUtils
import org.junit.Rule
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.rule.PowerMockRule
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Paths

import static org.mockito.internal.verification.VerificationModeFactory.times
import static org.powermock.api.mockito.PowerMockito.*

@PrepareForTest([ ConditionalSingleton, LegacyIdProvider, LegacyCode, GroovyCondition ])
class LegacyCodeTestGroovy extends Specification {
  @Rule PowerMockRule rule = new PowerMockRule()
  def static resourcePath

  def setupSpec() {
    resourcePath = Paths.get("target/test-classes/conditional.singleton.properties");
    Files.createFile(resourcePath);
  }
  def cleanupSpec() {
    if (Files.exists(resourcePath)){
      FileUtils.deleteQuietly(resourcePath.toFile());
    }
  }

  def "test matching inner method call" (paramValue) {
    given:
      spyOnConditionalSingletonMakingItAvailable()
    and:
      mockAllGroovyConditionConstructors()

      def legacyCodeSpy = newSpiedLegacyCodeWithId()
    when:
      legacyCodeSpy.doLegacyOperation(paramValue)
    then:
      verifyPrivate(legacyCodeSpy, times(1)).invoke("matchingOperation")
      verifyPrivate(legacyCodeSpy, times(0)).invoke("unMatchedOperation")
    where:
      paramValue << ["matching", "also matching"]
  }

  def "test unmatched inner method call" (paramValue) {
    given:
      spyOnConditionalSingletonMakingItAvailable()
    and:
      mockAllGroovyConditionConstructors()

      def legacyCodeSpy = newSpiedLegacyCodeWithId()
    when:
      legacyCodeSpy.doLegacyOperation(paramValue)
    then:
      verifyPrivate(legacyCodeSpy, times(0)).invoke("matchingOperation")
      verifyPrivate(legacyCodeSpy, times(1)).invoke("unMatchedOperation")
    where:
      paramValue << ["unmatched", "not a match"]
  }

  private void mockAllGroovyConditionConstructors() {
    def mocked = Mock(GroovyCondition)
    mocked.isAllowed() >> true

    whenNew(GroovyCondition.class).withAnyArguments().thenReturn(mocked)
  }

  private void expandoMockAllGroovyConditionConstructors() {
    def expandoMetaClass = new ExpandoMetaClass(GroovyCondition, true)
    expandoMetaClass.isAllowed = { -> true }
    expandoMetaClass.initialize()
    final GroovyCondition groovyCondition = new GroovyCondition()
    groovyCondition.metaClass = expandoMetaClass

    whenNew(GroovyCondition.class).withAnyArguments().thenReturn(groovyCondition)
  }

  private static LegacyCode newSpiedLegacyCodeWithId() throws Exception {
    final idProvider = mock(LegacyIdProvider.class)
    when(idProvider.getId()).thenReturn("12345")
    whenNew(LegacyIdProvider.class).withAnyArguments().thenReturn(idProvider)

    return spy(new LegacyCode())
  }

  private static spyOnConditionalSingletonMakingItAvailable() {
    final spiedConditional = spy(ConditionalSingleton.getInstance())
    doReturn(true).when(spiedConditional).isAvailable()

    mockStatic(ConditionalSingleton.class)
    doReturn(spiedConditional).when(ConditionalSingleton, "getInstance")
  }
}