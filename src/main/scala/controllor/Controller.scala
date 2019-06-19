package controllor

import java.io.File

import io.github.cloudify.scala.spdf._
import java.text.SimpleDateFormat
import java.util.Calendar

import model.Mail.{Mail, send}
import model.User

class Controller() {




  //TODO hier kommt noch als zweite Parametr die Noten
  def createCertificate(user: User): Unit = {

    var pdf = Pdf(new PdfConfig {
      orientation := Portrait
      pageSize := "Letter"
      marginTop := "1in"
      marginBottom := "1in"
      marginLeft := "1in"
      marginRight := "1in"
    })

    val jutiperZertifikat = new File("certificates",  "JutiperZertifikat.pdf")
    val note = 1
    val gesamt = 2
    val format = new SimpleDateFormat("dd.MM.yyyy")
    val currentDate = format.format(Calendar.getInstance().getTime)

    val page = "<html><head><h4 align=\"right\">Jutiper Virtuelle Universität</h4></head><br/>" +
      "<body>" +
      "<h1><b><center>Zertifikat von Jutiper</center></b></h1>" +
      "<table >" +
      "<tr>" +
      "<td><b>Name</b></td> <td>" + user.Firstname + " " + user.Surname + "</td>" +
      "</tr><tr>" +
      "<td><b>Geburtsdatum</b></td>  <td>" + user.Birthdate + "</td>" +
      "</tr><tr>" +
      "<td><b>Geburtsort</b></td>  <td>" + user.Birthplace + "</td>" +
      "</tr>" +
      "</table>" +
      "********** <b>hat den Fotokurs Test</b> **********" + "<br/>" +
      "<br/>" +
      "<b>am</b> " + currentDate + "<br/>" +
      "<b>im</b>            Jutiper Virtuelle Universitaet" + "<br/>" +
      "<br/>" +
      "<br/>" +
      "<h2><b><center>Mit folgenden Ergebnissen abgelegt</center></b></h2>" +
      "<table >" +
      "<tr>" +
      "<td><b>Geschichte</b></td> <td>" + note + "</td>" +
      "</tr><tr>" +
      "<td><b>Belichtungszeit</b></td> <td>" + note + "</td>" +
      "</tr><tr>" +
      "<td><b>Blende</b></td> <td>" + note + "</td>" +
      "</tr><tr>" +
      "<td><b>ISO</b></td> <td>" + note + "</td>" +
      "</tr><tr>" +
      "<td><b>Studiolicht</b></td> <td>" + note + "</td>" +
      "</tr><tr>" +
      "<td><b>Blitz</b></td> <td>" + note + "</td>" +
      "</tr><tr>" +
      "<td><b>Komposition&Farbkontraste</b></td> <td>" + note + "</td>" +
      "</tr><tr>" +
      "<td><b>Objektive</b></td> <td>" + note + "</td>" +
      "</tr><tr>" +
      "<td><b>Perspektive</b></td> <td>" + note + "</td>" +
      "</tr><tr>" +
      "<td><b>Portrait</b></td> <td>" + note + "</td>" +
      "</tr>" +
      "</table>" +
      "<h2>Gesamt " + "  " + gesamt + "</h2>" +
      "<br/>" +
      "<br/>" +
      "<br/>" +
      "<br/>" +
      "<br/>" +
      "<h3>TeilnehmerNr. 175786 </h3>" +
      "<h3>Digital Signatur</h3>" +
      "<h3>Konstanz, " + currentDate + "</h3>" +
      "<img src=\"file://C:/Users/Marcimio/IdeaProjects/JutiperServer/assets/Juti.png\" style=\"width:177px;height:106px;\">" +
      "</body></html>"

    // TODO: jpg sollte noch gemacht werden

    pdf.run(page, jutiperZertifikat)
  }

  //TODO hier kommen noch mehr Parameter
  def sendCertificate() = {
    //println("DEBUG: sending to: " + user.Email)
    val certificate = new File("certificates", "JutiperZertifikat.pdf")


    // TODO: richtiger user adresse
    send a new Mail (
      from = ("JutiperDev@gmail.com", "Jutiper Dev"),
      to = Seq("herr_shoh@mail.ru"),
      bcc = Seq("mabaldis@htwg-konstanz.de"),
      subject = "Jutiper Zertifikat",
      message = "Gratulation :)" ,
      attachment = Some(certificate)
    )
  }
}
