package main.java.ua.nure.bogun.apiproject.service;


import main.java.ua.nure.bogun.apiproject.database.DBException;
import main.java.ua.nure.bogun.apiproject.entity.Role;

public interface Login {

    STATUS logining(String login, String password) throws DBException;

    enum STATUS{
        TRUE, FALSE, UNDEFINED
    }
}