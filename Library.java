import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;

public class Library {
    public static String HELP_STRING = "EXIT ends the library process\nCOMMANDS outputs this help string\n\nLIST ALL [LONG] outputs either the short or long string for all books\nLIST AVAILABLE [LONG] outputs either the short of long string for all available books\nNUMBER COPIES outputs the number of copies of each book\nLIST GENRES outputs the name of every genre in the system\nLIST AUTHORS outputs the name of every author in the system\n\nGENRE <genre> outputs the short string of every book with the specified genre\nAUTHOR <author> outputs the short string of every book by the specified author\n\nBOOK <serialNumber> [LONG] outputs either the short or long string for the specified book\nBOOK HISTORY <serialNumber> outputs the rental history of the specified book\n\nMEMBER <memberNumber> outputs the information of the specified member\nMEMBER BOOKS <memberNumber> outputs the books currently rented by the specified member\nMEMBER HISTORY <memberNumber> outputs the rental history of the specified member\n\nRENT <memberNumber> <serialNumber> loans out the specified book to the given member\nRELINQUISH <memberNumber> <serialNumber> returns the specified book from the member\nRELINQUISH ALL <memberNumber> returns all books rented by the specified member\n\nADD MEMBER <name> adds a member to the system\nADD BOOK <filename> <serialNumber> adds a book to the system\n\nADD COLLECTION <filename> adds a collection of books to the system\nSAVE COLLECTION <filename> saves the system to a csv file\n\nCOMMON <memberNumber1> <memberNumber2> ... outputs the common books in members' history";

    private List<Book> books  = new ArrayList<Book>();
    private List<Member> members  = new ArrayList<Member>();
    private int nextMemberNumber = 100000;

    public Library(){
    }
    
//  1 - MY METHODS  \\
//  1.1 - Book Methods
    public boolean noBooks() {  // Checking for no books
        if (this.books.size() == 0) {
            System.out.println("No books in system.");
            return true;
        }
        return false;
    }
    public boolean noSuchBook(String serialNumber) {  // Checking if specific book exists
        boolean noBook = true;
        for (int i = 0; i < this.books.size(); i++) {
            Book currentBook = this.books.get(i);
            if (serialNumber.equals(currentBook.getSerialNumber())) {
                noBook = false;
            }
        }
        if (noBook == true) {
            System.out.println("No such book in system.");
            return true;
        }
        return false; 
    }
    public Book getBookBySerialNumber(String serialNumber){  // Returns specific book
        for (int i = 0; i < this.books.size(); i++) {
            Book currentBook = this.books.get(i);
            if (serialNumber.equals(currentBook.getSerialNumber())) {
                return currentBook;
            }
        }
        return null;
    }

//  1.2 - Member Methods
    public boolean noMembers() {  // Checking for no members
        if (this.members.size() == 0) {
            System.out.println("No members in system.");
            return true;
        }
        return false;
    }
    public boolean noSuchMember(String memberNumber) {  // Checking if specific member exists
        boolean noMember = true;
        for (int i = 0; i < this.members.size(); i++) {
            Member currentMember = this.members.get(i);
            if (memberNumber.equals(currentMember.getMemberNumber())) {
                noMember = false;
            }
        }
        if (noMember == true) {
            System.out.println("No such member in system.");
            return true;
        }
        return false; 
    }
    public Member getMemberByMemberNumber(String memberNumber){  // Returns specific member
        for (int i = 0; i < this.members.size(); i++) {
            Member currentMember = this.members.get(i);
            if (memberNumber.equals(currentMember.getMemberNumber())) {
                return currentMember;
            }
        }
        return null;
    }

//  2 - COMPULSORY METHODS  \\
//  2.1 - Book Methods
    public void addBook(String bookfile, String serialNumber) {  // Adds a book to the system by reading it from a csv file
        for (int i = 0; i < this.books.size(); i++) {
            Book currentBook = this.books.get(i);
            if (serialNumber.equals(currentBook.getSerialNumber())) {
                System.out.println("Book already exists in system.");
                return;
            }
        }
        Book addedBook = Book.readBook(bookfile, serialNumber);
        if (addedBook != null) {
            this.books.add(addedBook);
        }
    }
    public void addCollection(String filename) {  // Adds the collection of books stored in a csv file to the system
        List<Book> listToAdd = Book.readBookCollection(filename);
        int numOfAddedBooks = 0;
        if (listToAdd != null) {
            for (int i = 0; i < listToAdd.size(); i++) {
                Boolean exists = false;
                Book currentBook = listToAdd.get(i);
                for (int j = 0; j < this.books.size(); j++) {
                    Book copyCheck = this.books.get(j);
                    if (copyCheck.getSerialNumber().equals(currentBook.getSerialNumber())) {
                        exists = true;
                        break;
                    }
                }
                if (exists == false) {
                    this.books.add(currentBook);
                    numOfAddedBooks += 1;
                }
            }
            if (numOfAddedBooks == 0) {
                System.out.println("No books have been added to the system.");
            } else {
                System.out.println(numOfAddedBooks + " books successfully added.");
            }
        }
    }

