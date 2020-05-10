package main.java.ua.nure.bogun.apiproject.servlet.web;

import com.google.gson.Gson;
import main.java.ua.nure.bogun.apiproject.entity.Role;
import main.java.ua.nure.bogun.apiproject.entity.User;
import main.java.ua.nure.bogun.apiproject.service.RequestResponseService;
import main.java.ua.nure.bogun.apiproject.service.databaseservice.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/webUser")
public class UserWebServlet extends HttpServlet {
    private final Gson gson = new Gson();
    private final RequestResponseService r = new RequestResponseService();

    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if(session.getAttribute("user")!=null){
            readCommand((User)session.getAttribute("user"), request,response);
        } else {
            response.setStatus(401);
            r.write("Unauthorized", request, response);
        }
    }

    void readCommand(User requestUser, HttpServletRequest request, HttpServletResponse response) {
        String command = request.getParameter("command");
        String userId = request.getParameter("userId");
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String oldPass = request.getParameter("oldPassword");
        String newPass = request.getParameter("newPassword");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        if (!r.notEmpty(command)){
            r.write(r.setError(),request,response);
            return;
        }
        switch (command){

            case "createUser":
                r.write(
                        (r.notEmpty(login, password, firstName, lastName, email) && Role.checkRole(requestUser, Role.ADMIN))?
                                gson.toJson(
                                        UserService.getInstance().
                                                createUser(login, password, firstName, lastName, "1", email)
                                ):r.setError(),
                        request,
                        response
                );
                break;
            case "deleteUser":
                r.write(
                        (r.notEmpty(userId))?(Role.checkRole(requestUser, Role.ADMIN)||requestUser.getId()==Long.parseLong(userId))?
                                gson.toJson(
                                        UserService.getInstance().
                                                deleteUser(Long.parseLong(userId))
                                ):r.setError():r.setError(),
                        request,
                        response
                );
                break;
            case "getUserById":
                r.write(
                        (r.notEmpty(userId) )?(Role.checkRole(requestUser, Role.ADMIN)||requestUser.getId()==Long.parseLong(userId))?
                                gson.toJson(
                                        UserService.getInstance().
                                                getUserById(Long.parseLong(userId))
                               ):r.setError():r.setError(),
                        request,
                        response
                );
                break;
            case "changePassword":
                r.write(
                        (r.notEmpty(userId, oldPass, newPass))?
                                gson.toJson(
                                        UserService.getInstance().updatePassword(Long.parseLong(userId), oldPass, newPass)
                                ):r.setError(),
                        request,
                        response
                );
                break;
            case "getAllUsers":
                r.write(
                        (Role.checkRole(requestUser, Role.ADMIN))?
                                gson.toJson(
                                        UserService.getInstance().getAllUsers()
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
