package mypackage;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.ArrayList;
import java.io.Serializable;


class Reader implements Serializable{
	private static final long serialVersionUID = 1L;
	private String pesel;
    private String firstName;
    private String lastName;
    private List<Book> rentedBooks = new ArrayList<Book>();

    public Reader(String pesel, String firstName, String lastName){
        this.pesel=pesel;
        this.firstName=firstName;
        this.lastName=lastName;
    }

    public void addBook(Book book){
        rentedBooks.add(book);
    }

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public String getPesel(){
        return pesel;
    }

    public boolean canRent(){
        LocalDateTime nowDate = LocalDateTime.now();
        for(Book book : rentedBooks){
            int diff = (int) ChronoUnit.HOURS.between(book.getDate(), nowDate)/24;
            if(diff>30){
                return false;
            }
        }
        return true;
    }

    public Book searchForBook(String title, String author){
        for(Book book : rentedBooks){
            if(Objects.equals(book.getTitle(), title) && Objects.equals(book.getAuthor(), author))
                return book;
        }
        return null;
    }

    public void returnBook(Book book){
        rentedBooks.remove(book);
    }

    public void listBooks(){
        for(Book book : rentedBooks){
            System.out.println("    "+book.getTitle()+" "+book.getAuthor()+" "+book.getCategory()+" "+book.getStatus());
        }
    }
}