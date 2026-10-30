package ua.edu.nupp.kpp.server;

public class Exhibit {
    private Long id;
    private String inventoryCode;
    private String name;
    private String material;
    private String hall;
    private double materialSensitivity;
    private double exhibitionSignificance;

    public Exhibit() {
    }
    public Exhibit(Long id,
                   String inventoryCode,
                   String name,
                   String material,
                   String hall,
                   double materialSensitivity,
                   double exhibitionSignificance) {
        this.id = id;
        this.inventoryCode = inventoryCode;
        this.name = name;
        this.material = material;
        this.hall = hall;
        this.materialSensitivity = materialSensitivity;
        this.exhibitionSignificance = exhibitionSignificance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInventoryCode() {
        return inventoryCode;
    }

    public void setInventoryCode(String inventoryCode) {
        this.inventoryCode = inventoryCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getHall() {
        return hall;
    }

    public void setHall(String hall) {
        this.hall = hall;
    }

    public double getMaterialSensitivity() {
        return materialSensitivity;
    }

    public void setMaterialSensitivity(double materialSensitivity) {
        this.materialSensitivity = materialSensitivity;
    }

    public double getExhibitionSignificance() {
        return exhibitionSignificance;
    }

    public void setExhibitionSignificance(double exhibitionSignificance) {
        this.exhibitionSignificance = exhibitionSignificance;
    }
}