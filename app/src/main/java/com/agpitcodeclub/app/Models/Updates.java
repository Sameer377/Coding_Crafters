package com.agpitcodeclub.app.Models;

public class Updates {
    public String appurl;
    public float version;

    public float getVersion() {
        return version;
    }

    public Updates(String appurl, float version) {
        this.appurl = appurl;
        this.version = version;
    }

    public Updates() {
    }

    public String getAppurl() {
        return appurl;
    }

    public Updates(String appurl) {
        this.appurl = appurl;
    }
}
