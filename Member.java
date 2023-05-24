import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class Member {
    private String name;
    private String memberNumber;
    
    public List<Book> currentlyRenting = new ArrayList<Book>();
    public List<Book> history = new ArrayList<Book>();
    
    public Member(String name, String memberNumber) {
        this.name = name;
        this.memberNumber = memberNumber;
    }

    public String getName() {  // Returns the name of the member
        return this.name;
    }
    public String getMemberNumber() {  // Returns the member number of the member
        return this.memberNumber;
    }

    public boolean rent(Book book) {  // Rents the given book
        if (book == null || book.isRented() == true) {
            return false;
        }
        book.setCurrentRenter(this);
        book.setRent(true);
        this.currentlyRenting.add(book);
        return true;
    }
    public boolean relinquish(Book book) {  // Returns the book to the libary
        if (book == null || book.getCurrentRenter() != this) {
            return false;
        }
        book.setCurrentRenter(null);
        book.setRent(false);
        this.currentlyRenting.remove(book);
        this.history.add(book);
        return true; 
    }
    public void relinquishAll() {   // Returns all books rented by the member
        for (int i = 0; i < this.currentlyRenting.size(); i++) {
            Book currentBook = this.currentlyRenting.get(i);
            currentBook.setCurrentRenter(null);
            currentBook.setRent(false);
            this.history.add(currentBook);
        }
        List<Book> remover = new ArrayList<Book>(currentlyRenting);
        this.currentlyRenting.removeAll(remover);
    } 

    public List<Book> history() {   // Returns the history of books rented, in the order they were returned (oldest first)
        return this.history;
    }
    public List<Book> renting() {   // Returns the list of books currently being rented, in the order they were rented
        return this.currentlyRenting;
    }
    public static List<Book> commonBooks(Member[] members) {    // Returns the intersection of the members' histories, ordered by serial number
        if (members == null || members.length == 0) {
            return null;
        }
        List<Integer> serialNums = new ArrayList<Integer>();
        List<Book> sortedList = new ArrayList<Book>(); 
        for (int i = 0; i < members[0].history.size(); i++) {
            int numOfSameMembers = 0;
            for (int j = 0; j < members.length; j++) {
                for (int k = 0; k < members[j].history.size(); k++) {
                    if (members[0].history.get(i) == members[j].history.get(k)) {
                        numOfSameMembers += 1;
                    }
                }
            }
            if (numOfSameMembers == members.length) {
                Book currentBook = members[0].history.get(i);
                int serialNum = Integer.parseInt(currentBook.getSerialNumber());
                serialNums.add(serialNum);
            }
        }
        Collections.sort(serialNums);
        for (int i = 0; i < serialNums.size(); i++) {
            String serialNumStr = serialNums.get(i).toString();
            for (int j = 0; j < members[0].history.size(); j++) {
                Book currentBook = members[0].history.get(j);
                if (currentBook.getSerialNumber().equalsIgnoreCase(serialNumStr)) {
                    sortedList.add(currentBook);
                } 
            } 
        }
        return sortedList;
    }
}