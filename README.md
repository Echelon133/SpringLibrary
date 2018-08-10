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
|/users/register| POST       | User registration JSON | On this endpoint new users can be registered |
|/users/get-token| POST      | Basic authentication data must be provided | After successful authorization token that is valid for 3 days is going to be generated and returned |

### API

All requests to any endpoint have to have *Authorization* field set with previously generated token.
Only GET endpoints can be used by users without admin privileges. All endpoints that modify the data require admin privileges.

| Endpoint               | Method     | Data sent with the request | Description |
|------------------------|----------|----------------------------|-------------|
|/api/authors            | GET      |                | Display all existing authors. 
|/api/authors?name=      | GET      |                | Display authors filtered by name |
|/api/authors/{id}       | GET      |                | Display the author with specified id |
|/api/authors            | POST     | JSON containing new author data | Create a new author
|/api/authors/{id}       | PUT      | JSON containing data that is going to replace old data | Replace old author data with new data |
|/api/authors/{id}       | DELETE   |                    | Delete the author that has specified id |
|/api/genres             | GET      |                    | Display all existing genres |
|/api/genres/{id}        | GET      |                    | Display the genre with specified id |
|/api/genres             | POST     | JSON containing new genre data | Create a new genre |
|/api/genres/{id}        | PUT      | JSON containing genre data that is going to replace old data | Replace old genre data with new data |
|/api/genres/{id}        | DELETE   |                    | Delete genre that has specified id |
|/api/books              | GET      |                    | Display all existing books |
|/api/books?title=       | GET      |                    | Display books filtered by title |
|/api/books?author=      | GET      |                    | Display books filtered by author name|
|/api/books?genre=       | GET      |                    | Display books filtered by genre name|
|/api/books/{id}         | GET      |                    | Display the book that has specified id|
|/api/books/{id}/bookInfo| GET      |                    | Display book info of the book that has specified id |
|/api/books/{id}/bookInfo| PATCH    | JSON containing data that is going to replace old book data | Update chosen fields of book info |
|/api/books              | POST     | JSON containing new book data | Create a new book |
|/api/books              | PATCH    | JSON containing data that is going to replace old book data | Update chosen fields of book |
|/api/books/{id}         | DELETE   |                      | Delete book that has specified id |



