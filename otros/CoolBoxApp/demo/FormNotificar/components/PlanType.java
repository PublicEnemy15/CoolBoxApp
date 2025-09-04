package CoolBoxApp.demo.FormNotificar.components;


public class PlanType {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSizeDescription() {
        return sizeDescription;
    }

    public void setSizeDescription(String sizeDescription) {
        this.sizeDescription = sizeDescription;
    }





    public PlanType(String name, String region, String description, String size, String sizeDescription, double monthPrice) {
        this.name = name;
        this.region = region;
        this.description = description;
        this.size = size;
        this.sizeDescription = sizeDescription;
    }

    private String name;
    private String region;
    private String description;
    private String size;
    private String sizeDescription;
}