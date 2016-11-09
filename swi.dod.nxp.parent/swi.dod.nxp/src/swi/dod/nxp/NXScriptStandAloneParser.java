/*******************************************************************************
 * Jan Kožusznik
 * Copyright (c) 2014 All Right Reserved, http://www.kozusznik.cz
 *
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 ******************************************************************************/
package swi.dod.nxp;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.util.StringInputStream;

import com.google.inject.Injector;


/**
 * @author Jan Kožusznik
 * @version 0.1
 */
public class NXScriptStandAloneParser {

  private Injector injector;

  /**
   *
   */
  public NXScriptStandAloneParser() {
    injector =
        new DslStandaloneSetupGenerated().createInjectorAndDoEMFRegistration();
  }

  public EObject parse(InputStream is) throws IOException {
    XtextResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);
    resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
    Resource res = resourceSet.createResource(URI.createURI("ghost.nxs"));
    res.load(is, resourceSet.getLoadOptions());
    return res.getContents().get(0);
  }

  public EObject parse(String str) {
    
    try(InputStream is = new StringInputStream(str)) {
      return parse(is);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }
}
