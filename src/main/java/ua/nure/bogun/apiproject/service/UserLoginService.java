package main.java.ua.nure.bogun.apiproject.service;

import main.java.ua.nure.bogun.apiproject.database.DBException;
import main.java.ua.nure.bogun.apiproject.entity.Role;
import main.java.ua.nure.bogun.apiproject.entity.User;
import main.java.ua.nure.bogun.apiproject.service.databaseservice.UserService;

public class UserLoginService implements Login{
    private User cash;

    @Override
    public STATUS logining(String login, String password) throws DBException {

        User client = UserService.getInstance().getUserByLogin(login);
        if(client != null){
            if(Hashing.getHashedString(password).equals(client.getPassword())){
                cash = client;
                return STATUS.TRUE;
            }else{
                return STATUS.FALSE;
            }
        }else{
            return STATUS.UNDEFINED;
        }
    }

    public User getCash(){
        return cash;
    }
}
