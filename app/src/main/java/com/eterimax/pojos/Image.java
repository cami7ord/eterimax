package com.eterimax.pojos;

public class Image {

    private final String imageUrl;
    private final String description;

    public static class Builder {
        // Required parameters
        private final String imageUrl;
        // Optional parameters - initialized to default values
        private String description = "";

        public Builder(String url) {
            this.imageUrl = url;
        }

        public Builder description(String description)
            { this.description = description;   return this; }

        public Image build() {
            return new Image(this);
        }
    }

    private Image(Builder builder) {
        imageUrl = builder.imageUrl;
        description = builder.description;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
