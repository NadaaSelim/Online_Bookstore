db = connect( 'mongodb://localhost/bookstore_db' );

db.createCollection("users")
db.createCollection("books")
db.createCollection("requests")
db.createCollection("reviews")
db.createCollection("messages")