package com.essoapps.vince.corgiwallpapertwo;

public class GalleryItem {
    private String url;
    private String desc;
    private Boolean active;


    public GalleryItem(String url, String desc, Boolean active) {
        this.url = url;
        this.desc = desc;
        this.active = active;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDesc() {
        return desc;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
