import api.HttpServer
import scala.io.StdIn

import io.github.cloudify.scala.spdf._
import java.io._
import java.net.URL

import model.User
import model.mail._
import controllor.Controllor

object JutiperServerApp {

//  def main(args: Array[String]): Unit = {
//    val webserver = new HttpServer()
//    webserver.connectDatabase()
//    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
//    StdIn.readLine() // let it run until user presses return
//    webserver.closeDatabase()
//    webserver.unbind()
//  }

  def main(args: Array[String]): Unit = {

    val user = new User("Shohrukh", "Shohrukh", "Koyirov", "Uzbekistan", "05.01.1992", "herr_shoh@mail.ru", "jutiper2019")

    val con = new Controllor()
    con.createCertificate(user)

    send a new Mail (
      from = ("shohrukhkoyirov@gmail.com", "Shohrukh Koyirov"),
      to = "herr_shoh@mail.ru",
      cc = "shkoyiro@htwg-konstanz.de",
      subject = "Jutiper Zertifikat",
      message = "Gratulation..."
    )


//    // Create a new Pdf converter with a custom configuration
//    // run `wkhtmltopdf --extended-help` for a full list of options
//    var pdf = Pdf(new PdfConfig {
//      orientation := Portrait
//      pageSize := "Letter"
//      marginTop := "1in"
//      marginBottom := "1in"
//      marginLeft := "1in"
//      marginRight := "1in"
//    })
//
//    var jutiperZertifikat = new File("JutiperZertifikat.pdf")
//    val myName = "Shohrukh Koyirov"
//    val gesamt = 2;
//
//    val page = "<html><head><justify><h4>Jutiper Virtuell Universitaet</h4></justify></head><br/>" +
//                "<body>" +
//                "<h1><b><center>Zertifikat von Jutiper</center></b></h1>" +
//                  "<b>Herr/Frau</b> " + myName + "<br/>" +
//                  "<b>Geboren am</b>    02.02.1998" + "<br/" +
//                  "<h3><b><center>hat den Jutiper Test </center></b></h3>" +
//                  "<b>am</b>            07.06.2019" + "<br/>" +
//                  "<b>im</b>            Jutiper Virtuell Universitaet" + "<br/>" +
//                  "<br/>" +
//                  "<br/>" +
//                  "<h2><b><center>Mit folgendem Ergebnissen abgelegt</center></b></h2>" +
//                  "<b>Geschichte</b>     1" + "<br/>" +
//                  "<b>Belichtungszeit</b>     1" + "<br/>" +
//                  "<b>Blende</b>     1" + "<br/>" +
//                  "<b>ISO</b>     1" + "<br/>" +
//                  "<b>Studiolicht</b>     1" + "<br/>" +
//                  "<b>Blitz</b>     1" + "<br/>" +
//                  "<b>KompositionFarbkontraste</b>     1" + "<br/>" +
//                  "<b>Objektive</b>     1" + "<br/>" +
//                  "<b>Perspektive</b>     1" + "<br/>" +
//                  "<b>Portrait</b>     1" + "<br/>" +
//                  "<h2>Gesamt " + "  " + gesamt + "</h2>" +
//                  "<br/>" +
//                  "<br/>" +
//                  "<h3>TeilnehmerNr.175786" + "_______________________" + "Konstanz, 07.06.2019</h3>" +
//                  "                                               <h3>Digital Signatur</h3>" +
//                "</body></html>"
//
//
//    // Save the PDF generated from the above HTML into a Byte Array
//    val outputStream = new ByteArrayOutputStream
//    //pdf.run(page, outputStream)
//    pdf.run(page, jutiperZertifikat)
//
//    // Save the PDF of Google's homepage into a file
//    //pdf.run(new URL("http://www.google.com"), new File("google.pdf"))
  }

}