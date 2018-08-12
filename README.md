# Library REST API

Features of this app:
* Custom authentication system based on JWT tokens
* Error handling of invalid requests
* CRUD operations on authors, genres, books and entries.
* Advanced searching with GET parameters

## Docs

### Registration and token generation endpoints 

| Endpoint      | Method     | Data sent with the request | Description |
|---------------|------------|------------------------|-------------|
|/users/register| POST       | [JSON](https://github.com/Echelon133/SpringLibrary#user-registration) | On this endpoint new users can be registered |
|/users/get-token| GET      | Basic authentication data must be provided | After successful authorization token that is valid for 3 days is going to be generated and returned |

### API

All requests to any endpoint have to have *Authorization* field set with previously generated token.
Only GET endpoints can be used by users without admin privileges. All endpoints that modify the data require admin privileges.

| Endpoint               | Method     | Data sent with the request | Description |
|------------------------|----------|----------------------------|-------------|
|/api/authors            | GET      |                | Display all existing authors. 
|/api/authors?name=      | GET      |                | Display authors filtered by name |
|/api/authors/{id}       | GET      |                | Display the author with specified id |
|/api/authors            | POST     | [JSON](https://github.com/Echelon133/SpringLibrary#author-createreplace) | Create a new author
|/api/authors/{id}       | PUT      | [JSON](https://github.com/Echelon133/SpringLibrary#author-createreplace) | Replace old author data with new data |
|/api/authors/{id}       | DELETE   |                    | Delete the author that has specified id |
|/api/genres             | GET      |                    | Display all existing genres |
|/api/genres/{id}        | GET      |                    | Display the genre with specified id |
|/api/genres             | POST     | [JSON](https://github.com/Echelon133/SpringLibrary#genre-createreplace) | Create a new genre |
|/api/genres/{id}        | PUT      | [JSON](https://github.com/Echelon133/SpringLibrary#genre-createreplace) | Replace old genre data with new data |
|/api/genres/{id}        | DELETE   |                    | Delete genre that has specified id |
|/api/books              | GET      |                    | Display all existing books |
|/api/books?title=       | GET      |                    | Display books filtered by title |
|/api/books?author=      | GET      |                    | Display books filtered by author name|
|/api/books?genre=       | GET      |                    | Display books filtered by genre name|
|/api/books/{id}         | GET      |                    | Display the book that has specified id|
|/api/books/{id}/bookInfo| GET      |                    | Display book info of the book that has specified id |
|/api/books/{id}/bookInfo| PATCH    | [JSON](https://github.com/Echelon133/SpringLibrary#book-info-update)| Update chosen fields of book info |
|/api/books              | POST     | [JSON](https://github.com/Echelon133/SpringLibrary#book-create) | Create a new book |
|/api/books              | PATCH    | [JSON](https://github.com/Echelon133/SpringLibrary#book-update) | Update chosen fields of book |
|/api/books/{id}         | DELETE   |                      | Delete book that has specified id |
|/api/entries            | GET      |                      | Display all existing entries |
|/api/entries?returned=  | GET      |                      | Display entries filtered by "returned status". This parameter accepts "true"/"false" values. |
|/api/entries?bookTitle= | GET      |                      | Display entries filtered by book title |
|/api/entries?username=  | GET      |                      | Display entries filtered by **exact** username |
|/api/entries?since=     | GET      |                      | Display entries added since specified time. Accepted values: *day*/*week*/*month*/*year*. If the parameter value does not belong to accepted values, all entries are going to be displayed |
|/api/entries/{id}       | GET      |                      | Display entry that has specified id |
|/api/entries            | POST     | [JSON](https://github.com/Echelon133/SpringLibrary#entry-create) | Create a new entry |
|/api/entries/{id}       | PATCH    | [JSON](https://github.com/Echelon133/SpringLibrary#entry-patch) | Update status of the entry that has specified id |


### Json objects

#### User registration

* *username* length between 4 and 50
* *password* length between 6 and 100
* passwords must match

```JSON
{
  "username" : "some-username",
  "password" : "user-password",
  "password2" : "user-password"
}
```

#### Author create/replace

* *name* length between 1 and 100
* *description* length between 10 and 3000

```JSON
{
  "name" : "author name",
  "description" : "author description text"
}
```

#### Genre create/replace

* *name* length between 1 and 50
* *description* length between 10 and 1500

```JSON
{
  "name" : "genre name",
  "description" : "genre description text"
}
```

#### Book create 

* *title* length between 1 and 255
* *authorIds* size between 1 and 10
* *genreIds* size between 1 and 10

```JSON
{
  "title" : "New book title",
  "authorIds" : [1, 2],
  "genreIds" : [1, 4]
}
```

#### Book update

Same JSON as above, but fields that are not updated can be ommited.

#### Book Info update

All fields are optional, so it is possible to ex. update only one field, two fields etc.

* *numberOfPages* allows only positive numbers up to 4 digits
* *language* length between 3 and 30
* *publicationYear* allows only positive numbers up to 4 digits
* *description* length between 10 and 500
* *isbn* is validated against the ISBN specification 

```JSON
{
  "numberOfPages" : 100,
  "language" : "English",
  "publicationYear" : 2018,
  "description" : "book description text",
  "isbn" : "978-1-56619-909-4"
}
```

#### Entry create

* both values are required

```JSON
{
  "borrowedBookId" : 5,
  "borrowerUsername" : "some-user"
}
```

#### Entry patch

* *returned* field set to true also sets *dateFinished* field of the entry to the date of the status change
* once *returned* is set to true it cannot be changed anymore

```JSON
{
  "returned" : true
}
```

## Screens

### Get Token
![GET-TOKEN](https://github.com/Echelon133/SpringLibrary/blob/master/screens/1GET-TOKEN.png)

### Create a Genre
![CREATE-GENRE](https://github.com/Echelon133/SpringLibrary/blob/master/screens/2CREATE-GENRE.png)

### Get Genres
![GET-GENRES](https://github.com/Echelon133/SpringLibrary/blob/master/screens/3GET-GENRES.png)

### Get a Single Genre
![GET-SINGLE-GENRE](https://github.com/Echelon133/SpringLibrary/blob/master/screens/4GET-SINGLE-GENRE.png)

### Replace a Genre
![REPLACE-GENRE](https://github.com/Echelon133/SpringLibrary/blob/master/screens/5REPLACE-GENRE.png)

### Delete a Genre
![DELETE-GENRE](https://github.com/Echelon133/SpringLibrary/blob/master/screens/6DELETE-GENRE.png)

### Create an Author
![CREATE-AUTHOR](https://github.com/Echelon133/SpringLibrary/blob/master/screens/7CREATE-AUTHOR.png)

### Get Authors
![GET-AUTHORS](https://github.com/Echelon133/SpringLibrary/blob/master/screens/8GET-AUTHORS.png)

### Get a Single Author
![GET-SINGLE-AUTHOR](https://github.com/Echelon133/SpringLibrary/blob/master/screens/9GET-SINGLE-AUTHOR.png)

### Replace an Author
![REPLACE-AUTHOR](https://github.com/Echelon133/SpringLibrary/blob/master/screens/10REPLACE-AUTHOR.png)

### Delete an Author
![DELETE-AUTHOR](https://github.com/Echelon133/SpringLibrary/blob/master/screens/11DELETE-AUTHOR.png)

### Filter Authors By Name
![FILTER-AUTHOR-BY-NAME](https://github.com/Echelon133/SpringLibrary/blob/master/screens/12FILTER-AUTHOR-BY-NAME.png)


