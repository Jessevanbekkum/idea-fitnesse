package fitnesse.idea.lang.psi

import com.intellij.lang.annotation.{AnnotationHolder, Annotator}
import com.intellij.psi.PsiElement
import fitnesse.idea.fixtureclass.FixtureClass
import fitnesse.idea.fixturemethod.MethodReferences

class MissingReferencesAnnotator extends Annotator {

  def annotate(element: PsiElement, holder: AnnotationHolder): Unit = {
    element match {
      case fixtureClass: FixtureClass =>
        if (fixtureClass.getReferences.isEmpty) {
          holder.createErrorAnnotation(element.getTextRange, "No fixture class found")
        }
      case methodReferences: MethodReferences =>
        if (methodReferences.getReferences.isEmpty) {
          holder.createErrorAnnotation(element.getTextRange, "No fixture method found")
        }
      case _ =>
    }
  }
}