    public void getAllBooks(boolean fullString) {  // Prints out the formatted strings for all books in the system
        if (this.noBooks() == true) {
            return;
        }
        for (int i = 0; i < this.books.size(); i++) {
            Book currentBook = this.books.get(i);
            if (fullString == true) {
                System.out.println(currentBook.longString());
                if (i != (this.books.size() - 1)) {
                    System.out.println("");
                }
            } else {
                System.out.println(currentBook.shortString());
            }
        }
    }
    public void getAvailableBooks(boolean fullString) {  // Prints out the formatted strings for all available books in the system
        if (this.noBooks() == true) {
            return;
        }
        int availableBooks = 0;
        for (int i = 0; i < this.books.size(); i++) {
            Book currentBook = this.books.get(i);
            if (currentBook.isRented() == false) {
                if (fullString == true) {
                    System.out.println(currentBook.longString());
                    if (i != (this.books.size() - 1)) {
                        System.out.println("");
                    }
                } else {
                    System.out.println(currentBook.shortString());
                }
                availableBooks += 1;
            }
        }
        if (availableBooks == 0) {
            System.out.println("No books available.");
        }
    }
   public void getCopies() {  // Prints out the number of copies of each book in the system
        if (this.noBooks() == true) {
            return;
        }
        List<String> sortedList = new ArrayList<String>();
        for (int i = 0; i < this.books.size(); i++) {
            Book currentBook = this.books.get(i);
            String currentTitle = currentBook.getTitle();
            String currentAuthor = currentBook.getAuthor();
            int numOfCopies = 0;
            for (int j = 0; j < this.books.size(); j++) {
                Book copyBook = this.books.get(j);
                String copyTitle = copyBook.getTitle();
                String copyAuthor = copyBook.getAuthor();
                if (copyTitle.equalsIgnoreCase(currentTitle) == true && copyAuthor.equalsIgnoreCase(currentAuthor) == true) {
                    numOfCopies += 1;
                }
            }
            if (sortedList.contains(currentBook.shortString() + ": " + numOfCopies) != true) {
                sortedList.add(currentBook.shortString() + ": " + numOfCopies);
            }
        }
        Collections.sort(sortedList, String.CASE_INSENSITIVE_ORDER);
        for (int i = 0; i < sortedList.size(); i++) {
            System.out.println(sortedList.get(i));
        }
    }
    public void getGenres() {  // Prints out all the genres in the system
        if (this.noBooks() == true) {
            return;
        }
        List<String> genres = new ArrayList<String>();
        for (int i = 0; i < this.books.size(); i++) {
            Book currentBook = this.books.get(i);
            String currentGenre = currentBook.getGenre();
            boolean exists = false;
            for (int j = 0; j < genres.size(); j++) {
                if (genres.get(j).equalsIgnoreCase(currentGenre)) {
                    exists = true;
                }
            }
            if (exists == false) {
                genres.add(currentGenre);
            }
        }
        Collections.sort(genres, String.CASE_INSENSITIVE_ORDER);
        for (int i = 0; i < genres.size(); i++) {
            System.out.println(genres.get(i));
        }
    }
    public void getAuthors() {  // Prints out all the authors in the system
        if (this.noBooks() == true) {
            return;
        }
        List<String> authors = new ArrayList<String>();
        for (int i = 0; i < this.books.size(); i++) {
            Book currentBook = this.books.get(i);
            String currentAuthor = currentBook.getAuthor();
            boolean exists = false;
            for (int j = 0; j < authors.size(); j++) {
                if (authors.get(j).equalsIgnoreCase(currentAuthor)) {
                    exists = true;
                }
            }
            if (exists == false) {
                authors.add(currentAuthor);
            }
        }
        Collections.sort(authors, String.CASE_INSENSITIVE_ORDER);
        for (int i = 0; i < authors.size(); i++) {
            System.out.println(authors.get(i));
        }
    }
    public void getBooksByGenre(String genre) {  // Prints all books in the system with the specified genre
        if (this.noBooks() == true) {
            return;
        }
        int booksOfGenre = 0;
        List<Integer> serialNumOfSameGenres = new ArrayList<Integer>(); 
        for (int i = 0; i < this.books.size(); i++) {
            Book currentBook = this.books.get(i);
            if (currentBook.getGenre().equalsIgnoreCase(genre)) {
                int serialNum = Integer.parseInt(currentBook.getSerialNumber());
                serialNumOfSameGenres.add(serialNum);
                booksOfGenre += 1;
            }
        }
        if (booksOfGenre == 0) {
            System.out.println("No books with genre " + genre + ".");
        } else {
            Collections.sort(serialNumOfSameGenres);
            for (int i = 0; i < serialNumOfSameGenres.size(); i++) {
                String serialNumStr = serialNumOfSameGenres.get(i).toString();
                for (int j = 0; j < this.books.size(); j++) {
                    Book currentBook = this.books.get(j);
                    if (currentBook.getSerialNumber().equals(serialNumStr) == true) {
                        System.out.println(currentBook.shortString());
                    }
                }
            }
        }
    }
    public void getBooksByAuthor(String author) {  // Prints all books in the system by the specified author
        if (this.noBooks() == true) {
            return;
        }
        int booksOfAuthor = 0;
        List<Integer> serialNumOfSameAuthors = new ArrayList<Integer>(); 
        for (int i = 0; i < this.books.size(); i++) {
            Book currentBook = this.books.get(i);
            if (currentBook.getAuthor().equalsIgnoreCase(author)) {
                int serialNum = Integer.parseInt(currentBook.getSerialNumber());
                serialNumOfSameAuthors.add(serialNum);
                booksOfAuthor += 1;
            }
        }
        if (booksOfAuthor == 0) {
            System.out.println("No books by " + author + ".");
        } else {
            Collections.sort(serialNumOfSameAuthors);
            for (int i = 0; i < serialNumOfSameAuthors.size(); i++) {
                String serialNumStr = serialNumOfSameAuthors.get(i).toString();
                for (int j = 0; j < this.books.size(); j++) {
                    Book currentBook = this.books.get(j);
                    if (currentBook.getSerialNumber().equals(serialNumStr) == true) {
                        System.out.println(currentBook.shortString());
                    }
                }
            }
        }
    }
    public void getBook(String serialNumber, boolean fullString) {  // Prints either the short or long string of the specified book
        if (this.noBooks() == true) {
            return;
        }
        if (this.noSuchBook(serialNumber) == true) {
            return;
        }
        Book book = this.getBookBySerialNumber(serialNumber);
        if (fullString == true) {
            System.out.println(book.longString());
        } else {
            System.out.println(book.shortString());
        }
    }
    public void bookHistory(String serialNumber) {  // Prints out all the member numbers of members who have previously rented a book
        if (this.noSuchBook(serialNumber) == true) {
            return;
        }
        Book book = this.getBookBySerialNumber(serialNumber);
        List<Member> history = book.renterHistory();
        if (history.isEmpty()) {
            System.out.println("No rental history.");
            return;
        }
        for (int i = 0; i < history.size(); i++) {
            Member currentMember = history.get(i);
            System.out.println(currentMember.getMemberNumber());
        }
    }
    public void saveCollection(String filename) {  // Saves the current collection of books in the system to a csv file
        if (this.noBooks() == true) {
            return;
        }
        Book.saveBookCollection(filename, this.books);
    }

//  2.2 - Member Methods
    public void addMember(String name) {  // Adds a member to the system
        String memberNumberStr = Integer.toString(this.nextMemberNumber);
        Member newMember = new Member(name, memberNumberStr);
        this.members.add(newMember);
        System.out.println("Success.");
        this.nextMemberNumber += 1;
    }
    public void getMember(String memberNumber) {  // Prints the details of the specified member
        if (this.noMembers() == true) {
            return;
        }
        if (this.noSuchMember(memberNumber) == true) {
            return;
        }
        Member member = this.getMemberByMemberNumber(memberNumber);
        System.out.println(member.getMemberNumber() + ": " + member.getName());
    }
    public void getMemberBooks(String memberNumber) {  // Prints a list of all the books a member is currently renting
        if (this.noMembers() == true) {
            return;
        }
        if (this.noSuchMember(memberNumber) == true) {
            return;
        }
        Member member = this.getMemberByMemberNumber(memberNumber);
        List<Book> renting = member.renting();
        if (renting.size() == 0) {
            System.out.println("Member not currently renting.");
            return;
        }
        for (int i = 0; i < renting.size(); i++) {
            Book currentBook = renting.get(i);
            System.out.println(currentBook.shortString());
        }
    }
    public void memberRentalHistory(String memberNumber) {  // Prints a list of all the books a member has previously rented
        if (this.noMembers() == true) {
            return;
        }
        if (this.noSuchMember(memberNumber) == true) {
            return;
        }
        Member member = this.getMemberByMemberNumber(memberNumber);
        List<Book> history = member.history();
        if (history.size() == 0) {
            System.out.println("No rental history for member.");
            return;
        }
        for (int i = 0; i < history.size(); i++) {
            Book currentBook = history.get(i);
            System.out.println(currentBook.shortString());
        }    
    }

//  2.3 - Member and Book Integration Methods
    public void rentBook(String memberNumber, String serialNumber) {  // Loans out a book to a member within the system
        if (this.noMembers() == true) {
            return;
        }
        if (this.noBooks() == true) {
            return;
        }
        if (this.noSuchMember(memberNumber) == true) {
            return;
        }
        if (this.noSuchBook(serialNumber) == true) {
            return;
        }       
        Book book = this.getBookBySerialNumber(serialNumber);
        Member member = this.getMemberByMemberNumber(memberNumber);

        if (book.isRented() == true) {
            System.out.println("Book is currently unavailable.");
            return;
        }
        member.rent(book);
        book.rent(member);
        
        book.setCurrentRenter(member);
        book.setRent(true);

        for (int i = 0; i < this.books.size(); i++) {  // Updating list of books
            Book currentBook = this.books.get(i);
            if (book.getSerialNumber().equals(currentBook.getSerialNumber())) {
                this.books.set(i, book);
            }
        } 
        for (int i = 0; i < this.members.size(); i++) {  // Updating list of members
            Member currentMember = this.members.get(i);
            if (member.getMemberNumber().equals(currentMember.getMemberNumber())) {
                this.members.set(i, member);
            }
        }
        System.out.println("Success.");
    }
    
