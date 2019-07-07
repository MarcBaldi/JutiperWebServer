package controller

import model.User
import play.api.libs.json.{JsObject, Json}

class Controller() {

  val debug = true



  def errorToJson(message: String): JsObject = {
    val myJson = Json.obj(
      "status" -> "error",
      "message" -> message
    )

    if (debug) {println("converted to json: "+ myJson)}
    myJson
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



  //converts a percentage to a school-like grade
  def convertGrade(myTotal: Double): Double ={

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



}
