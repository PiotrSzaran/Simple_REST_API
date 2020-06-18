package simple_rest_api.model;

public enum Category {
    IT("IT"), AGD("AGD"), SPORT("SPORT"), BEAUTY("BEAUTY"), MOTO("MOTO");

    private String fullName;

    Category(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }
}
