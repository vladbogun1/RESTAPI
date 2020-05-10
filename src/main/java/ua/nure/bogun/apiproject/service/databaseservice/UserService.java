package main.java.ua.nure.bogun.apiproject.service.databaseservice;

import main.java.ua.nure.bogun.apiproject.database.DBException;
import main.java.ua.nure.bogun.apiproject.database.UserDBManager;
import main.java.ua.nure.bogun.apiproject.entity.Role;
import main.java.ua.nure.bogun.apiproject.entity.User;
import main.java.ua.nure.bogun.apiproject.service.ImagesGenerator;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private static UserService instance;
    private static final Logger logger = Logger.getLogger(UserService.class);

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }
    public User getUserById(Long id, Role role){
        return UserDBManager.getInstance().findUserById(id,role);
    }
    public User getUserById(Long id){
        return UserDBManager.getInstance().findUserById(id,null);
    }
    public User getUserByLogin(String login){
        return UserDBManager.getInstance().findUserByLogin(login);
    }
    public boolean updateUser(User user){
        try {
            return UserDBManager.getInstance().update(user);
        } catch (DBException e) {
            e.printStackTrace();
            logger.error("Error updateUser: ",e);
            return false;
        }
    }
    public boolean updatePassword(Long userId, String oldPass, String newPass){
        try {
            return UserDBManager.getInstance().updatePassword(oldPass, newPass,userId);
        } catch (DBException e) {
            e.printStackTrace();
            logger.error("Error updatePassword: ",e);
            return false;
        }
    }
    public List<User> getAllUsers(Role role){
        try {
            return UserDBManager.getInstance().findAllUsers(role);
        } catch (DBException e) {
            e.printStackTrace();
            logger.error("Error getAllUsers: ",e);
            return new ArrayList<>();
        }
    }
    public List<User> getAllUsers(){
        try {
            return UserDBManager.getInstance().findAllUsers(null);
        } catch (DBException e) {
            e.printStackTrace();
            logger.error("Error getAllUsers: ",e);
            return new ArrayList<>();
        }
    }
    public boolean createUser(String login, String password, String firstName, String lastName, String roleId, String email){
        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRoleId(Integer.parseInt(roleId));
        user.setEmail(email);
        user.setImage(ImagesGenerator.generate());
        try {
            return UserDBManager.getInstance().insert(user);
        } catch (DBException e) {
            e.printStackTrace();
            logger.error("Error createUser: ",e);
            return false;
        }
    }

    public boolean deleteUser(Long userId) {
        try {
            return UserDBManager.getInstance().delete(userId);
        } catch (DBException e) {
            e.printStackTrace();
            return false;
        }
    }
}
