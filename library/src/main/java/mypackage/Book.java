package mypackage;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.io.Serializable;

class Book implements Serializable{
	private static final long serialVersionUID = 1L;
	private String title;
    private String author;
    private String category;
    private boolean status=false;
    private LocalDateTime rentalDate=null;

    public Book(String title, String author, String category){
        this.title=title;
        this.author=author;
        this.category=category;
    }

    public boolean getStatus(){
        return status;
    }

    public void changeStatus(){
        status=!status;
    }

    public String getTitle(){
        return title;
    }

    public String getAuthor(){
        return author;
    }

    public String getCategory(){
        return category;
    }

    public LocalDateTime getDate(){
        return rentalDate;
    }

    public void setDate(LocalDateTime date){
        rentalDate=date;
    }

    public double calcFee(){
        LocalDateTime nowDate = LocalDateTime.now();
        int diff = (int) ChronoUnit.HOURS.between(rentalDate, nowDate)/24;
        if(diff>30)
            return (diff-30)*0.5;
        return 0;
    }
}
