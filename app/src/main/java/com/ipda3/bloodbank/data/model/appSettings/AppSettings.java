
package com.ipda3.bloodbank.data.model.appSettings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppSettings {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private AppSettingsData data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public AppSettingsData getData() {
        return data;
    }

    public void setData(AppSettingsData data) {
        this.data = data;
    }

}
