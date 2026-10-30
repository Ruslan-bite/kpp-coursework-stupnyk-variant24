package ua.edu.nupp.kpp.server;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class ConservationRequestService {
    private static final Logger LOG =
            Logger.getLogger(ConservationRequestService.class.getName());

    private final ConservationRequestDAO dao = new ConservationRequestDAO();
    private final InspectionDAO inspectionDAO = new InspectionDAO();
    private final SpecialistDAO specialistDAO = new SpecialistDAO();
    private final ExhibitDAO exhibitDAO = new ExhibitDAO();

    public List<ConservationRequest> getAll() throws SQLException {
        return dao.getAll();
    }

    public void add(long inspectionId, long specialistId, String description) throws SQLException {
        ConservationRequest request = fromInspection(null, inspectionId, specialistId, ConservationRequestState.NEW, description);
        dao.add(request);
        LOG.info("Створено заявку на консервацію");
    }

    public void update(long id, long inspectionId, long specialistId, String description) throws SQLException {
        ConservationRequest existing = dao.getById(id);
        if (existing == null) {
            throw new IllegalArgumentException("Заявку не знайдено");
        }
        ConservationRequest request = fromInspection(id, inspectionId, specialistId, existing.getState(), description);
        dao.update(request);
        LOG.info("Оновлено заявку на консервацію");
    }

    public void delete(long id) throws SQLException {
        dao.delete(id);
        LOG.info("Видалено заявку на консервацію");
    }

    public static boolean isAllowedTransition(ConservationRequestState from, ConservationRequestState to) {
        return (from == ConservationRequestState.NEW && to == ConservationRequestState.ASSESSED)
                || (from == ConservationRequestState.ASSESSED && to == ConservationRequestState.WORKS)
                || (from == ConservationRequestState.WORKS && to == ConservationRequestState.COMPLETED);
    }

    public void changeState(long id, ConservationRequestState next) throws SQLException {
        ConservationRequest current = dao.getById(id);
        if (current == null) {
            throw new IllegalArgumentException("Заявку не знайдено");
        }
        if (!isAllowedTransition(current.getState(), next)) {
            LOG.warning("Недопустимий перехід стану: "
                    + current.getState() + " -> " + next);
            throw new IllegalArgumentException("Недопустимий перехід стану");
        }
        dao.updateState(id, next);
        LOG.info("Змінено стан: " + current.getState() + " -> " + next);
    }

    private ConservationRequest fromInspection(Long id, long inspectionId, long specialistId, ConservationRequestState state, String description) throws SQLException {
        Inspection inspection = inspectionDAO.getById(inspectionId);
        if (inspection == null) {
            throw new IllegalArgumentException("Огляд не знайдено");
        }
        Exhibit exhibit = exhibitDAO.getById(inspection.getExhibitId());
        if (exhibit == null) {
            throw new IllegalArgumentException("Експонат не знайдено");
        }
        if (specialistDAO.getById(specialistId) == null) {
            throw new IllegalArgumentException("Фахівця не знайдено");
        }

        double f1 = exhibit.getMaterialSensitivity();
        double f2 = inspection.getDamageSeverity();
        double f3 = exhibit.getExhibitionSignificance();
        double riskIndex = RiskCalculator.calculate(f1, f2, f3);

        return new ConservationRequest(id, inspectionId, specialistId,
                state, f1, f2, f3, riskIndex, description);
    }
}