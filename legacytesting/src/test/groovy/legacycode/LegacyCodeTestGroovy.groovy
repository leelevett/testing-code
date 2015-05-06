package legacycode

import groovy.sql.GroovyRowResult
import org.apache.commons.io.FileUtils
import org.junit.Rule
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.rule.PowerMockRule
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Paths

import static org.mockito.internal.verification.VerificationModeFactory.times
import static org.powermock.api.mockito.PowerMockito.doReturn
import static org.powermock.api.mockito.PowerMockito.mock
import static org.powermock.api.mockito.PowerMockito.mockStatic
import static org.powermock.api.mockito.PowerMockito.spy
import static org.powermock.api.mockito.PowerMockito.verifyPrivate
import static org.powermock.api.mockito.PowerMockito.when
import static org.powermock.api.mockito.PowerMockito.whenNew

// 1. All the POM things. 1.6.2, try it at work. Went wibbly at home.
// 2. Rename test class to avoid clash.
//6. Do the powermocking...
@PrepareForTest([ ConditionalSingleton, LegacyIdProvider, LegacyCode, GroovyCondition ])
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

  // 4. copy this lot in.
  def "test matching inner method call" (paramValue) {
    given:
      spyOnConditionalSingletonMakingItAvailable()
    and:
      // 13. Change to expando to make a point.
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
    // 9 Good lord that was hard work. Mock results THEN spy constructor. Not certain as to why.
    def mocked = Mock(GroovyCondition)
    mocked.isAllowed() >> true
    // 10, This one works for in the Groovy test... Might do an uncomment this section to prove it works bit?
    GroovySpy(GroovyCondition, global: true) // 8 consider the args for constructor here. GroovyMock - JAVA Or things break, by not working.
    new GroovyCondition() >> mocked

    // 10. This one works for in the Java Powermocked class! Yikes.
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

  // 3 copy this lot in,
  private static LegacyCode newSpiedLegacyCodeWithId() throws Exception {
    final idProvider = mock(LegacyIdProvider.class)
    when(idProvider.getId()).thenReturn("12345")
    whenNew(LegacyIdProvider.class).withAnyArguments().thenReturn(idProvider)

    return spy(new LegacyCode())
  }

  // 6. So it seems this can't be done in setup? scoping issue of some sort.
  private static spyOnConditionalSingletonMakingItAvailable() {
    final spiedConditional = spy(ConditionalSingleton.getInstance())
    doReturn(true).when(spiedConditional).isAvailable()

    mockStatic(ConditionalSingleton.class)
    doReturn(spiedConditional).when(ConditionalSingleton, "getInstance")
  }
}