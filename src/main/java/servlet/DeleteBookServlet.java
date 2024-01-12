package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.CodeNumEntity;
import entity.ResponseMessage;
import service.BookService;
import service.impl.BookServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author:lck
 * @Date 2023/12/22 10:31
 */

@WebServlet(name = "DeleteBookServlet", value = "/deleteBook")
public class DeleteBookServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));

        BookService deleteBook = new BookServiceImpl();

        ObjectMapper om = new ObjectMapper();
        Map<String, Object> map = new HashMap<>();
        ResponseMessage responseMessage = new ResponseMessage();

        boolean deleteBookResult = deleteBook.deleteBook(id);

        if (deleteBookResult){
            map.put("message", "删除图书成功");
            responseMessage.setStatusCode(CodeNumEntity.SUCCESS.getCode());
            responseMessage.setStatusMessage(CodeNumEntity.SUCCESS.getMessage());
            responseMessage.setContent(map);
        }else {
            map.put("message", "删除图书失败");
            responseMessage.setStatusCode(CodeNumEntity.FAIL.getCode());
            responseMessage.setStatusCode(CodeNumEntity.FAIL.getMessage());
            responseMessage.setContent(map);
        }

        resp.getWriter().write(om.writeValueAsString(responseMessage));

    }
}
