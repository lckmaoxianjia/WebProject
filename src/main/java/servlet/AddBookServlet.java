package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Book;
import entity.CodeNumEntity;
import entity.ResponseMessage;
import service.BookService;
import service.impl.BookServiceImpl;
import util.StringUtil;

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

@WebServlet(name = "AddBookServlet", value = "/addBook")
public class AddBookServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Book book = new Book();
        book.setId(Integer.parseInt(StringUtil.getSubTimeNumString(7,7)));
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

        BookService bookService = new BookServiceImpl();
        int addBookResult = bookService.addBook(book);

        ObjectMapper om = new ObjectMapper();
        Map<String, Object> map = new HashMap<>();
        ResponseMessage responseMessage = new ResponseMessage();

        if(addBookResult > 0){
            map.put("message", "添加图书成功");
            responseMessage.setStatusCode(CodeNumEntity.SUCCESS.getCode());
            responseMessage.setStatusMessage(CodeNumEntity.SUCCESS.getMessage());
            responseMessage.setContent(map);
        }else {
            map.put("message", "添加图书失败");
            responseMessage.setStatusCode(CodeNumEntity.FAIL.getCode());
            responseMessage.setStatusMessage(CodeNumEntity.FAIL.getMessage());
            responseMessage.setContent(map);
        }

        resp.getWriter().write(om.writeValueAsString(responseMessage));

    }
}
