package sumdu.edu.odz.model;

public class Cat {
    private int id;
    private String breedName;
    private String description;

    public Cat(int id, String breedName, String description) {
        this.id = id;
        this.breedName = breedName;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getBreedName() {
        return breedName;
    }

    public void setBreedName(String breedName) {
        this.breedName = breedName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
