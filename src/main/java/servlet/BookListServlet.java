package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.BookList;
import entity.CodeNumEntity;
import entity.ResponseMessage;
import service.BookService;
import service.impl.BookServiceImpl;

import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "BookListServlet", value = "/getBookList")
public class BookListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
        String pageNow = request.getParameter("pageNow");
        String pageSize = request.getParameter("pageSize");
        BookService bookService = new BookServiceImpl();
        BookList bookList = bookService.getBookList(pageNow, pageSize);

        //将List转换成json字符串
        ObjectMapper om = new ObjectMapper();
        ResponseMessage responseMessage = new ResponseMessage();

        if (bookList.getBookCount() != 0) {
            //查询成功
            Map<String,Object> map = new HashMap<>();
            map.put("message", bookList);
            responseMessage.setStatusCode(CodeNumEntity.SUCCESS.getCode());
            responseMessage.setStatusMessage(CodeNumEntity.SUCCESS.getMessage());
            responseMessage.setContent(map);
        } else if (bookList.getBooks() == null) {
            //未查询到数据
            responseMessage.setStatusCode(CodeNumEntity.DATABASE_ERROR.getCode());
            responseMessage.setStatusMessage(CodeNumEntity.DATABASE_ERROR.getMessage());
        } else {
            //其他异常情况
            responseMessage.setStatusCode(CodeNumEntity.SERVER_ERROR.getCode());
            responseMessage.setStatusMessage(CodeNumEntity.SERVER_ERROR.getMessage());
        }

        //向前端发送数据
        try {
            //om.writeValueAsString()将respMessage转换成json字符串
            response.getWriter().write(om.writeValueAsString(responseMessage));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response){
        doGet(request, response);
    }
}
