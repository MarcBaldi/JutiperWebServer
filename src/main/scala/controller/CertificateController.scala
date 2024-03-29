package controller

import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import model.Mail.{Mail, send}
import model.{CertificateGrades, User}
import io.github.cloudify.scala.spdf._

class CertificateController {

  val debug = true

  def createCertificate(user: User, certGrades : CertificateGrades): Unit = {

    //function of spdf to prepare transforming of html to a pdf file.
    val pdf = Pdf(new PdfConfig {
      orientation := Portrait
      pageSize := "Letter"
      marginTop := "1in"
      marginBottom := "1in"
      marginLeft := "1in"
      marginRight := "1in"
    })

    val jutiperCertificate = new File("certificates",  "JutiperZertifikat.pdf")
    //val note = 1
    val total = certGrades.totalTotal
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
      "<td><b>Geschichte</b></td> <td>" + certGrades.grades("01_geschichte") + "</td>" +
      "</tr><tr>" +
      "<td><b>Belichtungszeit</b></td> <td>" + certGrades.grades("02_belichtung") + "</td>" +
      "</tr><tr>" +
      "<td><b>Blende</b></td> <td>" + certGrades.grades("03_blende") + "</td>" +
      "</tr><tr>" +
      "<td><b>ISO</b></td> <td>" + certGrades.grades("04_iso") + "</td>" +
      "</tr><tr>" +
      "<td><b>Studiolicht</b></td> <td>" + certGrades.grades("05_studiolicht") + "</td>" +
      "</tr><tr>" +
      "<td><b>Blitz</b></td> <td>" + certGrades.grades("06_blitz") + "</td>" +
      "</tr><tr>" +
      "<td><b>Komposition&Farbkontraste</b></td> <td>" + certGrades.grades("07_komposition") + "</td>" +
      "</tr><tr>" +
      "<td><b>Objektive</b></td> <td>" + certGrades.grades("08_objektive") + "</td>" +
      "</tr><tr>" +
      "<td><b>Perspektive</b></td> <td>" + certGrades.grades("09_perspektive") + "</td>" +
      "</tr><tr>" +
      "<td><b>Portrait</b></td> <td>" + certGrades.grades("10_portrait") + "</td>" +
      "</tr>" +
      "</table>" +
      "<h2>Gesamt " + "  " + total + "</h2>" +
      "<br/>" +
      "<br/>" +
      "<br/>" +
      "<br/>" +
      "<br/>" +
      "<h3>TeilnehmerNr. 175786 </h3>" +
      "<h3>Digital Signatur</h3>" +
      "<h3>Konstanz, " + currentDate + "</h3>" +
      "</body></html>"

    // wishlist: jpg support could be appreciated
    // "<img src=\"file://C:/Users/Marcimio/IdeaProjects/JutiperServer/assets/Juti.png\" style=\"width:177px;height:106px;\">" +

    pdf.run(page, jutiperCertificate)
    sendCertificate(user, jutiperCertificate)
  }

  def sendCertificate(user: User, jutiperCertificate: File):Unit = {
    if (debug) println("DEBUG: sending to: " + user.Email)

    send a Mail (
      from = ("JutiperDev@gmail.com", "Jutiper Dev"),
      to = Seq(user.Email),
      //bcc = Seq("other@email.example"),
      subject = "Jutiper Zertifikat",
      message = "Gratulation :)" ,
      attachment = Some(jutiperCertificate)
    )
  }
}
