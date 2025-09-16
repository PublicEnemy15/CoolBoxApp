package CoolBoxApp.demo.model;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class ModelEmployee {

    private String date;
    private double salary;
    private String position;
    private String email;
    private String telefono; 
    private ModelProfile profile;

    // Constructor
    public ModelEmployee(String date, double salary, String position, String email, String telefono, ModelProfile profile) {
        this.date = date;
        this.salary = salary;
        this.position = position;
        this.email = email;
        this.telefono = telefono;
        this.profile = profile;
    }

    // Getters y Setters
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public ModelProfile getProfile() {
        return profile;
    }

    public void setProfile(ModelProfile profile) {
        this.profile = profile;
    }

    // Métodos para la tabla
    public Object[] toTableRowBasic(int row) {
        NumberFormat nf = new DecimalFormat("$ #,##0.##");
        return new Object[]{
                row,
                profile.getName(),
                profile.getLocation(),
                date,
                nf.format(salary),
                position,
                email,
                telefono  
        };
    }

    public Object[] toTableRowCustom(int row) {
        NumberFormat nf = new DecimalFormat("$ #,##0.##");
        return new Object[]{
                false,
                row,
                profile,
                date,
                nf.format(salary),
                position,
                email,
                telefono 
        };
    }
}
