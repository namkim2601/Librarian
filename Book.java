import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class Book {
    private String title;
    private String author;
    private String genre;
    private String serialNumber;
    private boolean rented;
    
    public Member currentRenter;
    
    public List<Member> history = new ArrayList<Member>();

    public Book(String title, String author, String genre, String serialNumber) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.serialNumber = serialNumber;
    }
    
    public String getTitle() {  // Returns the title of the book
        return this.title;
    }
    public String getAuthor() {  // Returns the author of the book
        return this.author;
    }
    public String getGenre() {  // Returns the genre of the book.
        return this.genre;
    }
    public String getSerialNumber() {  // Returns the serial number of the book
        return this.serialNumber;
    }
    public Member getCurrentRenter() {  // Returns the current renter of the book
        return this.currentRenter;
    }
    
    public void setRent(boolean rented) {  // Sets the rent state of the book
        this.rented = rented;
    }
    public void setCurrentRenter(Member member) {  // Sets the current renter of the book
        this.currentRenter = member;
    }

    public String longString() {  // Formats the Book object to create the long form of its toString()
        String longStr = (this.serialNumber+": "+this.title+" ("+this.author+", "+this.genre+")");
        if (this.isRented() == true && this.currentRenter != null) {
            longStr += ("\nRented by: " + this.currentRenter.getMemberNumber() + ".");
        } else {
            longStr += "\nCurrently available.";
        }
        return longStr;
    }
    public String shortString() {  // Formats the Book object to create the short form of its toString()
        String shortStr = (this.title+" ("+this.author+")");
        return shortStr;
    }

    public List<Member> renterHistory() {  // Returns the renter history, in chronological order
        return this.history;
    }

    public boolean isRented() {  // Returns whether the book is currently being rented by a member of the library
        if (this == null) {
            return false;
        }
        return this.rented;
    }

    public boolean rent(Member member) {  // Sets the current renter to be the given member
        if (member == null || this.isRented() == true) {
            return false;
        }
        this.currentRenter = member;
        this.rented = true;
        return true;
    }

    public boolean relinquish(Member member) {  // Returns the book to the library
        if (member == null || this.currentRenter != member) {
            return false;
        }
        this.currentRenter = null;
        this.rented = false;
        this.history.add(member);
        return true;
    }

    public static Book readBook(String filename, String serialNumber) {  // Retrieves the book from the given file based on its serial number
        if (filename == null || serialNumber == null) {
            return null;
        }
        File bookFile = new File(filename);
		try {
		 	Scanner scan = new Scanner(bookFile);
		 	while (scan.hasNextLine()) {
				String s = scan.nextLine();
				String[] arr = s.split(",");
                if (arr[0].equals(serialNumber)) {
                    String title = arr[1];
                    String author = arr[2];
                    String genre = arr[3];
                    Book newBook = new Book(title, author, genre, serialNumber);
                    System.out.println("Successfully added: " + newBook.shortString() +".");
                    return newBook;
                }
             }
		scan.close(); 
		} catch (FileNotFoundException e) {
            System.out.println("No such file.");
			return null;
        }
        System.out.println("No such book in file.");
        return null;
    }
    public static List<Book> readBookCollection(String filename) {  // Reads in the collection of books from the given file
        if (filename == null) {
            return null;
        }
        File bookFile = new File(filename);
        List<Book> books = new ArrayList<Book>();
		try {
		 	Scanner scan = new Scanner(bookFile);
		 	while (scan.hasNextLine()) {
				String s = scan.nextLine();
				String[] arr = s.split(",");
                try {  
                    Integer.parseInt(arr[0]);  
                } catch(NumberFormatException e){  
                    continue;
                }
                String serialNumber = arr[0];
                String title = arr[1];
                String author = arr[2];
                String genre = arr[3];
                Book currentBook = new Book(title, author, genre, serialNumber);
                books.add(currentBook);
            }
		scan.close(); 
		} catch (FileNotFoundException e) {
            System.out.println("No such collection.");
			return null;
        }
     
        return books;
    }
    public static void saveBookCollection(String filename, Collection<Book> books) {  // Save the collection of books to the given file
        if (filename == null || books == null) {
            return;
        }
        try {
	        FileWriter myWriter = new FileWriter(filename);
			myWriter.write("serialNumber,title,author,genre");
			myWriter.write("\n");
            for (int i = 0; i < books.size(); i++) {
                List<Book> booksArrList = new ArrayList<Book>(books);
                Book currentBook = booksArrList.get(i);
                String title = currentBook.getTitle();
                String author = currentBook.getAuthor();
                String genre = currentBook.getGenre();
                String serialNumber = currentBook.getSerialNumber();
                myWriter.write(serialNumber+","+title+","+author+","+genre);
                myWriter.write("\n");
                }
            System.out.println("Success.");
            myWriter.close();
		} catch (IOException e) {
			return;
		}
    }
    
    public static List<Book> filterAuthor(List<Book> books, String author){  // Creates a new list containing books by the specified author
        if (books == null || author == null) {
            return null;
        }
        List<Book> sortedList = new ArrayList<Book>(); 
        List<Integer> serialNumsOfSameAuthor = new ArrayList<Integer>();
        for (int i = 0; i < books.size(); i++) {
            Book currentBook = books.get(i);
            if (currentBook == null) {
                continue;
            } else {
                if (author.equalsIgnoreCase(currentBook.getAuthor())) {
                    int serialNum = Integer.parseInt(currentBook.getSerialNumber());
                    serialNumsOfSameAuthor.add(serialNum);
                }
            }
        }
        Collections.sort(serialNumsOfSameAuthor);
        for (int i = 0; i < serialNumsOfSameAuthor.size(); i++) {
            String serialNumStr = serialNumsOfSameAuthor.get(i).toString();
            for (int j = 0; j < books.size(); j++) {
                Book currentBook = books.get(i);
                if (currentBook.getSerialNumber().equals(serialNumStr) && sortedList.contains(currentBook) == false) {
                    sortedList.add(currentBook);
                } 
            }
        }
        return sortedList;
    }
    public static List<Book> filterGenre(List<Book> books, String genre){  // Creates a new list containing books by the specified genre
        if (books == null || genre == null) {
            return null;
        }
        List<Book> sortedList = new ArrayList<Book>(); 
        List<Integer> serialNumsOfSameGenre = new ArrayList<Integer>();
        books.removeAll(Collections.singleton(null));
        for (int i = 0; i < books.size(); i++) {
            Book currentBook = books.get(i);
            if (genre.equalsIgnoreCase(currentBook.getGenre())) {
                int serialNum = Integer.parseInt(currentBook.getSerialNumber());
                serialNumsOfSameGenre.add(serialNum);
            }
        }
        Collections.sort(serialNumsOfSameGenre);
        for (int i = 0; i < serialNumsOfSameGenre.size(); i++) {
            String serialNumStr = serialNumsOfSameGenre.get(i).toString();
            for (int j = 0; j < books.size(); j++) {
                Book currentBook = books.get(i);
                if (currentBook.getSerialNumber().equals(serialNumStr) && sortedList.contains(currentBook) == false) {
                    sortedList.add(currentBook);
                } 
            }
        }
        return sortedList;
    }
}