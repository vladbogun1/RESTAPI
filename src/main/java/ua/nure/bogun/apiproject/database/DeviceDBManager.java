package main.java.ua.nure.bogun.apiproject.database;

import main.java.ua.nure.bogun.apiproject.entity.Device;
import main.java.ua.nure.bogun.apiproject.service.TimeConverter;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.sql.DriverManager.getConnection;

public class DeviceDBManager extends DBManager{
    private static final String SELECT_DEVICE_BY_ID = "SELECT * FROM" +
            " device WHERE device_id=?";
    private static final String SELECT_ALL_DEVICES = "SELECT * FROM device";
    private static final String SELECT_ALL_DEVICES_BY_USER_ID = "SELECT * FROM " +
            "device WHERE user_id=?";
    private static final String UPDATE_DATA = "UPDATE device SET temperature = ?, humidity = ?, last_date_time = ? WHERE device_id = ?";
    private static final String UPDATE_PHONE = "UPDATE device SET phone = ? WHERE device_id = ?";
    private static final String DELETE = "DELETE FROM device WHERE device_id = ?";





    private static DeviceDBManager instance;
    private static final Logger logger = Logger.getLogger(DeviceDBManager.class);

    public static DeviceDBManager getInstance() {
        if (instance == null) {
            instance = new DeviceDBManager();
        }
        return instance;
    }
    private Device getDevice(ResultSet rs) throws SQLException {
        Device device = new Device();
        device.setId(rs.getLong("device_id"));
        device.setPhone(rs.getString("phone"));
        device.setTemperature(rs.getString("temperature"));
        device.setHumidity(rs.getString("humidity"));
        device.setLastDate(TimeConverter.timestampToString(rs.getTimestamp("last_date_time")));
        device.setUserId(rs.getInt("user_id"));
        return  device;
    }


    public Device findDeviceById(Long id) {
        Device device = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = getConnection(CONNECTION_URL);
            pstmt = con.prepareStatement(SELECT_DEVICE_BY_ID);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                device = getDevice(rs);
            }
        } catch (SQLException ex) {
            rollback(con);
            logger.error("Cannot select device by id", ex);
        } finally {
            close(rs);
            close(pstmt);
            commitAndClose(con);
        }
        return device;
    }



    public List<Device> findAllDevices() throws DBException {
        Connection connection = null;
        Statement statement = null;
        ResultSet set = null;
        List<Device> list = new ArrayList<>();

        try {
            connection = getConnection(CONNECTION_URL);
            statement = connection.createStatement();
            set = statement.executeQuery(SELECT_ALL_DEVICES);

            while (set.next()) {
                Device user = getDevice(set);
                list.add(user);
            }
        } catch (SQLException e) {
            logger.error("Error in selecting devices", e);
            throw new DBException("Error in selecting devices", e);
        } finally {
            close(connection);
            close(statement);
            close(set);
        }

        return list;
    }

    public List<Device> findDevicesByUserId(Long userId) throws DBException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        List<Device> list = new ArrayList<>();

        try {
            con = getConnection(CONNECTION_URL);
            pstmt = con.prepareStatement(SELECT_ALL_DEVICES_BY_USER_ID);
            pstmt.setLong(1, userId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Device device = getDevice(rs);
                list.add(device);
            }
        } catch (SQLException e) {
            logger.error("Error in selecting devices", e);
            throw new DBException("Error in selecting devices", e);
        } finally {
            close(con);
            close(pstmt);
            close(rs);
        }
        return list;
    }





    public boolean updatePhone(String phone, Long id) throws DBException {
        Connection connection = null;
        PreparedStatement statement = null;
        boolean result = true;
        try {
            connection = getConnection(CONNECTION_URL);

            statement = connection.prepareStatement(UPDATE_PHONE);
            statement.setString(1, phone);
            statement.setLong(2, id);
            statement.execute();

        } catch (SQLException e) {
            result = false;
            logger.error("Error in updating device phone", e);
            throw new DBException("Error in updating device phone", e);
        } finally {
            close(connection);
            close(statement);
        }
        return result;
    }

    public boolean updateData(Device device) throws DBException {
        Connection connection = null;
        PreparedStatement statement = null;
        boolean result =  true;
        try {
            connection = getConnection(CONNECTION_URL);

            statement = connection.prepareStatement(UPDATE_DATA);
            statement.setString(1, device.getTemperature());
            statement.setString(2, device.getHumidity());
            statement.setTimestamp(3, TimeConverter.stringToTimestamp(device.getLastDate()));
            statement.setLong(4, device.getId());

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
