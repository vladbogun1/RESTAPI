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

@WebServlet("/getUser")
public class CurrentUserServlet extends HttpServlet {
    private final Gson gson = new Gson();
    private final RequestResponseService r = new RequestResponseService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if(session.getAttribute("user")!=null){
            response.setStatus(200);
            r.write(
                    gson.toJson((User)session.getAttribute("user")),
                    request,
                    response
            );

        } else {
            response.setStatus(401);
            r.write("{null}", request, response);
        }
    }
}
