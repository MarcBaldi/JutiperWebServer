package api

import java.sql.{Connection, DriverManager, SQLException}
import java.text.SimpleDateFormat
import java.util.Calendar

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import controllor.Controller
import play.api.libs.json._
import model.{CertificateGrades, CourseGrade, User}

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.collection.mutable.ListBuffer
class HttpServer(con: Controller) {

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
              case Some(user) =>
                complete(HttpEntity(ContentTypes.`application/json`, userToJson(user).toString()))
              case None =>
                complete(HttpEntity(ContentTypes.`application/json`, errorToJson("Username does not exist").toString()))
            }
          }
        } ~
        // simple query of a user to database
        path("login"/ Segment) { command =>
        {
          complete(HttpEntity(ContentTypes.`application/json`, login(command).toString()))
        }
        } ~
        // simple insertion of a user to database - maybe delete this!
      // TODO : DEPRECATED
        path("putUser" / Segment) { command => {processInputLine(command)}
          complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, "<h1>Transmitted Successfully</h1>"))
        } ~
        // simple insertion of a user to database aka a simple REGISTER
        path("putUserJson" / Segment) { command => {
          processInputLineJson(command)
        }
          complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, "<h1>Transmitted Successfully</h1>"))
        } ~
        // simple insertion of an Erg to database
        path("putErgJson" / Segment) { command => {
          processInputLineErgJson(command)
        }
          complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, "<h1>Transmitted Successfully</h1>"))
        } ~
        path("sendCertificate"/ Segment) { command =>
        {
          complete(HttpEntity(ContentTypes.`application/json`, sendCertificate(command).toString()))
        }
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

  def insertUser(Username: String, Firstname: String, Surname: String, Birthplace: String, Birthdate: String, Email: String, Password: String): Unit = {
    val statement = connection.createStatement()
    statement.executeUpdate("INSERT INTO user (Username, Firstname, " +
      "Surname, Birthplace, Birthdate, Email, Password) VALUES " +
      "('" + Username + "', '" + Firstname + "', '" + Surname + "', '" + Birthplace + "', '" + Birthdate + "', '" + Email + "', '" + Password + "');")
    if (debug) println("DB-request. successfully INSERTED")
  }

  // TODO: maybe make all var names english
  def insertErg(UserName: String, VersuchNr: Int, KursName: String, AufgabeNr: Int, Erg: Int): Unit = {
    val statement = connection.createStatement()
    statement.executeUpdate("UPDATE " + KursName +
      " SET q" + AufgabeNr +" = '" + Erg +
      "' WHERE userFK = '" + UserName + "' AND versuchNr = '" + VersuchNr + "';")
    if (debug) println("DB-request. successfully UPDATED")
    if (debug) println("DEBUG: "+ "UPDATE " + KursName +
      " SET q" + AufgabeNr +" = '" + Erg +
      "' WHERE userFK = '" + UserName + "' AND versuchNr = '" + VersuchNr + "';")
  }

  // TODO;
  def insertVersuchNr(UserName: String){

    val statement = connection.createStatement()
    statement.executeUpdate("INSERT INTO 01_geschichte (userFK, versuchNr) " +
      "VALUES " + "('" + UserName + "', 1);")
    statement.executeUpdate("INSERT INTO 02_belichtung (userFK, versuchNr) " +
      "VALUES " + "('" + UserName + "', 1);")
    statement.executeUpdate("INSERT INTO 03_blende (userFK, versuchNr) " +
      "VALUES " + "('" + UserName + "', 1);")
    statement.executeUpdate("INSERT INTO 04_iso (userFK, versuchNr) " +
      "VALUES " + "('" + UserName + "', 1);")
    statement.executeUpdate("INSERT INTO 05_studiolicht (userFK, versuchNr) " +
      "VALUES " + "('" + UserName + "', 1);")
    statement.executeUpdate("INSERT INTO 06_blitz (userFK, versuchNr) " +
      "VALUES " + "('" + UserName + "', 1);")
    statement.executeUpdate("INSERT INTO 07_komposition (userFK, versuchNr) " +
      "VALUES " + "('" + UserName + "', 1);")
    statement.executeUpdate("INSERT INTO 08_objektive (userFK, versuchNr) " +
      "VALUES " + "('" + UserName + "', 1);")
    statement.executeUpdate("INSERT INTO 09_perspektive (userFK, versuchNr) " +
      "VALUES " + "('" + UserName + "', 1);")
    statement.executeUpdate("INSERT INTO 10_portrait (userFK, versuchNr) " +
      "VALUES " + "('" + UserName + "', 1);")
    if (debug) println("DB-request. successfully MASS INSERTED")

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

  //login
  def login(input: String): JsObject = {


    val myJson: JsValue = Json.parse(input)
    val username = (myJson \\ "Username").head.as[String]
    val password = (myJson \\ "Password").head.as[String]

    if (debug) {println(username + " tries to login")}

    try {
      val statement = connection.createStatement()
      val resultSet = statement.executeQuery("SELECT * FROM jutiper.user WHERE Username = '" + username + "' AND Password = '" + password + "';")
      resultSet.first()
      if (debug) {println("DEBUG: "+ password)}
      if (debug) {println("DEBUG: "+ input)}
      //if (debug) {println("DEBUG: "+ resultSet.)}

      if (resultSet.getString("Username")== username ){
        if (debug) {println("it was successful")}
        Json.obj(
          "status" -> "true"
        )
      }else{
        if (debug) {println("it was not successful")}
        Json.obj(
          "status" -> "false"
        )
      }
    } catch {
      case _: SQLException => Json.obj(
        "status" -> "false"
      )
    }
  }


  //sendCertificate
  def sendCertificate(input: String): JsObject = {

    if (debug) {println("DEBUG : "+ input)}

    val myJson: JsValue = Json.parse(input)
    val username = (myJson \\ "Username").head.as[String]

    if (debug) {println(username + " tries to send certificate")}

    try {
      val statement = connection.createStatement()
      var resultSet = statement.executeQuery("SELECT g.userFK, versuchNr FROM jutiper.01_geschichte as g " +
        "INNER JOIN versuchnr_max_werte AS m ON g.versuchnr = m.max_v " +
        "AND g.userFK = m.userFK AND g.userFK = '" + username + "';")
      resultSet.first()
      if (debug) {println("DEBUG versNr: "+ resultSet.getString("versuchNr"))}

      val vers_nr = resultSet.getString("versuchNr")

      // TODO: big query

      // TODO: make to map
      // maybe make a call "show tables;" and filter the other 3 tables out.
      val courseNames = List("01_geschichte", "02_belichtung", "03_blende", "04_iso", "05_studiolicht", "06_blitz", "07_komposition", "08_objektive", "09_perspektive", "10_portrait")
      // questionCount(0) is not a course (placeholder)
      val questionCount = List(5,5,2,4,1,3,7,5,9,5)

      var allCourses = new ListBuffer[CourseGrade]

      for (courseNr <- 0 to courseNames.length-1) {

        resultSet = statement.executeQuery("SELECT * FROM jutiper." + courseNames(courseNr) + " WHERE userFK = '" + username + "' AND versuchnr = '" + vers_nr + "';")
        resultSet.first()

        //if (debug) {println("DEBUG 01_g: q2: "+ resultSet.getString("q2"))}



        var map_01: Map[String, Option[Int]] = Map()
        var grade_opt: Option[Int] = None

        for (questionNr <- 1 to questionCount(courseNr)) {
          if (debug) {
            println("DEBUG counting to : " + questionNr+ " with max: "+ questionCount(courseNr))
          }
          val grade_res = resultSet.getString("q" + questionNr)

          if (grade_res == null) {
            grade_opt = None
          } else {
            // TODO: errorhandling

            grade_opt = Some(grade_res.toInt)
          }


          map_01 = map_01 + ("q" + questionNr -> grade_opt)
        }
        allCourses += CourseGrade(courseNames(courseNr), map_01)

      }



      if (debug) {println("DEBUG allCourses : "+ allCourses)}
      if (debug) {println("DEBUG one course : "+ allCourses(1).calcTotal)}
      // gesamtGesamt berechnen ...
      //allCourses(0).questions.("01_geschichte").get

      var myTotalTotal = 0.0

      var certificateMap = Map[String, Double]()

      for (myCourse <- allCourses) {
        myCourse.checkQuestions() match {
          case None =>
          case Some(x) => {
            // At least one question was not answered. return the missing answer.
            //TODO: return all missing answers.
            if (debug) {println("DEBUG returned early due to course: "+ myCourse.CourseName + " and questionNr :"+ x)}
            return Json.obj(
              "status" -> "false", "message" -> "Some questions are missing!", "course" -> myCourse.CourseName, "question" -> x
            )
          }
        }

        val myTotal = myCourse.calcTotal
        certificateMap += (myCourse.CourseName -> convertGrade(myTotal))

        if (debug) println("DEBUG: "+myCourse.CourseName +" : "+convertGrade(myTotal))
        myTotalTotal += myTotal
      }
      myTotalTotal = myTotalTotal / allCourses.size
      if (debug) println("DEBUG: total: "+ convertGrade(myTotalTotal))


      // update result tabelle
      resultSet = statement.executeQuery("SELECT max_c FROM jutiper.certnr_max_werte;")

      resultSet.first()
      val cert_nr = resultSet.getString("max_c")
      val newCertNr = cert_nr.toInt + 1
      if (debug) {println("DEBUG certNr: "+ newCertNr)}

      val format = new SimpleDateFormat("yyyy-MM-dd")
      val currentDate = format.format(Calendar.getInstance().getTime)

      statement.executeUpdate("INSERT INTO `jutiper`.`result` (`certNr`,`userFK`,`date`,`score`) " +
        "VALUES ("+ newCertNr +",'"+username+"','"+currentDate+"',"+convertGrade(myTotalTotal)+");")



      // ---- certificate stuff -----
      val certificateGra = CertificateGrades(convertGrade(myTotalTotal), certificateMap)

      getUserByUsername(username) match {
        case Some(user) => con.createCertificate(user, certificateGra)
        case None =>{
          // certificate needs a valid user. if there is no user, return with the error
          if (debug) {println("DEBUG returned early due to user: "+ username + " not found")}
          return Json.obj(
            "status" -> "false", "message" -> "Some users are missing!"
          )
        }
      }



      if (debug) {println("DEBUG: successful message sent")}
      Json.obj(
        "status" -> "true", "message" -> "Congratulations! Certificate was sent."
      )


    } catch {
      case _: SQLException => Json.obj(
        "status" -> "false", "message" -> "sorry, SQL error. Check back with the devs."
      )
    }
  }

  //converts a percentage to a school-like grade
  private def convertGrade(myTotal: Double): Double ={

    val note = myTotal match {
      case i if i <= 100 && i > 94.4 => 1.0
      case i if i <= 94.4 && i > 88.8 => 1.3
      case i if i <= 88.8 && i > 83.2 => 1.7
      case i if i <= 83.2 && i > 77.6 => 2.0
      case i if i <= 77.6 && i > 72.0 => 2.3
      case i if i <= 72.0 && i > 66.4 => 2.7
      case i if i <= 66.4 && i > 60.8 => 3.0
      case i if i <= 60.8 && i > 55.2 => 3.3
      case i if i <= 55.2 && i > 49.6 => 3.7
      case i if i <= 49.6 && i > 44.0 => 4.0
      case _ => 5.0
    }

    //println("calculatin.. total: " + myTotal+ " note: "+ note)
    note
  }


  //maybe delete this
  //TODO: DEPRECATED
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

  //register
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

    // insert first versuchNr on each course
    insertVersuchNr((myJson \\ "Username").head.as[String])
  }


  //update question
  def processInputLineErgJson(input: String): Unit = {
    if (debug) println("processing Erg Json input ...")

    val myJson: JsValue = Json.parse(input)

    insertErg((myJson \\ "Username").head.as[String],
      (myJson \\ "VersuchNr").head.as[Int],
      (myJson \\ "KursName").head.as[String],
      (myJson \\ "AufgabeNr").head.as[Int],
      (myJson \\ "Erg").head.as[Int])
  }
}
