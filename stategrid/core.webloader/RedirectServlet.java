/*******************************************************************************
 * Copyright (c) 2009 EclipseSource and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   EclipseSource - initial API and implementation
 ******************************************************************************/
package core.webloader;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.*;


/**
 * This servlet is registered for alias "/" and redirects all requests to the
 * <code>examples</code> servlet.
 */
public class RedirectServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  private static final String REDIRECT_URL = "webloader";

  protected void doGet( final HttpServletRequest req,
                        final HttpServletResponse resp )
    throws ServletException, IOException
  {
    redirect( req, resp );
  }

  protected void doPost( final HttpServletRequest req,
                         final HttpServletResponse resp )
    throws ServletException, IOException
  {
    redirect( req, resp );
  }

  private static void redirect( final HttpServletRequest request, 
                                final HttpServletResponse response )
    throws IOException
  {
    if( request.getPathInfo().equals( "/" ) || request.getPathInfo().equals( "/" + REDIRECT_URL) ) {
      response.sendRedirect( response.encodeRedirectURL( REDIRECT_URL ) );
    } else {
      response.sendError( HttpServletResponse.SC_NOT_FOUND );
    }
  }
}
