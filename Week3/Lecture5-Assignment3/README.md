# Assignment 3: REST API Best Practices

## REST API Best Practices 

### 1. Use JSON
JSON is a lightweight data-interchange format that is easy to read and write for humans and easy to parse and generate for machines.

### 2. Use Nouns for Endpoints
Endpoints should represent resources (nouns) rather than actions (verbs) to align with REST principles. This makes the API more intuitive and aligns with RESTful conventions.

### 3. Plural Names for Collections
Using plural nouns to represent collections of resources helps to maintain consistency and makes the API more understandable.

### 4. Nesting Resources
Nesting resources in the URL structure can represent relationships between resources, making the API more readable and logically structured.

### 5. Error Handling
Providing consistent and meaningful error messages helps clients understand what went wrong and how to fix it. Standard HTTP status codes should be used to indicate the result of the request.

### 6. Filtering, Sorting, Pagination
These functionalities are essential for working with large datasets, providing clients the ability to retrieve the exact data they need without overloading the server or network.

### 7. Security
Security practices such as HTTPS, authentication, and authorization are crucial to protect data and ensure that only authorized users can access or modify resources.

### 8. Caching
Implementing caching can significantly improve the performance and scalability of an API by reducing the load on the server and decreasing latency for the client.

### 9. Versioning
Versioning your APIs allows you to make changes and add new features without breaking existing clients. It provides a clear way to manage API updates and deprecations.

## What can be inproved from previous assignment

### 1. Use JSON
Ensure all responses are in JSON format. This is already being followed.

### 2. Use Nouns for Endpoints
Endpoints should represent resources with nouns. This is already being followed.

### 3. Plural Names for Collections
Change the endpoint `/api/v1/contact` to `/api/v1/contacts` to represent the collection of contacts.

### 4. Nesting Resources
If there are nested resources, such as addresses or departments under contacts, consider nesting the endpoints appropriately.

### 5. Error Handling
Handle errors gracefully and return standard HTTP status codes and provide more detailed error messages in the response body for better client understanding.

### 6. Filtering, Sorting, Pagination
Implement pagination, filtering, and sorting for listing contacts using query parameters.

### 7. Security
Ensure that endpoints are secured with proper authentication and authorization mechanisms.

### 8. Caching
Implement caching strategies for GET requests to improve performance, if applicable.