    public void relinquishBook(String memberNumber, String serialNumber) {  // Returns a book to the system
        if (this.noMembers() == true) {
            return;
        }
        if (this.noBooks() == true) {
            return;
        }
        if (this.noSuchMember(memberNumber) == true) {
            return;
        }
        if (this.noSuchBook(serialNumber) == true) {
            return;
        }
        Book book = this.getBookBySerialNumber(serialNumber);
        Member member = this.getMemberByMemberNumber(memberNumber);

        if (book.getCurrentRenter() != member) {
            System.out.println("Unable to return book.");
            return;
        }
        member.relinquish(book);
        book.relinquish(member);

        member.currentlyRenting.remove(book);
        book.setCurrentRenter(null);
        book.history.add(member);
        book.setRent(false);

        for (int i = 0; i < this.books.size(); i++) {  // Updating list of books
            Book currentBook = this.books.get(i);
            if (book.getSerialNumber().equals(currentBook.getSerialNumber())) {
                this.books.set(i, book);
            }
        } 
        for (int i = 0; i < this.members.size(); i++) {  // Updating list of members
            Member currentMember = this.members.get(i);
            if (member.getMemberNumber().equals(currentMember.getMemberNumber())) {
                this.members.set(i, member);
            }
        }
        System.out.println("Success.");
    }

