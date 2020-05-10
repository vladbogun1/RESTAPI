package main.java.ua.nure.bogun.apiproject.entity;


import java.util.Arrays;

public enum Role {

    ADMIN, CLIENT, MODERATOR;

    public static Role getUserRole(User user) {
        int roleId = user.getRoleId();
        return Role.values()[roleId];
    }
    public static int getRoleId(Role role){ return Arrays.binarySearch(Role.values(),role);}
    public static Role getRole(int id){
        return Role.values()[id];
    }
    public String getName() {
        return name().toLowerCase();
    }
    public static boolean checkRole(User user, Role expected) {
        return getRole(user.getRoleId()) == expected;
    }


}
