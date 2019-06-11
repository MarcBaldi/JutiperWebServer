package api

import java.sql.{Connection, DriverManager, SQLException}

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import play.api.libs.json._
import model.User

import scala.concurrent.{ExecutionContextExecutor, Future}

class HttpServer() {

  val debug = true

  implicit val system: ActorSystem = ActorSystem("my-system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  // there's probably a better way to do this
  var connection: Connection = _


  val route: Route =
    get {
      pathSingleSlash {
        complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, "<h1>Willkommen beim Jutiper Server</h1>"))
      } ~
        // simple query of a user to database
        path("getUser"/ Segment) { command =>
          {
            val myJson: Option[User] = getUserByUsername(command)
            myJson match {
              case Some(user) => {
                complete(HttpEntity(ContentTypes.`application/json`, userToJson(user).toString()))
              }
              case None => {
                complete(HttpEntity(ContentTypes.`application/json`, errorToJson("Username does not exist").toString()))
              }
            }
          }
        } ~
        // simple insertion of a user to database - maybe delete this!
        path("putUser" / Segment) { command => {processInputLine(command)}
          complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, "<h1>Transmitted Successfully</h1>"))
        } ~
        // simple insertion of a user to database
        path("putUserJson" / Segment) { command => {
          processInputLineJson(command)
        }
          complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, "<h1>Transmitted Successfully</h1>"))
        }
    }

  def userToJson(user: User): JsObject = {
    val myJson = Json.obj(
      "status" -> "success",
      "Username" -> user.Username,
      "Firstname" -> user.Firstname,
      "Surname" -> user.Surname,
      "Birthplace" -> user.Birthplace,
      "Birthdate" -> user.Birthdate,
      "Email" -> user.Email,
      "Passwort" -> user.Password
    )

    if (debug) {println("converted to json: "+ myJson)}
    myJson
  }

  def errorToJson(message: String): JsObject = {
    val myJson = Json.obj(
      "status" -> "error",
      "message" -> message
    )

    if (debug) {println("converted to json: "+ myJson)}
    myJson
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
    val username = "jutiper"
    val password = "jutiper"

    try {
      // make the connection
      Class.forName(driver)

      connection = DriverManager.getConnection(url + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Europe/Berlin", username, password)

      if (debug) {println("DB-Access successful")}
    } catch {
      case e: Throwable => e.printStackTrace()
    }
  }

  def closeDatabase(): Unit = {
    connection.close()
  }

  // maybe delete this
  def insertUser(Username: String, Firstname: String, Surname: String, Birthplace: String, Birthdate: String, Email: String, Password: String): Unit = {
    val statement = connection.createStatement()
    val resultSet = statement.executeUpdate("INSERT INTO user (Username, Firstname, " +
      "Surname, Birthplace, Birthdate, Email, Password) VALUES " +
      "('" + Username + "', '" + Firstname + "', '" + Surname + "', '" + Birthplace + "', '" + Birthdate + "', '" + Email + "', '" + Password + "');")
    if (debug) println("DB-request successfully sent")
  }

  def getUserByUsername(Username: String): Option[User] = {
    try {
      val statement = connection.createStatement()
      val resultSet = statement.executeQuery("SELECT * FROM jutiper.user WHERE Username = '" + Username + "';")

      resultSet.first()
      val Firstname = resultSet.getString("Firstname")
      val Surname = resultSet.getString("Surname")
      val Birthplace = resultSet.getString("Birthplace")
      val Birthdate = resultSet.getString("Birthdate")
      val Email = resultSet.getString("Email")
      val Password = resultSet.getString("Password")

      val user = User(Username, Firstname, Surname, Birthplace, Birthdate, Email, Password)


      if (debug) {
        println("returned user: " + user)
      }
      Some(user)
    } catch {
      case _: SQLException => None
    }
  }

  def processInputLine(input: String): Unit = {
    if (debug) println("processing input ...")
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
    insertUser(infolist(0), infolist(1), infolist(2), infolist(3), infolist(4), infolist(5), infolist(6))

  }

  def processInputLineJson(input: String): Unit = {
    if (debug) println("processing Json input ...")

    val myJson: JsValue = Json.parse(input)

    insertUser((myJson \\ "Username").head.as[String],
      (myJson \\ "Firstname").head.as[String],
      (myJson \\ "Surname").head.as[String],
      (myJson \\ "Birthplace").head.as[String],
      (myJson \\ "Birthdate").head.as[String],
      (myJson \\ "Email").head.as[String],
      (myJson \\ "Password").head.as[String])
  }
}
