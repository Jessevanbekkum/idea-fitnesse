package fitnesse.idea.fixturemethod

import com.intellij.openapi.application.QueryExecutorBase
import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.searches.MethodReferencesSearch
import com.intellij.psi.{PsiMethod, PsiReference}
import com.intellij.util.Processor
import fitnesse.idea.lang.filetype.FitnesseFileType

import scala.collection.JavaConversions._

class FixtureMethodReferencesSearch extends QueryExecutorBase[PsiReference, MethodReferencesSearch.SearchParameters](true) {

  def processQuery(p: MethodReferencesSearch.SearchParameters, consumer: Processor[PsiReference]) {
    p.getMethod match {
      case method: PsiMethod =>
        p.getEffectiveSearchScope match {
          case scope: GlobalSearchScope =>
            val restrictedScope = GlobalSearchScope.getScopeRestrictedByFileTypes(scope, FitnesseFileType.INSTANCE)
            find(method.getName, method.getProject, restrictedScope)
              .map(methodReferences => consumer.process(new MethodReference(method, methodReferences)))
          case _ =>
        }
      case _ =>
    }
  }

  def find(key: String, project: Project, scope: GlobalSearchScope): List[MethodReferences] = {
    val references: List[MethodReferences] = FixtureMethodIndex.INSTANCE.get(key, project, scope).toList

    // TODO: Take into account the class name (via Stub structure?)
    return references
  }

}
