# Library
The scope of the platform is to manage the activities of a library and to keep track of its books, authors, readers, sections and publishing houses.

## Objects defined on the platform
1. Book class
   - an abstract class which stores basic information about a book (title, number of pages, publishing date, section, author, publishing house)
   - Pbook -> extends the Book base class, representing a physical book (adds the atribute number of copies)
   - Ebook -> extends the Book base class, representing a virtual book (adds the stribute format)
2. Section class
   - a class which represents a section of the library (name, books belonging to it)
3. Person class
   - an abstract class which stores the basic information of a person (name, birthdate, email)
   - Author -> extends base class Person, representing an author whose books can be find in the library (adds specific methods such as publish book)        
   - Reader -> extends base class Person, representing a reader enrolled in the library (adds specific methods such as lend book and return book)
4. PublishingHouse class
   - a class which stores information about a publishing house associated with the library (name, books it published)
5. Library class
   - a service class which stores a TreeSet of every component of the library and implements all the functionalities
6. Program class
   - the main class where the user can choose which functionalities to use from the existing 27
7. Audit class
   - a singleton class which saves logs regarding the actions performed and their timestamps into the file log.csv
8. CSV class
   - a singleton class which serves to read and write from/into csv files

## Available Functionalities
1. Listing the details about the library
2. Listing all the books from the library
3. Adding a new book into the library
4. Removing a book from the library
5. Adding copies of a physical book to the library
6. Removing copies of a physical book from the library
7. Listing all the sections of the library
8. Adding a new section to the library
9. Removing a section from the library
10. Listing all the books from a section
11. Adding a book into a section
12. Removing a book from a section
13. Listing all authors
14. Adding an author into the library
15. Removing an author from the library
16. Listing all the books of an author that are in the library
17. Adding a new book from an author
18. Listing all the enrolled readers of the library
19. Enrolling a new reader into the library
20. Removing a reader from the library
21. Viewing the books lent by a reader
22. Lending a book by a reader
23. Returning a book by a reader
24. Listing all publishing houses with whom the library is associated
25. Associating a new publishing house with the library
26. Removing a publishing house
27. Listing all the books from a publishing house
