/**
 * generated by Xtext 2.10.0
 */
package swi.dod.nxp;

import swi.dod.nxp.DslStandaloneSetupGenerated;

/**
 * Initialization support for running Xtext languages without Equinox extension registry.
 */
@SuppressWarnings("all")
public class DslStandaloneSetup extends DslStandaloneSetupGenerated {
  public static void doSetup() {
    DslStandaloneSetup _dslStandaloneSetup = new DslStandaloneSetup();
    _dslStandaloneSetup.createInjectorAndDoEMFRegistration();
  }
}