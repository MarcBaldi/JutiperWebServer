import api.HttpServer

import scala.io.StdIn
import controller._

object JutiperServerApp {

  def main(args: Array[String]): Unit = {

    val con = new Controller()
    val certCon = new CertificateController()

    val webServer = new HttpServer(con, certCon)
    webServer.connectDatabase()
    println(s"Server online at http://0.0.0.0:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    webServer.closeDatabase()
    webServer.unbind()
  }
}
