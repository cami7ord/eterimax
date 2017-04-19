package com.eterimax.pojos;

public class Image {

    private final int farm;
    private final String serverId;
    private final String imageId;
    private final String secret;
    private final String ownerId;
    private final String ownerName;
    private final String date;
    private final String title;
    private final String description;
    private final String location;
    private final String sizeSuffix;

    public static class Builder {
        // Required parameters
        private final int farm;
        private final String serverId;
        private final String imageId;
        private final String secret;
        private final String ownerId;

        // Optional parameters - initialized to default values
        private String ownerName = "";
        private String date = "";
        private String title = "";
        private String description = "";
        private String location = "";
        private String sizeSuffix = "";


        public Builder(int farm, String serverId, String imageId, String secret, String ownerId) {
            this.farm = farm;
            this.serverId = serverId;
            this.imageId = imageId;
            this.secret = secret;
            this.ownerId = ownerId;
        }

        public Builder ownerName(String ownerName)
            { this.ownerName = ownerName;   return this; }

        public Builder date(String date)
            { this.date = date;   return this; }

        public Builder title(String title)
            { this.title = title;   return this; }

        public Builder description(String description)
            { this.description = description;   return this; }

        public Builder location(String location)
            { this.location = location;   return this; }

        public Builder sizeSuffix(String sizeSuffix)
        { this.sizeSuffix = "_" + sizeSuffix;   return this; }

        public Image build() {
            return new Image(this);
        }
    }

    private Image(Builder builder) {
        farm = builder.farm;
        serverId = builder.serverId;
        imageId = builder.imageId;
        secret = builder.secret;
        ownerId = builder.ownerId;
        ownerName = builder.ownerName;
        date = builder.date;
        title = builder.title;
        description = builder.description;
        location = builder.location;
        sizeSuffix = builder.sizeSuffix;
    }

    public int getFarm() {
        return farm;
    }

    public String getServerId() {
        return serverId;
    }

    public String getImageId() {
        return imageId;
    }

    public String getSecret() {
        return secret;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getSizeSuffix() {
        return sizeSuffix;
    }

    @Override
    public String toString() {

        //https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}_[mstzb].jpg
        return "https://farm" + farm + ".staticflickr.com/" + serverId + "/"
                + imageId + "_" + secret + sizeSuffix + ".jpg";
    }
}
