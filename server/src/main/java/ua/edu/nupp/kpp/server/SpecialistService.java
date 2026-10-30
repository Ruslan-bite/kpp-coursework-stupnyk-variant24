package ua.edu.nupp.kpp.server;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class SpecialistService {
    private static final Logger LOG =
            Logger.getLogger(SpecialistService.class.getName());

    private final SpecialistDAO dao = new SpecialistDAO();

    public List<Specialist> getAll() throws SQLException {
        return dao.getAll();
    }

    public void add(Specialist s) throws SQLException {
        validate(s);
        dao.add(s);
        LOG.info("Додано фахівця");
    }

    public void update(Specialist s) throws SQLException {
        validate(s);
        dao.update(s);
        LOG.info("Оновлено фахівця");
    }

    public void delete(long id) throws SQLException {
        dao.delete(id);
        LOG.info("Видалено фахівця");
    }

    private void validate(Specialist s) {
        if (s == null || blank(s.getFullName()) || blank(s.getPosition()) || blank(s.getSpecialization())) {
            throw new IllegalArgumentException("Усі поля фахівця є обов'язковими");
        }
    }

    private static boolean blank(String v) {
        return v == null || v.isBlank();
    }
}