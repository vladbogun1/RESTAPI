package main.java.ua.nure.bogun.apiproject.servlet.web;

import com.google.gson.Gson;
import main.java.ua.nure.bogun.apiproject.service.RequestResponseService;
import main.java.ua.nure.bogun.apiproject.service.databaseservice.DeviceService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/webDevice")
public class DeviceWebServlet extends HttpServlet {
    private final Gson gson = new Gson();
    private final RequestResponseService r = new RequestResponseService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if(session.getAttribute("user")!=null){
            readCommand(request,response);
        } else {
            response.setStatus(401);
            r.write("Unauthorized", request, response);
        }
    }

    void readCommand(HttpServletRequest request, HttpServletResponse response) {
        String command = request.getParameter("command");
        String deviceId = request.getParameter("deviceId");
        String phone = request.getParameter("phone");
//        String temperature = request.getParameter("temperature");
//        String humidity = request.getParameter("humidity");
//        String lastDate = request.getParameter("lastDate");
        String userId = request.getParameter("userId");
        RequestResponseService r = new RequestResponseService();
        if (r.notEmpty(command)){
            r.write(r.setError(),request,response);
            return;
        }
        switch (command){
            case "getDeviceById":
                r.write(
                        (r.notEmpty(deviceId))?
                        gson.toJson(
                                DeviceService.getInstance().
                                        getDeviceById(Long.parseLong(deviceId))
                        ):r.setError(),
                        request,
                        response
                );
                break;
            case "getAllDevicesByUserId":
                r.write(
                        (r.notEmpty(userId))?
                                gson.toJson(
                                        DeviceService.getInstance().
                                                getAllDevicesByUserId(Long.parseLong(userId))
                                ):r.setError(),
                        request,
                        response
                );
                break;
            case "updatePhone":
                r.write(
                        (r.notEmpty(phone,deviceId))?
                                gson.toJson(
                                        DeviceService.getInstance().
                                                updatePhone(phone,Long.parseLong(deviceId))
                                ):r.setError(),
                        request,
                        response
                );
                break;
            case "getAllDevices":
                r.write(
                        gson.toJson(
                                DeviceService.getInstance().
                                        getAllDevices()
                        ),
                        request,
                        response
                );
                break;
            case "deleteDevice":
                r.write(
                        (r.notEmpty(deviceId))?
                                gson.toJson(
                                        DeviceService.getInstance().
                                                deleteDevice(Long.parseLong(deviceId))
                                ):r.setError(),
                        request,
                        response
                );
                break;
            default:
                r.write(r.setError(),request,response);
                break;

        }

    }





}
