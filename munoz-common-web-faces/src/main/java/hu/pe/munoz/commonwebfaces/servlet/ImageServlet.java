package hu.pe.munoz.commonwebfaces.servlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/resources.image/*")
public class ImageServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(ImageServlet.class);

    protected ResourceBundle applicationBundle = ResourceBundle.getBundle("application");
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String imagesDir = applicationBundle.getString("directory.path.Images");
            String filename = req.getPathInfo().substring(1);
            
            File file = new File(imagesDir, filename);
            resp.setHeader("Content-Type", getServletContext().getMimeType(filename));
            resp.setHeader("Content-Length", String.valueOf(file.length()));
            resp.setHeader("Content-Disposition", "inline; filename=\"" + filename + "\"");
            Files.copy(file.toPath(), resp.getOutputStream());
        } catch (Exception e) {
            LOG.error(e.toString(), e);
        }
    }

}
