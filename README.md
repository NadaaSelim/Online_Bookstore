# Online Bookstore Project

## Overview

The Online Bookstore Project is an assignment for a Distributed Systems course. It implements a client-server architecture using **Java SE sockets and multithreading**. The application allows users to browse, search, borrow, and lend books in an online environment.

## Requirements:

- JDK 17
- MongoDB

## Features
1 - **Server-Client Communication**: Utilizes Java SE sockets for communication between the server and clients.
2 - **Book Inventory Management**: Maintains a MongoDB database of available books with details such as title, author, genre, etc.
3 - **User Authentication**: Implements login and registration functionalities for secure access to the system.
4 - **Browse and Search Books**: Enables users to browse and search for books by various criteria.
5 - **Add and Remove Books**: Allows users to add books they wish to lend and remove books they don't want to lend anymore.
6 - **Submit a Request**: Users can submit borrowing requests to other users and chat once the request is accepted.
7 - **Accept / Reject a Request**: Users can manage incoming borrowing requests by accepting or rejecting them.
8 - **Request History**: Provides users with access to their borrowing requests' history.
9 - **Library overall statistics**: Admins can view the overall status of the library.
10 - **Error Handling**: Implements mechanisms to handle various scenarios and exceptions.
11 - **Review a Book**: Add a review book feature from a specific client or based on specific user-defined review format including various data.
12 - **Admin Stats**: Calculate on the server, the overall accumulated rating for that book, and any additional review information as per your user-defined review format.
13 - **Recommendations**: Display to any reader the list of books from each genre, listed by their current reviews’ calculation. This would be one recommendation, and other recommendations would be based on the user’s personal genre preferences or those whom he mostly borrows from.

## Setup

1. Clone the repository:

```bash
git clone https://github.com/NadaaSelim/Online_Bookstore.git
```
2. Navigate to the project directory:
```bash
cd Online_Bookstore
```
3. Database setup
- Run mongodb
- Run the script `mongo_script.js` using mongosh:

```bash
mongosh --file mongo_script.js
```
4.Compile the Java files:
```bash
javac *.java
```
5.Run server
```bash
java Server
```
6. Run Messaging Server
 ```bash
java MessageServer
```
7. 
Run the client (multiple instances for testing):
```bash
java Client
```

### [More on list of commands for testing](https://docs.google.com/document/d/18qaXxgguNOCu3fRjDAp19bivuBEfIogB-0kjkPfVIDA/edit#heading=h.lakuiwdh35j3)
# Notes
No graphical user interface is available the whole project can be run through the command line.

