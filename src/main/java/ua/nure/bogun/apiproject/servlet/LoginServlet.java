package main.java.ua.nure.bogun.apiproject.servlet;

import com.google.gson.Gson;
import main.java.ua.nure.bogun.apiproject.database.DBException;
import main.java.ua.nure.bogun.apiproject.entity.User;
import main.java.ua.nure.bogun.apiproject.service.Login;
import main.java.ua.nure.bogun.apiproject.service.RequestResponseService;
import main.java.ua.nure.bogun.apiproject.service.UserLoginService;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(LoginServlet.class);
    private final Gson gson = new Gson();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestResponseService r = new RequestResponseService();
        HttpSession session = request.getSession();
        if (session.getAttribute("user") != null) {
            r.write(
                    gson.toJson((User)session.getAttribute("user")),
                    request,
                    response
            );
        } else {
            String login = request.getParameter("login");
            String password = request.getParameter("password");
            UserLoginService service = new UserLoginService();
            try {
                Login.STATUS status = service.logining(login, password);
                switch (status) {
                    case TRUE:
                        response.setStatus(200);
                        session.setAttribute("user", service.getCash());
                        r.write(gson.toJson(service.getCash()),request,response);
                        logger.info("User #" + service.getCash().getId() + " log in successful.");
                        break;
                    default:
                        response.setStatus(400);
                        r.write(r.setError(),request,response);
                }
            } catch (DBException e) {
                logger.error("Error in authentication", e);
                response.sendError(500);
            }

        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
