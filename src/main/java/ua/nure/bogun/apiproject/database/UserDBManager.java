package main.java.ua.nure.bogun.apiproject.database;

import main.java.ua.nure.bogun.apiproject.entity.Role;
import main.java.ua.nure.bogun.apiproject.entity.User;
import main.java.ua.nure.bogun.apiproject.service.Hashing;
import main.java.ua.nure.bogun.apiproject.service.ImagesGenerator;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.DriverManager.getConnection;

public class UserDBManager extends DBManager{
    private static final String SELECT_USER_BY_LOGIN = "SELECT * FROM" +
            " user  JOIN role ON user.role_id = role.role_id WHERE user.login=?";
    private static final String SELECT_USER_BY_ID = "SELECT * FROM" +
            " user  JOIN role ON user.role_id = role.role_id WHERE user.user_id=?";
    private static final String SELECT_ADMIN_BY_ID = "SELECT * FROM" +
            " user  JOIN role ON user.role_id = role.role_id WHERE role.role_name='admin' AND user.user_id=?";
    private static final String SELECT_CLIENT_BY_ID = "SELECT * FROM" +
            " user  JOIN role ON user.role_id = role.role_id WHERE role.role_name='client' AND user.user_id=?";
    private static final String SELECT_MODERATOR_BY_ID = "SELECT * FROM" +
            " user  JOIN role ON user.role_id = role.role_id WHERE role.role_name='moderator' AND user.user_id=?";
    private static final String SELECT_ALL_USERS = "SELECT * FROM user";
    private static final String SELECT_ALL_ADMIS = "SELECT * FROM " +
            "user JOIN role ON user.role_id = role.role_id WHERE role.role_name = 'admin'";
    private static final String SELECT_ALL_CLIENTS = "SELECT * FROM " +
            "user JOIN role ON user.role_id = role.role_id WHERE role.role_name = 'client'";
    private static final String SELECT_ALL_MODERATORS = "SELECT * FROM " +
            "user JOIN role ON user.role_id = role.role_id WHERE role.role_name = 'moderator'";
    private static final String UPDATE_PASSWORD = "UPDATE user SET password = ? WHERE user_id = ?";
    private static final String UPDATE = "UPDATE user SET first_name = ?, last_name=?, " +
            "login=?, picture=?, email=? WHERE user_id = ?";
    private static final String DELETE = "DELETE FROM user WHERE user_id = ?";
    private static final String INSERT_USER = "INSERT INTO " +
            "user (`login`, `password`, `first_name`," +
            " `last_name`, `role_id`, `picture`, `email`) VALUES " +
            "(?,?,?,?,?,?,?)";




    private static UserDBManager instance;
    private static final Logger logger = Logger.getLogger(UserDBManager.class);

