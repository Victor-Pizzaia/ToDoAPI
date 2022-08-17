package org.pizzaia.todo.model;

public enum Status {
    TODO(0),
    INPROGRESS(1),
    COMPLETED(2),
    CANCELED(3);

    private final int id;

    Status(final int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
}
