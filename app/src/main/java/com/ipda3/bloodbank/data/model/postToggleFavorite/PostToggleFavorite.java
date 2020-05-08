
package com.ipda3.bloodbank.data.model.postToggleFavorite;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostToggleFavorite {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private PostToggleFavoriteData data;

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

    public PostToggleFavoriteData getData() {
        return data;
    }

    public void setData(PostToggleFavoriteData data) {
        this.data = data;
    }

}
