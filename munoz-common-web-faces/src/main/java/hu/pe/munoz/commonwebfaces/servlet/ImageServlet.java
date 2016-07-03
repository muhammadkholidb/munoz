package hu.pe.munoz.commonwebfaces.servlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hu.pe.munoz.commonwebfaces.helper.WebAppHelper;

@WebServlet("/resources.image/*")
public class ImageServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Properties applicationProperties = WebAppHelper.getApplicationProperties(Thread.currentThread().getContextClassLoader());
		String imageDir = applicationProperties.getProperty("directory.Images");
		String filename = req.getPathInfo().substring(1);
		File file = new File(imageDir, filename);
		resp.setHeader("Content-Type", getServletContext().getMimeType(filename));
		resp.setHeader("Content-Length", String.valueOf(file.length()));
		resp.setHeader("Content-Disposition", "inline; filename=\"" + filename + "\"");
		Files.copy(file.toPath(), resp.getOutputStream());
	}

}
