
package com.ipda3.bloodbank.data.model.editProfile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ipda3.bloodbank.data.model.client.Client;

public class EditProfileData {

    @SerializedName("client")
    @Expose
    private Client client;

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

}
