package main.java.ua.nure.bogun.apiproject.servlet.arduino;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import main.java.ua.nure.bogun.apiproject.entity.User;
import main.java.ua.nure.bogun.apiproject.service.RequestResponseService;
import main.java.ua.nure.bogun.apiproject.service.TimeConverter;
import main.java.ua.nure.bogun.apiproject.service.databaseservice.DeviceService;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

@WebServlet("/arduino/command")
public class DeviceArduinoServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(DeviceArduinoServlet.class);
    private final Gson gson = new GsonBuilder().serializeNulls().create();

    private final RequestResponseService r = new RequestResponseService();


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            readCommand(request, response, user);
        } else {
            logger.error("User is not valid: " + gson.toJson((User) session.getAttribute("user")));
        }
    }

    void readCommand(HttpServletRequest request, HttpServletResponse response, User user) {

        String command = request.getParameter("command");
        String deviceId = request.getParameter("deviceId");
        String phone = request.getParameter("phone");
        String temperature = request.getParameter("temperature");
        String humidity = request.getParameter("humidity");
        String currentDate = TimeConverter.currentTimestamp().toString();
        if (!r.notEmpty(command, deviceId)) {
            r.write(r.setError(), request, response);
            return;
        } else if (
                DeviceService.getInstance().getDeviceById(
                        Long.parseLong(deviceId)
                ).getUserId() != user.getId()) {
            r.write(r.setError(), request, response);
            return;
        }
        switchConditions(
                command,
                deviceId,
                phone,
                temperature,
                humidity,
                currentDate,
                request,
                response
        );

    }


    private void switchConditions(String command,
                                  String deviceId,
                                  String phone,
                                  String temperature,
                                  String humidity,
                                  String currentDate,
                                  HttpServletRequest request,
                                  HttpServletResponse response
    ) {
        switch (command) {
            case "getDeviceById":
                r.write(
                        (r.notEmpty(deviceId)) ?
                                gson.toJson(
                                        DeviceService.getInstance().
                                                getDeviceById(Long.parseLong(deviceId))
                                ) : r.setError(),
                        request,
                        response
                );
                break;
            case "updatePhone":
                r.write(
                        (r.notEmpty(phone, deviceId)) ?
                                gson.toJson(
                                        DeviceService.getInstance().
                                                updatePhone(phone, Long.parseLong(deviceId))
                                ) : r.setError(),
                        request,
                        response
                );
                break;
            case "updateData":
                r.write(
                        (r.notEmpty(deviceId, temperature, humidity, currentDate)) ?
                                gson.toJson(
                                        DeviceService.getInstance().
                                                updateData(
                                                        Long.parseLong(deviceId),
                                                        temperature,
                                                        humidity,
                                                        currentDate
                                                )
                                ) : r.setError(),
                        request,
                        response
                );
                break;
            default:
                r.write(r.setError(), request, response);
                break;

        }
    }
}
