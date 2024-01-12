package service.impl;

import dao.impl.BookDaoImpl;
import entity.Book;
import entity.BookList;
import service.BookService;

public class BookServiceImpl implements BookService {
    private final BookDaoImpl bookDao;

    public BookServiceImpl(){
        bookDao = new BookDaoImpl();
    }

    //通过service层传递分页内pageNow和pageSize参数给dao层进行查找
    @Override
    public BookList getBookList(String pageNow, String pageSize) {
        int num = Integer.parseInt(pageNow);
        int size = Integer.parseInt(pageSize);
        return bookDao.selectBookList((num-1)*size, size);
    }

    //通过service层传递id参数给dao层进行查找
    @Override
    public Book getBookDetails(int id) {
        return bookDao.selectBookDetails(id);
    }

    @Override
    public int addBook(Book book) {
        return bookDao.addBook(book);
    }

    @Override
    public boolean deleteBook(Integer id) {
        return bookDao.deleteBook(id);
    }

    @Override
    public BookList getBookListWithLike(String info, String pageNow, String pageSize) {
        int num = Integer.parseInt(pageNow);
        int size = Integer.parseInt(pageSize);
        return bookDao.selectBookListWithLike(info,(num-1)*size, size);

    }

    @Override
    public boolean updateBook(Book book) {
        return bookDao.updateBook(book);
    }
}
