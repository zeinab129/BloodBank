
package com.ipda3.bloodbank.data.model.register;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ipda3.bloodbank.data.model.client.Client;

public class RegisterData {

    @SerializedName("api_token")
    @Expose
    private String apiToken;
    @SerializedName("client")
    @Expose
    private Client client;

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

}
