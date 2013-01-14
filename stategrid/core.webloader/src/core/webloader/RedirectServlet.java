package core.webloader;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RedirectServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		redirect(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		redirect(req, resp);
	}

	static void redirect(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		if (request.getPathInfo().equals("/")) {
			response.sendRedirect(response.encodeRedirectURL("webloader"));
		} else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}

}
