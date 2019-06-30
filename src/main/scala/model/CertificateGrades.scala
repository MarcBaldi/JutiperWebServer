package model

// used to store the separate and total grades of a course.
final case class CertificateGrades (totalTotal: Double, grades: Map[String, Double] )