    public void relinquishAll(String memberNumber) {  // Makes a member return all books they are currently renting
        if (this.noMembers() == true) {
            return;
        }
        if (this.noSuchMember(memberNumber) == true) {
            return;
        }

        Member member = this.getMemberByMemberNumber(memberNumber);
        for (int i = 0; i < member.currentlyRenting.size(); i++) {
            Book book = member.currentlyRenting.get(i);
            book.setCurrentRenter(null);
            book.setRent(false);
            book.history.add(member);
            member.history.add(book);
            for (int j = 0; j < this.books.size(); j++) {  // Updating list of books
                Book currentBook = this.books.get(i);
                if (book.getSerialNumber().equals(currentBook.getSerialNumber())) {
                    this.books.set(i, book);
                }
            }
        }
        List<Book> remover = new ArrayList<Book>(member.currentlyRenting);
        member.currentlyRenting.removeAll(remover);

        for (int i = 0; i < this.members.size(); i++) {  // Updating list of members
            Member currentMember = this.members.get(i);
            if (member.getMemberNumber().equals(currentMember.getMemberNumber())) {
                this.members.set(i, member);
            }
        }
        System.out.println("Success.");
    }
    public void common(String[] memberNumbers) {  // Prints out all the books that all members provided have previously rented
        if (this.noMembers() == true) {
            return;
        }
        if (this.noBooks() == true) {
            return;
        }
        Member[] members = new Member[memberNumbers.length];
        for (int i = 0; i < memberNumbers.length; i++) {
            if (this.noSuchMember(memberNumbers[i]) == true) {
                return;
            }

            for (int j = 1; j < memberNumbers.length-i; j++) {
                if (memberNumbers[i].equals(memberNumbers[i+j])) {
                    System.out.println("Duplicate members provided.");
                    return;
                }
            }

            Member currentMember = this.getMemberByMemberNumber(memberNumbers[i]);
            members[i] = currentMember;
        }
        List<Book> commonBooks = Member.commonBooks(members);
        if (commonBooks == null || commonBooks.size() == 0) {
            System.out.println("No common books.");
            return;
        }
        for (int i = 0; i < commonBooks.size(); i++) {
            System.out.println(commonBooks.get(i).shortString());
        }
    }

