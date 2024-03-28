package com.myblogbackend.blog.enums;

public enum RatingType {
    LIKE("LIKE"),
    UNLIKE("UNLIKE"),
    DISLIKE("DISLIKE");

    private final String type;

    RatingType(final String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
