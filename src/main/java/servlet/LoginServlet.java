package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.CodeNumEntity;
import entity.ResponseMessage;
import entity.User;
import service.impl.UserServiceImpl;
import service.UserService;
import util.Constants;

import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response){
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UserService userService = new UserServiceImpl();
        ObjectMapper om = new ObjectMapper();
        ResponseMessage responseMessage = new ResponseMessage();

        //防止用swagger访问接口时报空指针异常
        if(!username.trim().equals("") && !password.trim().equals("")) {
            User user = userService.login(username, password);
            Map<String, Object> map = new HashMap<>();
            if (user != null) {
                request.getSession().setAttribute(Constants.User_Session, user);
                map.put("username", user.getUsername());
                responseMessage.setStatusCode(CodeNumEntity.SUCCESS.getCode());
                responseMessage.setStatusMessage(CodeNumEntity.SUCCESS.getMessage());
                responseMessage.setContent(map);
            } else {
                responseMessage.setStatusCode(CodeNumEntity.USER_ERROR.getCode());
                responseMessage.setStatusMessage(CodeNumEntity.USER_ERROR.getMessage());
            }
        } else {
            responseMessage.setStatusCode(CodeNumEntity.USER_ERROR.getCode());
            responseMessage.setStatusMessage(CodeNumEntity.USER_ERROR.getMessage());
        }

        //向前端发送数据
        try {
            response.getWriter().write(om.writeValueAsString(responseMessage));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
