package ua.edu.nupp.kpp.server;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class ExhibitService {
    private static final Logger LOG =
            Logger.getLogger(ExhibitService.class.getName());

    private final ExhibitDAO dao = new ExhibitDAO();

    public List<Exhibit> getAll() throws SQLException {
        return dao.getAll();
    }

    public void add(Exhibit e) throws SQLException {
        validate(e);
        dao.add(e);
        LOG.info("Додано експонат");
    }

    public void update(Exhibit e) throws SQLException {
        validate(e);
        dao.update(e);
        LOG.info("Оновлено експонат");
    }

    public void delete(long id) throws SQLException {
        dao.delete(id);
        LOG.info("Видалено експонат");
    }

    private void validate(Exhibit e) {
        if (e == null || blank(e.getInventoryCode()) || blank(e.getName()) || blank(e.getMaterial()) || blank(e.getHall())) {
            throw new IllegalArgumentException("Усі текстові поля експоната є обов'язковими");
        }
        if (outOfRange(e.getMaterialSensitivity()) || outOfRange(e.getExhibitionSignificance())) {
            throw new IllegalArgumentException("Чутливість матеріалу (F1) і значущість експонування (F3) мають бути в діапазоні 0..100");
        }
    }

    private static boolean outOfRange(double v) {
        return v < 0 || v > 100;
    }

    private static boolean blank(String v) {
        return v == null || v.isBlank();
    }
}