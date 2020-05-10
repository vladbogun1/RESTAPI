package main.java.ua.nure.bogun.apiproject.servlet.arduino;

import com.google.gson.Gson;
import main.java.ua.nure.bogun.apiproject.database.DBException;
import main.java.ua.nure.bogun.apiproject.entity.User;
import main.java.ua.nure.bogun.apiproject.service.Login;
import main.java.ua.nure.bogun.apiproject.service.RequestResponseService;
import main.java.ua.nure.bogun.apiproject.service.UserLoginService;
import main.java.ua.nure.bogun.apiproject.servlet.LoginServlet;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/arduino")
public class LoginArduinoServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(LoginArduinoServlet.class);
    private final Gson gson = new Gson();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestResponseService r = new RequestResponseService();
        HttpSession session = request.getSession();
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        if (session.getAttribute("user") == null ) {
            if(!r.notEmpty(login, password)){
                logger.info("Arduino no correct password or login");
                r.write(r.setError(),request,response);
                return;
            }
            UserLoginService service = new UserLoginService();
            try {
                Login.STATUS status = service.logining(login, password);
                switch (status) {
                    case TRUE:
                        response.setStatus(200);
                        session.setAttribute("user", service.getCash());
                        break;
                    default:
                        response.setStatus(400);
                        r.write(r.setError(),request,response);
                }
            } catch (DBException e) {
                logger.error("Error in authentication", e);
                response.sendError(500);
            }
            logger.info("Arduino loggined, user:  " + gson.toJson((User)session.getAttribute("user")));

        }
        request.getRequestDispatcher("/arduino/command").forward(request,response);

    }
}
