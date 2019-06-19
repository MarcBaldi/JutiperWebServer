package controllor

import java.io.File
import io.github.cloudify.scala.spdf._
import java.io._
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Calendar

import scala.concurrent.{ExecutionContextExecutor, Future}
import model.User

class Controllor() {

  //hier kommt noch als zweite Parametr die Noten
  def createCertificate(user: User): Unit = {

    var pdf = Pdf(new PdfConfig {
      orientation := Portrait
      pageSize := "Letter"
      marginTop := "1in"
      marginBottom := "1in"
      marginLeft := "1in"
      marginRight := "1in"
    })

    var jutiperZertifikat = new File("JutiperZertifikat.pdf")
    val myName = "Shohrukh Koyirov"
    val gesamt = 2;
    val format = new SimpleDateFormat("dd.mm.yyyy")
    val currentDate = format.format(Calendar.getInstance().getTime())

    val page = "<html><head><justify><h4>Jutiper Virtuell Universitaet</h4></justify></head><br/>" +
      "<body>" +
      "<h1><b><center>Zertifikat von Jutiper</center></b></h1>" +
      "<b>Herr/Frau</b> " + user.Firstname + " " + user.Surname + "<br/>" +
      "<b>Geburtsdatum</b> " + user.Birthdate + "<br/>" +
      "<b>Geburtsort</b> " + user.Birthplace + "<br/>" +
      "<br/>" +
      "********** <b>hat den Fotokurs Test</b> **********" + "<br/>" +
      "<br/>" +
      "<b>am</b> " + currentDate + "<br/>" +
      "<b>im</b>            Jutiper Virtuell Universitaet" + "<br/>" +
      "<br/>" +
      "<br/>" +
      "<h2><b><center>Mit folgendem Ergebnissen abgelegt</center></b></h2>" +
      "<b>Geschichte</b>     1" + "<br/>" +
      "<b>Belichtungszeit</b>     1" + "<br/>" +
      "<b>Blende</b>     1" + "<br/>" +
      "<b>ISO</b>     1" + "<br/>" +
      "<b>Studiolicht</b>     1" + "<br/>" +
      "<b>Blitz</b>     1" + "<br/>" +
      "<b>KompositionFarbkontraste</b>     1" + "<br/>" +
      "<b>Objektive</b>     1" + "<br/>" +
      "<b>Perspektive</b>     1" + "<br/>" +
      "<b>Portrait</b>     1" + "<br/>" +
      "<h2>Gesamt " + "  " + gesamt + "</h2>" +
      "<br/>" +
      "<br/>" +
      "<br/>" +
      "<br/>" +
      "<br/>" +
      "<br/>" +
      "<h3>TeilnehmerNr.175786" + "_______________________" + "Konstanz, 07.06.2019</h3>" +
      "                                               <h3>Digital Signatur</h3>" +
      "</body></html>"


    //pdf.run(page, outputStream)
    pdf.run(page, jutiperZertifikat)
    // Save the PDF of Google's homepage into a file
    //pdf.run(new URL("http://www.google.com"), new File("google.pdf"))
  }
}
