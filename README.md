## Library
The scope of the platform is to manage the activities of a library and to keep track of its books, authors, readers, sections and publishing houses.

# Objects defined on the platform
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

# Available Functionalities
