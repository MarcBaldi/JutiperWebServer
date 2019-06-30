import api.HttpServer

import scala.io.StdIn
import io.github.cloudify.scala.spdf._
import java.io._
import java.net.URL

import model.User
import model.Mail
import controllor.Controller
import model.Mail.{Mail, send}

import java.nio.file

object JutiperServerApp {

  def main(args: Array[String]): Unit = {

    val user = User("Shohrukh", "Shohrukh", "Koyirov", "Uzbekistan", "05.01.1992", "shkoyiro@htwg-konstanz.de", "jutiper2019")

    val con = new Controller()
    //con.createCertificate(user)
    //con.sendCertificate()



    val webserver = new HttpServer(con)
    webserver.connectDatabase()
    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    webserver.closeDatabase()
    webserver.unbind()
  }

}