    public static void main(String[] args) {
        Library librarian = new Library();
        Scanner input = new Scanner(System.in);
        while(input.hasNextLine()) {  //  Asking for user input until program is manually exited
            System.out.print("user: ");
            String command = input.nextLine();
            String[] commandArr = command.split(" ");
            if (commandArr.length > 1) {
                if (command.equalsIgnoreCase("LIST ALL")) {
                    librarian.getAllBooks(false);
                }
                if (command.equalsIgnoreCase("LIST ALL LONG")) {
                    librarian.getAllBooks(true);
                }
                if (command.equalsIgnoreCase("LIST AVAILABLE")) {
                    librarian.getAvailableBooks(false);
                }
                if (command.equalsIgnoreCase("LIST AVAILABLE LONG")) {
                    librarian.getAvailableBooks(true);
                }
                if (command.equalsIgnoreCase("NUMBER COPIES")) {
                    librarian.getCopies();
                }
                if (command.equalsIgnoreCase("LIST GENRES")) {
                    librarian.getGenres();
                }
                if (command.equalsIgnoreCase("LIST AUTHORS")) {
                    librarian.getAuthors();
                }
                if (commandArr[0].equalsIgnoreCase("GENRE")) {
                    String[] chars = command.split("");
                    String genre = "";
                    for (int i = 6; i < chars.length; i++) {
                        genre += chars[i];
                    }
                    librarian.getBooksByGenre(genre);
                }
                if (commandArr[0].equalsIgnoreCase("AUTHOR")) {
                    String[] chars = command.split("");
                    String author = "";
                    for (int i = 7; i < chars.length; i++) {
                        author += chars[i];
                    }
                    librarian.getBooksByAuthor(author);
                }
                
                if (commandArr[0].equalsIgnoreCase("BOOK")) {
                    if (commandArr[1].equalsIgnoreCase("HISTORY")) {
                        librarian.bookHistory(commandArr[2]);
                    } else {
                        if (commandArr.length == 3) {
                            if (commandArr[2].equalsIgnoreCase("LONG")) {
                                librarian.getBook(commandArr[1], true);
                            }
                        } else {
                            librarian.getBook(commandArr[1], false);
                        }
                    }
                }
                
                if (commandArr[0].equalsIgnoreCase("MEMBER")) {
                    if (commandArr.length == 2) {
                        librarian.getMember(commandArr[1]);
                    } else {
                        if (commandArr[1].equalsIgnoreCase("BOOKS")) {
                            librarian.getMemberBooks(commandArr[2]);
                        }
                        if (commandArr[1].equalsIgnoreCase("HISTORY")) {
                            librarian.memberRentalHistory(commandArr[2]);
                        }
                    }
                }

                if (commandArr[0].equalsIgnoreCase("ADD")) {
                    if (commandArr[1].equalsIgnoreCase("MEMBER")) {
                        String[] chars = command.split("");
                        String memberStr = "";
                        for (int i = 11; i < chars.length; i++) {
                            memberStr += chars[i];
                        }
                        librarian.addMember(memberStr);
                    }
                    
                    if (commandArr[1].equalsIgnoreCase("BOOK")) {
                        librarian.addBook(commandArr[2], commandArr[3]);
                    }
                }

                
                if (commandArr[1].equalsIgnoreCase("COLLECTION")) {
                    if (commandArr[0].equalsIgnoreCase("ADD")) {
                        librarian.addCollection(commandArr[2]);
                    }
                    if (commandArr[0].equalsIgnoreCase("SAVE")) {
                        librarian.saveCollection(commandArr[2]);                
                    }
                }

                if (commandArr[0].equalsIgnoreCase("COMMON")) {
                    String[] memberNumbers = new String[commandArr.length - 1];
                    for (int i = 0; i < commandArr.length - 1; i++) {
                        memberNumbers[i] = commandArr[i + 1];
                    }
                    librarian.common(memberNumbers);
                }

                if (commandArr[0].equalsIgnoreCase("RENT")) {
                    librarian.rentBook(commandArr[1], commandArr[2]);
                } 
                if (commandArr[0].equalsIgnoreCase("RELINQUISH") && commandArr[1].equalsIgnoreCase("ALL") == false) {
                    librarian.relinquishBook(commandArr[1], commandArr[2]);
                } 
                if (commandArr[0].equalsIgnoreCase("RELINQUISH") && commandArr[1].equalsIgnoreCase("ALL")) {
                    librarian.relinquishAll(commandArr[2]);
                } 
            }  
       
            if (command.equalsIgnoreCase("COMMANDS")) {
                System.out.println(librarian.HELP_STRING);
            }
            if (command.equalsIgnoreCase("EXIT")) {
                System.out.println("Ending Library process.");
                break;
            }
            System.out.println("");
        }
        input.close();;
	}
}