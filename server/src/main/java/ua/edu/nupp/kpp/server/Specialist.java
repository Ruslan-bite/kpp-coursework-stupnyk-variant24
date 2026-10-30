package ua.edu.nupp.kpp.server;

public class Specialist {
    private Long id;
    private String fullName;
    private String position;
    private String specialization;

    public Specialist() {
    }

    public Specialist(Long id,
                      String fullName,
                      String position,
                      String specialization) {
        this.id = id;
        this.fullName = fullName;
        this.position = position;
        this.specialization = specialization;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}