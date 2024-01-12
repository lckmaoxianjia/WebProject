package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Book;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author:lck
 * @Date 2023/12/15 10:45
 */

@WebServlet(name = "UpdateBookServlet", value = "/updateBook")
public class UpdateBookServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Book book = new Book();
        book.setId(Integer.parseInt(req.getParameter("id")));
        book.setBook_name(req.getParameter("book_name"));
        book.setAuthor(req.getParameter("author"));
        try {
            book.setPublish_date(new SimpleDateFormat("yyy-MM-dd").parse(req.getParameter("publish_date")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        book.setPages(Integer.parseInt(req.getParameter("pages")));
        book.setPrice(req.getParameter("price"));
        book.setPicture(null);
        book.setContent(req.getParameter("content"));

        System.out.println(book.getBook_name());

        BookService bookService = new BookServiceImpl();
        boolean updateResult = bookService.updateBook(book);

        //将List转换成json字符串
        ObjectMapper om = new ObjectMapper();
        Map<String,Object> map = new HashMap<>();

        if(updateResult){
            map.put("message", "修改图书成功");
            // CodeNumEntity.SUCCESS.getCode():200      CodeNumEntity.SUCCESS.getMessage():"请求成功"
            ResponseMessage responseMessage = new ResponseMessage(CodeNumEntity.SUCCESS.getCode(), CodeNumEntity.SUCCESS.getMessage(), map);
            //向前端发送数据
            //om.writeValueAsString()将respMessage转换成json字符串
            resp.getWriter().write(om.writeValueAsString(responseMessage));
        }else {
            map.put("message", "修改图书失败");
            //400 操作失败
            ResponseMessage responseMessage = new ResponseMessage(CodeNumEntity.FAIL.getCode(), CodeNumEntity.FAIL.getMessage(), map);
            resp.getWriter().write(om.writeValueAsString(responseMessage));
        }

    }
}
