package dao.impl;

import dao.BookDao;
import entity.Book;
import entity.BookList;
import util.JDBCConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class BookDaoImpl implements BookDao {
    //获取图书列表
    @Override
    public BookList selectBookList(int start, int pageSize) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        ArrayList<Book> books = new ArrayList<>();
        try {
            //建立数据库连接
            conn = JDBCConnect.getConnection();
            String sql = "select * from book limit " + start + "," + pageSize;
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            //查询记录  将查找出来的数据装入ArrayList<Book>
            while (rs.next()) {
                int id = rs.getInt("id");
                String book_name = rs.getString("book_name");
                String author = rs.getString("author");
                Date publish_date = rs.getDate("publish_date");
                int pages = rs.getInt("pages");
                String price = rs.getString("price");
                Book book = new Book(id, book_name, author, publish_date, pages, price);
                books.add(book);
            }

            //查询符合条件的记录条数,sql语句优化器默认用主键索引查询
            sql = "select count(*) from book ";
            ps = conn.prepareStatement(sql.toString());
            rs = ps.executeQuery();
            int count = 0;
            while (rs.next()) {
                count = rs.getInt(1);
            }
            BookList booklist = new BookList(books, count);

            return booklist;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //释放数据库连接
            JDBCConnect.release(conn, ps, rs);
        }
        return null;
    }

    //根据id查询数据库内图书详情信息
    @Override
    public Book selectBookDetails(int id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            //建立数据库连接
            conn = JDBCConnect.getConnection();
            String sql = "select * from book where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                Book book = new Book();
                book.setId(rs.getInt("id"));
                book.setBook_name(rs.getString("book_name"));
                book.setAuthor(rs.getString("author"));
                book.setPublish_date(rs.getDate("publish_date"));
                book.setPages(rs.getInt("pages"));
                book.setPrice(rs.getString("price"));
                book.setPicture(rs.getString("picture"));
                book.setContent(rs.getString("content"));
                return book;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //释放数据库连接
            JDBCConnect.release(conn, ps, rs);
        }
        return null;
    }

    @Override
    public int addBook(Book book) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            //建立数据库连接
            conn = JDBCConnect.getConnection();
            String sql = "INSERT INTO `book` VALUES (?, ?, ?, ?, ?, ?, ?, ?) ";
            ps = conn.prepareStatement(sql);
            Object[] paramsList = {book.getId(),book.getBook_name(),book.getAuthor(),
                    book.getPublish_date(),book.getPages(),book.getPrice(),
                    book.getPicture(),book.getContent()};


            if(null != paramsList && paramsList.length>0){
                for(int i = 0;i<paramsList.length;i++){
                    ps.setObject(i+1, paramsList[i]);
                }
            }
            return  ps.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //释放数据库连接
            JDBCConnect.release(conn, ps, rs);
        }

        return 0;
    }

    //删除图书信息
    @Override
    public boolean deleteBook(Integer id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            //建立数据库连接
            conn = JDBCConnect.getConnection();
            String sql = "DELETE FROM book where id='"+id+"'";
            ps = conn.prepareStatement(sql);
            int count = ps.executeUpdate();

            if (count > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //释放数据库连接
            JDBCConnect.release(conn, ps, rs);
        }
        return false;
    }

    @Override
    public BookList selectBookListWithLike(String info, int start, int pageSize) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        ArrayList<Book> books = new ArrayList<>();
        try {
            //建立数据库连接
            conn = JDBCConnect.getConnection();
            String sql  = "SELECT * from book WHERE book_name LIKE ? limit ?,?";
            Object[] paramsList = {info,start,pageSize};
            ps = conn.prepareStatement(sql);

            if(null != paramsList && paramsList.length>0){
                for(int i = 0;i<paramsList.length;i++){
                    if(i==0){
                        ps.setObject(i+1,"%"+info+"%");
                    }else{
                        ps.setObject(i+1, paramsList[i]);
                    }

                }
            }
            rs = ps.executeQuery();

            //查询记录  将查找出来的数据装入ArrayList<Book>
            while (rs.next()){
                int id=rs.getInt("id");
                String book_name = rs.getString("book_name");
                String author = rs.getString("author");
                Date publish_date = rs.getDate("publish_date");
                int pages = rs.getInt("pages");
                String price = rs.getString("price");
                Book book = new Book(id, book_name, author, publish_date, pages, price);
                books.add(book);
            }

            //查询符合条件的记录条数,sql语句优化器默认用主键索引查询
            sql = "select count(*) from book where book_name LIKE ?";
            ps = conn.prepareStatement(sql.toString());
            ps.setObject(1,"%"+info+"%");
            rs = ps.executeQuery();
            int count = 0;
            while (rs.next()) {
                count = rs.getInt(1);
            }
            BookList booklist = new BookList(books, count);

            return booklist;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //释放数据库连接
            JDBCConnect.release(conn, ps, rs);
        }
        return null;
    }

    @Override
    public boolean updateBook(Book book) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            //建立数据库连接
            conn = JDBCConnect.getConnection();
            String sql = "UPDATE book SET book_name=?, author=?, publish_date=?, pages=?, price=?, content=? WHERE id=?";

            ps = conn.prepareStatement(sql);
            ps.setString(1, book.getBook_name());
            ps.setString(2, book.getAuthor());

            // todo 注意：这里不能直接使用 java.sql.Date 强制类型转换，因为java.util.Date和java.sql.Date之间没有直接的继承关系，因此不能直接转换
            // TODO 可以使用java.sql.Date的构造方法转换
            ps.setDate(3, new java.sql.Date(book.getPublish_date().getTime()));

            ps.setInt(4, book.getPages());
            ps.setString(5, book.getPrice());
            ps.setString(6, book.getContent());
            ps.setInt(7, book.getId());

            int count = ps.executeUpdate();

            if (count > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //释放数据库连接
            JDBCConnect.release(conn, ps, rs);
        }
        return false;
    }
}
