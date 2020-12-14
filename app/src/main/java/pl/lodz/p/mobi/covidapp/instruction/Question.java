package pl.lodz.p.mobi.covidapp.instruction;

public class Question {
    private String imagePath = "";
    private String title;
    private String description;
    private double weight;
    private boolean response = false;

    public String getImagePath() {
        return imagePath;
    }

    public Question setImagePath(String imagePath) {
        this.imagePath = imagePath;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Question setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Question setDescription(String description) {
        this.description = description;
        return this;
    }

    public double getWeight() {
        return weight;
    }

    public Question setWeight(double weight) {
        this.weight = weight;
        return this;
    }

    public boolean isResponse() {
        return response;
    }

    public Question setResponse(boolean response) {
        this.response = response;
        return this;
    }
}
