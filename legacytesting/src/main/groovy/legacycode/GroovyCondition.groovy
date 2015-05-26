package legacycode

class GroovyCondition {
  def boolean allowed

  def boolean isAllowed() {
    metaClass.getMetaMethod("isAllowed").invoke(this)
  }
}