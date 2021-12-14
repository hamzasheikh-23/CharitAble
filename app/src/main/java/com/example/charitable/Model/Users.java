package com.example.charitable.Model;

public class Users {
    private String Name, Username, Password;

    public Users(){

    }

    public Users(String Name, String Username, String Password) {
        this.Name = Name;
        this.Username = Username;
        this.Password = Password;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }
}
