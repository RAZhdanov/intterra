package com.company;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class User {
    private String userName;
    private Set<String> emailCollection;

    public User() {
    }

    public User(String [] values) {
        userName = values[0];
        emailCollection = new HashSet<>(Arrays.asList(values[1].replaceAll("\\s+","").split(",")));
    }

    public User(String userName, Set<String> emailCollection) {
        this.userName = userName;
        this.emailCollection = emailCollection;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Set<String> getEmailCollection() {
        return emailCollection;
    }

    public void setEmailCollection(Set<String> emailCollection) {
        this.emailCollection = emailCollection;
    }


    @Override
    public String toString() {
        return "User{" + "userName=" + userName + ", emailCollection=" + emailCollection + '}';
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + userName.hashCode();
        result = prime * result + emailCollection.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        if (!Objects.equals(this.userName, other.userName)) {
            return false;
        }
        if (!Objects.equals(this.emailCollection, other.emailCollection)) {
            return false;
        }
        return true;
    }
}
