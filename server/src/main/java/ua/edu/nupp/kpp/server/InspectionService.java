package ua.edu.nupp.kpp.server;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.logging.Logger;

public class InspectionService {
    private static final Logger LOG =
            Logger.getLogger(InspectionService.class.getName());

    private final InspectionDAO dao = new InspectionDAO();
    private final ExhibitDAO exhibitDAO = new ExhibitDAO();

    public List<Inspection> getAll() throws SQLException {
        return dao.getAll();
    }

    public Inspection getById(long id) throws SQLException {
        return dao.getById(id);
    }

    public void add(Inspection i) throws SQLException {
        validate(i);
        requireExhibit(i.getExhibitId());
        dao.add(i);
        LOG.info("Додано огляд");
    }

    public void update(Inspection i) throws SQLException {
        validate(i);
        requireExhibit(i.getExhibitId());
        dao.update(i);
        LOG.info("Оновлено огляд");
    }

    public void delete(long id) throws SQLException {
        dao.delete(id);
        LOG.info("Видалено огляд");
    }

    private void validate(Inspection i) {
        if (i == null || i.getExhibitId() <= 0 || !validDate(i.getInspectionDate())) {
            throw new IllegalArgumentException("Некоректні основні дані огляду");
        }
        if (i.getDamageSeverity() < 0 || i.getDamageSeverity() > 100) {
            throw new IllegalArgumentException("Тяжкість пошкодження (F2) має бути в діапазоні 0..100");
        }
    }

    private boolean validDate(String value) {
        if (value == null || value.isBlank()) {
            return false;
        }
        try {
            LocalDate.parse(value);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private void requireExhibit(long id) throws SQLException {
        if (exhibitDAO.getById(id) == null) {
            throw new IllegalArgumentException("Експонат не знайдено");
        }
    }
}