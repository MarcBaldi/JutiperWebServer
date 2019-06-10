//package api

import java.sql.{Connection, DriverManager}

import akka.actor.ActorSystem
import akka.http.javadsl.model.StatusCodes
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpHeader.ParsingResult.Ok
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.server.{Route, StandardRoute}
import akka.stream.ActorMaterializer
import play.api.libs.json._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
//import spray.json._

import model.User

import scala.concurrent.Future
import scala.io.StdIn

// collect your json format instances into a support trait:
/*trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  //implicit val itemFormat = jsonFormat2(Item)
  //implicit val orderFormat = jsonFormat1(Order) // contains List[Item]
}*/

class HttpServer() { // extends Directives with JsonSupport

  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  // there's probably a better way to do this
  var connection: Connection = null


  val route: Route =
    get {
      pathSingleSlash {
        complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, "<h1>Jutiper Server</h1>"))
      } ~
        path("insertUser") {
          //TODO: add parameters
          insertUser("Marc3", "Marc", "Bal", "Germany", "02.04.1994", "marc@mail.de", "jutimarc");
          statetoHtml
        } ~
        path("getUser" / "user") {
          getUserByUsername("Marc2")
          statetoHtml
        } ~
        path("testJSON") {
          val myJson = userToJson(getUserByUsername("Marc2"))
          complete(HttpEntity(ContentTypes.`application/json`, myJson.toString()))
        } ~
        path("putUser" / Segment) { command => {
          processInputLine(command)
          }
          println("test1")
          complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, "<h1>Transmitted Successfully</h1>"))
        } ~
        path("move" / "right") {
          statetoHtml
        } ~
        path("move" / "down") {
          statetoHtml
        } ~
        path("wait") {
          statetoHtml
        } ~
        path("search") {
          statetoHtml
        } ~
        path("drop" / IntNumber) { slot =>
          statetoHtml
        } ~
        path("equip" / IntNumber) { slot =>
          statetoHtml
        }
    }
  /*~
  post {
    entity(as[Order]) { order => // will unmarshal JSON to Order
      val itemsCount = order.items.size
      val itemNames = order.items.map(_.name).mkString(", ")
      complete(s"Ordered $itemsCount items: $itemNames")
    }
  }*/


  def userToJson(user: User): JsObject = {
    val myJson = Json.obj(
      "Username" -> user.Username,
      "Firstname" -> user.Firstname,
      "Surname" -> user.Surname,
      "Birthplace" -> user.Birthplace,
      "Birthdate" -> user.Birthdate,
      "Email" -> user.Email,
      "Passwort" -> user.Password
    )
    myJson
  }

  def statetoHtml: StandardRoute = {
    complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Jutip</h1>"))
  }

  val bindingFuture: Future[Http.ServerBinding] = Http().bindAndHandle(route, "localhost", 8080)

  def unbind(): Unit = {
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }

  def connectDatabase(): Unit = {

    // connect to the database named "mysql" on the localhost
    val driver = "com.mysql.cj.jdbc.Driver"
    val url = "jdbc:mysql://localhost/jutiper"
    val username = "root"
    val password = "jutiper"

    try {
      // make the connection
      Class.forName(driver)
      //connection = DriverManager.getConnection(url, username, password)

      //import java.sql.DriverManager
      connection = DriverManager.getConnection(url + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Europe/Berlin", username, password)

      // create the statement, and run the select query
      val statement = connection.createStatement()
      val resultSet = statement.executeQuery("SELECT Username, Surname FROM user")
      while (resultSet.next()) {
        val host = resultSet.getString("Username")
        val user = resultSet.getString("Surname")
        println("Username = " + host + ", " + user)
      }
      println("DB-Access successful")
    } catch {
      case e: Throwable => e.printStackTrace
    }
    //connection.close()
  }

  /*def getJson() = Action {

  }*/

  def closeDatabase(): Unit = {
    connection.close()
  }

  def insertUser(Username: String, Firstname: String, Surname: String, Birthplace: String, Birthdate: String, Email: String, Password: String): Unit = {
    val statement = connection.createStatement()
    val resultSet = statement.executeUpdate("INSERT INTO user (Username, Firstname, " +
      "Surname, Birthplace, Birthdate, Email, Password) VALUES " +
      "('" + Username + "', '" + Firstname + "', '" + Surname + "', '" + Birthplace + "', '" + Birthdate + "', '" + Email + "', '" + Password + "');")
    println("DB-request successfully sent")

  }


  def getUserAsString(Username: String): String = {
    val statement = connection.createStatement()
    val resultSet = statement.executeQuery("SELECT * FROM jutiper.user WHERE Username = '" + Username + "';")
    resultSet.first()
    val Firstname = resultSet.getString("Firstname")
    val Surname = resultSet.getString("Surname")
    val Birthplace = resultSet.getString("Birthplace")
    val Birthdate = resultSet.getString("Birthdate")
    val Email = resultSet.getString("Email")
    val Password = resultSet.getString("Password")

    println("" + Username + " " + Firstname + " " + Surname + " " + Birthplace + " " + Birthdate + " " + Email + " " + Password)

    return "" + Username + " " + Firstname + " " + Surname + " " + Birthplace + " " + Birthdate + " " + Email + " " + Password
  }

  def getUserByUsername(Username: String): User = {
    val statement = connection.createStatement()
    val resultSet = statement.executeQuery("SELECT * FROM jutiper.user WHERE Username = '" + Username + "';")
    resultSet.first()
    val Firstname = resultSet.getString("Firstname")
    val Surname = resultSet.getString("Surname")
    val Birthplace = resultSet.getString("Birthplace")
    val Birthdate = resultSet.getString("Birthdate")
    val Email = resultSet.getString("Email")
    val Password = resultSet.getString("Password")

    val user = new User(Username, Firstname, Surname, Birthplace, Birthdate, Email, Password)

    return user
  }

  def processInputLine(input: String): Unit = {
    println("processing input ...")
    /*
    input.toList.filter(c => c != ' ').map(c => c.toString) match {
      case username :: firstname :: surname :: birthplace :: birthdate :: email :: password :: Nil => setUserNew(username, firstname, surname, birthplace, birthdate, email, password)
      case username ::  Nil => println(username)
      case username :: firstname :: Nil => println(username + firstname)
      case _ => println(input)
    }
    */
    val infolist = input.split(" ")
    println(infolist(0)+ infolist(1))
    setUserNew(infolist(0), infolist(1), infolist(2), infolist(3), infolist(4), infolist(5), infolist(6))

  }

  def setUserNew(Username: String, Firstname: String, Surname: String, Birthplace: String, Birthdate: String, Email: String, Password: String): Unit = {
    insertUser(Username, Firstname, Surname, Birthplace, Birthdate, Email, Password)

  }
}
