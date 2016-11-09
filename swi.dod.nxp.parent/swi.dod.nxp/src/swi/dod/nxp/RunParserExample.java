package swi.dod.nxp;

import org.eclipse.emf.ecore.EObject;

public class RunParserExample {

	public static void main(String[] args) {
		EObject result = new NXScriptStandAloneParser().parse("Engine forward 90%." +"Navigate left 12,3."+"Run 10,1 ms.");
		System.out.print(result);
	}

}
