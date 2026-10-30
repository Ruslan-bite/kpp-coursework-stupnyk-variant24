package ua.edu.nupp.kpp.server;

public class ConservationRequest {
    private Long id;
    private long inspectionId;
    private long specialistId;
    private ConservationRequestState state;
    private double f1;
    private double f2;
    private double f3;
    private double riskIndex;
    private String description;

    public ConservationRequest() {
    }

    public ConservationRequest(Long id,
                               long inspectionId,
                               long specialistId,
                               ConservationRequestState state,
                               double f1,
                               double f2,
                               double f3,
                               double riskIndex,
                               String description) {
        this.id = id;
        this.inspectionId = inspectionId;
        this.specialistId = specialistId;
        this.state = state;
        this.f1 = f1;
        this.f2 = f2;
        this.f3 = f3;
        this.riskIndex = riskIndex;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getInspectionId() {
        return inspectionId;
    }

    public void setInspectionId(long inspectionId) {
        this.inspectionId = inspectionId;
    }

    public long getSpecialistId() {
        return specialistId;
    }

    public void setSpecialistId(long specialistId) {
        this.specialistId = specialistId;
    }

    public ConservationRequestState getState() {
        return state;
    }

    public void setState(ConservationRequestState state) {
        this.state = state;
    }

    public double getF1() {
        return f1;
    }

    public void setF1(double f1) {
        this.f1 = f1;
    }

    public double getF2() {
        return f2;
    }

    public void setF2(double f2) {
        this.f2 = f2;
    }

    public double getF3() {
        return f3;
    }

    public void setF3(double f3) {
        this.f3 = f3;
    }

    public double getRiskIndex() {
        return riskIndex;
    }

    public void setRiskIndex(double riskIndex) {
        this.riskIndex = riskIndex;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

