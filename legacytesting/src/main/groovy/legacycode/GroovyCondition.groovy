package legacycode

class GroovyCondition {
  // 11. def or default generates synthetic methods. isX methods require boolean type.
  def boolean allowed

//  12. Using expandoMetaClass? Java won't find the meta class data. You'll need to fiddle it here to get at it.
  def boolean isAllowed() {
    metaClass.getMetaMethod("isAllowed").invoke(this)
  }
}