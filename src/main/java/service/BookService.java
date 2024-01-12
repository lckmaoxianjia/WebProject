package service;

import entity.Book;
import entity.BookList;

public interface BookService {
    BookList getBookList(String pageNow, String pageSize);
    Book getBookDetails(int id);

    int addBook(Book book);

    boolean deleteBook(Integer id);

    BookList getBookListWithLike(String info,String pageNow, String pageSize);

    boolean updateBook(Book book);
}
