package com.example.dbkpandroidapp.Model;

import java.util.List;

public class UsersList {
    private List<UserModel> usersList;
    private String status;

    public List<UserModel> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<UserModel> usersList) {
        this.usersList = usersList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
