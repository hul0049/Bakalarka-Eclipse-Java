/*
 * generated by Xtext 2.10.0
 */
package swi.dod.nxp.validation;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.xtext.validation.AbstractDeclarativeValidator;

public abstract class AbstractDslValidator extends AbstractDeclarativeValidator {
	
	@Override
	protected List<EPackage> getEPackages() {
		List<EPackage> result = new ArrayList<EPackage>();
		result.add(swi.dod.nxp.dsl.DslPackage.eINSTANCE);
		return result;
	}
	
}
