package mypackage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Library implements Serializable{
	private static final long serialVersionUID = 1L;
	private List<Reader> readers = new ArrayList<Reader>();
    private List<Book> books = new ArrayList<Book>();

    public Library(){
        open("books");
        open("readers");
    }

    public void addBook(Book book){
        books.add(book);
    }

    public void addReader(Reader reader){
        readers.add(reader);
    }

    public Reader searchForReader(String pesel){
        for(Reader reader : readers){
            if(Objects.equals(reader.getPesel(), pesel))
                return reader;
        }
        return null;
    }

    public Book searchForBook(String title, String author){
        for(Book book : books){
            if(Objects.equals(book.getTitle(), title) && Objects.equals(book.getAuthor(), author))
                return book;
        }
        return null;
    }

    public boolean rentBook(Reader reader, Book book){
        if(reader.canRent()==false)
            return false;
        reader.addBook(book);
        book.setDate(LocalDateTime.now());
        book.changeStatus();
        return true;
    }

    public void listBooks(){
        for(Book book : books){
            System.out.println(book.getTitle()+" "+book.getAuthor()+" "+book.getCategory()+" "+book.getStatus());
        }
        System.out.print("\n");
    }

    public void listReaders(){
        for(Reader reader : readers){
            System.out.println(reader.getFirstName()+" "+reader.getLastName());
            reader.listBooks();
        }
        System.out.print("\n");
    }

    public void returnBook(Book searchedBook){
        for(Book book : books){
            if(Objects.equals(book.getTitle(), searchedBook.getTitle()) && Objects.equals(book.getAuthor(), searchedBook.getAuthor()) && Objects.equals(book.getStatus(), searchedBook.getStatus())){
                book.changeStatus();
                return;
            }
        }
    }

    public void save(String pathname){
		pathname+=".dat";
		try{
			FileOutputStream fos = new FileOutputStream(pathname);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
            if(Objects.equals(pathname, "readers.dat"))
			    oos.writeObject(readers);
            else
                oos.writeObject(books);
			oos.flush();
			oos.close();
			fos.close();
		}catch(Exception ex){
			System.out.println(ex.toString());
		}
	}
	
	@SuppressWarnings("unchecked")
	public void open(String pathname){
		pathname+=".dat";
		try{
			FileInputStream fis = new FileInputStream(pathname);
			ObjectInputStream ois = new ObjectInputStream(fis);
			if(Objects.equals(pathname, "readers.dat"))
                readers = (List<Reader>) (ois.readObject());
            else
			    books = (List<Book>) (ois.readObject());
			ois.close();
			fis.close();
		}catch(Exception ex){
			System.out.println(ex.toString());
		}
	}

    public static void main(String args[]){
        Library library = new Library();
        for(int i=0; i<args.length-3; i+=4){
            if(Objects.equals(args[i], "a")){
                Book book = new Book(args[i+1], args[i+2], args[i+3]);
                library.addBook(book);
                System.out.println("Book added to the system");
            }
            else if(Objects.equals(args[i], "b")){
                Reader reader = new Reader(args[i+1], args[i+2], args[i+3]);
                library.addReader(reader);
                System.out.println("Reader added to the system");
            }
            else if(Objects.equals(args[i], "c")){
                Reader reader = library.searchForReader(args[i+1]);
                if(reader==null){
                    System.out.println("There is no such a person in the system");
                    continue;
                }
                Book book = library.searchForBook(args[i+2], args[i+3]);
                if(book==null)
                {
                    System.out.println("There is no such a book in the system");
                    continue;
                }
                if(book.getStatus()==true){
                    System.out.println("Book already rented");
                    continue;
                }
                if(library.rentBook(reader, book)){
                    System.out.println("Book rented successfully");
                }
                else{
                    System.out.println("Book cannot be rented: reader has a book not returned on time");
                }
            }
            else if(Objects.equals(args[i], "d")){
                Reader reader = library.searchForReader(args[i+1]);
                if(reader==null){
                    System.out.println("There is no such a person in the system");
                    continue;
                }
                Book book = reader.searchForBook(args[i+2], args[i+3]);
                if(book==null)
                {
                    System.out.println("This reader did not rent such a book");
                    continue;
                }
                double fee = book.calcFee();
                if(fee>0)
                    System.out.println("Reader paid "+fee+" for delay");
                library.returnBook(book);
                reader.returnBook(book);
                System.out.println("Book returned successfully");
            }
            else
                i-=3;
        }
        System.out.println("\n");
        library.listBooks();
        library.listReaders();
        library.save("books");
        library.save("readers");
    }
}
