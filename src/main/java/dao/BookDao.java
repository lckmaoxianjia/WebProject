package dao;

import entity.Book;
import entity.BookList;

public interface BookDao {
    BookList selectBookList(int start, int pageSize);
    Book selectBookDetails(int id);

    int addBook(Book book);

    boolean deleteBook(Integer id);

    BookList selectBookListWithLike(String info,int start,int pageSize);

    boolean updateBook(Book book);
}
