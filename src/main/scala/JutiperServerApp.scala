import scala.io.StdIn

object JutiperServerApp {


  def main(args: Array[String]): Unit = {

    val webserver = new HttpServer();
    webserver.connectDatabase()
    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    webserver.closeDatabase()
    webserver.unbind
  }

}