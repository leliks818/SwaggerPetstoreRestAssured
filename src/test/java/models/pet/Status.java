package models.pet;


public enum Status {
    AVAILABLE("available"),
    PENDING("pending"),
    SOLD("sold");

    private final String status;

    Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
