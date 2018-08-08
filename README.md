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

| Endpoint      | Method     | Data sent with the request | Description |
|---------------|------------|----------------------------|-------------|
|/api/authors[?name=]   | GET        |                | Displays all existing authors. Filtering by name is optional.|
|/api/authors/{id}      | GET        |                | Display the author with specified id |
|/api/authors           | POST       | JSON containing new author data | Create new author
|/api/authors/{id}      | PUT        | JSON containing data that is going to replace old data | Replace old author data with new data |
|/api/authors/{id}      | DELETE     |                    | Delete author that has specified id |
|/api/genres            | GET        |                    | Displays all existing genres |
|/api/genres/{id}       | GET        |                    | Displays the genre with specified id |
|/api/genres            | POST       | JSON containing new genre data | Creates new genre |
|/api/genres/{id}       | PUT        | JSON containing genre data that is going to replace old data | Replace old genre data with new data |
|/api/genres/{id}       | DELETE     |                    | Delete genre that has specified id |



