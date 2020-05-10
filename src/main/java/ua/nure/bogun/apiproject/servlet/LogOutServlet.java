package main.java.ua.nure.bogun.apiproject.servlet;

import main.java.ua.nure.bogun.apiproject.entity.User;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/logout")
public class LogOutServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(LogOutServlet.class);


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (!session.isNew()) {
            User user = (User)session.getAttribute("user");
            logger.info("User #" + user.getId() + " log out successful.");
            session.invalidate();
        }
        response.sendRedirect("/login");
    }
}