    public static UserDBManager getInstance() {
        if (instance == null) {
            instance = new UserDBManager();
        }
        return instance;
    }
    private User getUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("user_id"));
        user.setLogin(rs.getString("login"));
        user.setPassword(rs.getString("password"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setRoleId(rs.getInt("role_id"));
        user.setEmail(rs.getString("email"));
        user.setImage(
                rs.getString("picture")
                //ImagesGenerator.generate()
        );

        return  user;
    }

    PreparedStatement getFindUserPrepareStatement(Connection con, Role role) throws SQLException {
        if(role==null){
            return con.prepareStatement(SELECT_USER_BY_ID);
        }
        switch (role){
            case ADMIN:
                return con.prepareStatement(SELECT_ADMIN_BY_ID);
            case CLIENT:
                return con.prepareStatement(SELECT_CLIENT_BY_ID);
            case MODERATOR:
                return con.prepareStatement(SELECT_MODERATOR_BY_ID);
            default:
                throw new SQLException("PSMT for "+ role + "is not set.");
        }
    }
    public User findUserById(Long id, Role role) {
        User user = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = getConnection(CONNECTION_URL);
            pstmt = getFindUserPrepareStatement(con,role);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                user = getUser(rs);
            }
        } catch (SQLException ex) {
            rollback(con);
            logger.error("Cannot obtain a user by its login", ex);
        } finally {
            close(rs);
            close(pstmt);
            commitAndClose(con);
        }
        return user;
    }



    public User findUserByLogin(String login) {
        User user = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = getConnection(CONNECTION_URL);

            pstmt = con.prepareStatement(SELECT_USER_BY_LOGIN);
            pstmt.setString(1, login);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                user = getUser(rs);
            }
        } catch (SQLException ex) {
            rollback(con);
            logger.error("Cannot obtain a user by its login", ex);
        } finally {
            close(rs);
            close(pstmt);
            commitAndClose(con);
        }
        return user;
    }



    ResultSet getFindAllPrepareStatement(Statement statement, Role role) throws SQLException {
        if(role==null){
            return statement.executeQuery(SELECT_ALL_USERS);
        }
        switch (role){
            case ADMIN:
                return statement.executeQuery(SELECT_ALL_ADMIS);
            case CLIENT:
                return statement.executeQuery(SELECT_ALL_CLIENTS);
            case MODERATOR:
                return statement.executeQuery(SELECT_ALL_MODERATORS);
            default:
                throw new SQLException("PSMT for "+ role + "is not set.");
        }
    }
    public List<User> findAllUsers(Role role) throws DBException {
        Connection connection = null;
        Statement statement = null;
        ResultSet set = null;
        List<User> list = new ArrayList<>();

        try {
            connection = getConnection(CONNECTION_URL);
            statement = connection.createStatement();
            set = getFindAllPrepareStatement(statement,role);

            while (set.next()) {
                User user = getUser(set);
                list.add(user);
            }
        } catch (SQLException e) {
            logger.error("Error in selecting clients", e);
            throw new DBException("Error in selecting clients", e);
        } finally {
            close(connection);
            close(statement);
            close(set);
        }

        return list;
    }







    public boolean updatePassword(
            String oldPsw,
            String newPsw,
            Long id
    ) throws DBException {
        boolean result = false;
        if (Hashing.getHashedString(oldPsw).equals(findUserById(id,null).getPassword())) {
        Connection connection = null;
        PreparedStatement statement = null;
        result = true;
        try {
            connection = getConnection(CONNECTION_URL);

            statement = connection.prepareStatement(UPDATE_PASSWORD);
            statement.setString(1, newPsw);
            statement.setLong(2, id);
            statement.execute();

        } catch (SQLException e) {
            result = false;
            logger.error("Error in updating client password", e);
            throw new DBException("Error in updating client password", e);
        } finally {
            close(connection);
            close(statement);
        }
        }
        return result;
    }


    public boolean update(User user) throws DBException {
        Connection connection = null;
        PreparedStatement statement = null;
        boolean result =  true;
        try {
            connection = getConnection(CONNECTION_URL);

            statement = connection.prepareStatement(UPDATE);
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getLogin());
            statement.setString(4, user.getImage());
            statement.setString(5,user.getEmail());
            statement.setLong(6, user.getId());

            statement.execute();
        } catch (SQLException e) {
            result = false;
            logger.error("Error in updating user", e);
            throw new DBException("Error in updating user", e);
        } finally {
            close(connection);
            close(statement);
        }
        return result;
    }
    public boolean insert(User user) throws DBException {
        Connection connection = null;
        PreparedStatement statement = null;
        boolean status = true;
        try {
            connection = getConnection(CONNECTION_URL);

            statement = connection.prepareStatement(INSERT_USER);
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getFirstName());
            statement.setString(4, user.getLastName());
            statement.setInt(5, user.getRoleId());
            statement.setString(6, user.getImage());
            statement.setString(7, user.getEmail());
            statement.execute();

        } catch (SQLException e) {
            status = false;
            logger.error("Error in creating meeting", e);
            throw new DBException("Error in creating meeting", e);

        } finally {
            close(connection);
            close(statement);
        }
        return status;
    }
    public boolean delete(Long id) throws DBException {
        Connection connection = null;
        PreparedStatement statement = null;
        boolean result = true;
        try {

            connection = getConnection(CONNECTION_URL);
            statement = connection.prepareStatement(DELETE);
            statement.setLong(1, id);
            statement.execute();

        } catch (SQLException e) {
            result = false;
            logger.error("Error in deleting user", e);
            throw new DBException("Error in deleting user", e);
        } finally {
            close(connection);
            close(statement);
        }
        return result;
    }

}
