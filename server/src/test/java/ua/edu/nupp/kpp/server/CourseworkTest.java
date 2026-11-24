package ua.edu.nupp.kpp.server;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CourseworkTest {
    @Test void riskForDemoDataIs7925() { assertEquals(79.25, RiskCalculator.calculate(85, 65, 90), 0.001); }
    @Test void riskForSecondDemoDataIs6650() { assertEquals(66.5, RiskCalculator.calculate(90, 30, 80), 0.001); }
    @Test void riskForThirdDemoDataIs39() { assertEquals(39.0, RiskCalculator.calculate(45, 10, 70), 0.001); }
    @Test void riskRejectsFactorAbove100() { assertThrows(IllegalArgumentException.class, () -> RiskCalculator.calculate(101, 10, 10)); }
    @Test void newToAssessedAllowed() { assertTrue(ConservationRequestService.isAllowedTransition(ConservationRequestState.NEW, ConservationRequestState.ASSESSED)); }
    @Test void assessedToWorksAllowed() { assertTrue(ConservationRequestService.isAllowedTransition(ConservationRequestState.ASSESSED, ConservationRequestState.WORKS)); }
    @Test void worksToCompletedAllowed() { assertTrue(ConservationRequestService.isAllowedTransition(ConservationRequestState.WORKS, ConservationRequestState.COMPLETED)); }
    @Test void newToCompletedRejected() { assertFalse(ConservationRequestService.isAllowedTransition(ConservationRequestState.NEW, ConservationRequestState.COMPLETED)); }
    @Test void exhibitValidationWorks() { assertThrows(IllegalArgumentException.class, () -> new ExhibitService().add(new Exhibit(null, "", "Гобелен", "Текстиль", "Зала історії", 50, 50))); }
    @Test void inspectionValidationWorks() { assertThrows(IllegalArgumentException.class, () -> new InspectionService().add(new Inspection(null, 1, "2026-10-05", 120, "Тест"))); }
}