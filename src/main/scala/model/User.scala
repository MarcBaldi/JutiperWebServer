package model

//simple object to store a user  (for the certificate)
final case class User(Username: String, Firstname: String, Surname: String,
                      Birthplace: String, Birthdate: String, Email: String, Password: String)
