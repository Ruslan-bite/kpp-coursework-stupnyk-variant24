package ua.edu.nupp.kpp.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SpecialistServlet extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(SpecialistServlet.class.getName());
    private final SpecialistService service = new SpecialistService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        try {
            writeJson(resp, service.getAll());
        } catch (Exception e) {
            writeError(resp, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        req.setCharacterEncoding("UTF-8");
        try {
            String action = req.getParameter("action");
            if ("add".equals(action)) {
                service.add(readSpecialist(req, null));
            } else if ("update".equals(action)) {
                service.update(readSpecialist(req, id(req)));
            } else if ("delete".equals(action)) {
                service.delete(id(req));
            } else {
                throw new IllegalArgumentException("Невідома операція");
            }
            writeJson(resp, service.getAll());
        } catch (Exception e) {
            writeError(resp, e);
        }
    }

    private Specialist readSpecialist(HttpServletRequest req, Long id) {
        return new Specialist(id,
                req.getParameter("fullName"),
                req.getParameter("position"),
                req.getParameter("specialization"));
    }

    private void writeJson(HttpServletResponse resp, Object data)
            throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        mapper.writeValue(resp.getWriter(), data);
    }

    private void writeError(HttpServletResponse resp, Exception e)
            throws IOException {
        int status = HttpServletResponse.SC_BAD_REQUEST;
        String message;
        if (e instanceof NumberFormatException) {
            message = "Некоректне числове значення";
        } else if (e instanceof IllegalArgumentException) {
            message = e.getMessage() == null ? "Помилка" : e.getMessage();
        } else {
            LOG.log(Level.SEVERE, "Помилка сервера", e);
            status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            message = "Внутрішня помилка сервера";
        }
        resp.setStatus(status);
        writeJson(resp, Map.of("error", message));
    }

    private long id(HttpServletRequest req) {
        return Long.parseLong(req.getParameter("id"));
    }
}