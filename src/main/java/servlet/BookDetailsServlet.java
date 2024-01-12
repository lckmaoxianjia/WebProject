package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Book;
import entity.CodeNumEntity;
import entity.ResponseMessage;
import service.BookService;
import service.impl.BookServiceImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "BookDetailsServlet", value = "/getBookDetails")
public class BookDetailsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //使用jackson包将数据转换为json格式
        ObjectMapper om = new ObjectMapper();
        ResponseMessage responseMessage = new ResponseMessage();

        //如果前端传输的id为空，返回操作失败-----要先判定参数是否合法（不合法的参数可能会导致业务层代码出现异常，所以在处理业务之前先判参）
        if (request.getParameter("id").equals("")) {
            responseMessage.setStatusCode(CodeNumEntity.FAIL.getCode());
            responseMessage.setStatusMessage(CodeNumEntity.FAIL.getMessage());
        } else {
            //获取前端传输的id参数
            int id = Integer.parseInt(request.getParameter("id"));
            //获取书籍信息
            BookService bookService = new BookServiceImpl();
            Book book = bookService.getBookDetails(id);

            //如果查询不到图书，则返回数据库错误
            if (book == null) {
                responseMessage.setStatusCode(CodeNumEntity.DATABASE_ERROR.getCode());
                responseMessage.setStatusMessage(CodeNumEntity.DATABASE_ERROR.getMessage());
            }
            //如果查询到图书，则正确返回信息
            if (book != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("message", book);
                responseMessage.setStatusCode(CodeNumEntity.SUCCESS.getCode());
                responseMessage.setStatusMessage(CodeNumEntity.SUCCESS.getMessage());
                responseMessage.setContent(map);
            }
            //其它情况返回服务器错误
            else {
                responseMessage.setStatusCode(CodeNumEntity.SERVER_ERROR.getCode());
                responseMessage.setStatusMessage(CodeNumEntity.SERVER_ERROR.getMessage());
            }
        }

        //向前端发送数据
        try {
            System.out.println(om.writeValueAsString(responseMessage));
            response.getWriter().write(om.writeValueAsString(responseMessage));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
