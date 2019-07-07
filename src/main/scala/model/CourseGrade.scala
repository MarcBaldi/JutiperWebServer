package model

// questions is a map of string-option pairs. the string is the question name- e.g. q1 and in the option
// is the grade of that question, if it has been saved - else None
final case class CourseGrade (CourseName: String, questions: Map[String, Option[Int]] ){

  def calcTotal : Double = {
    var result = 0

    for ((k,v) <- questions) {
      //printf("DEBUG: key: %s, value: %s\n", k, v)

      v match {
        case None =>
          //println("DEBUG: Problem: v ist none")
          // should be handled by checkQuestions

        ;
        case Some(x) =>
          //println("DEBUG: v ist : ", x)
          // calculation
          if (x == 1) {
            result = result + (100 / questions.size)
          }
        ;
      }
    }

    result
  }

  // checks if any of the questions is not answered. returns None if all are answered, or Some(questionname) if it found one.
  def checkQuestions(): Option[String] = {
    for (q <- questions) {
      q._2 match{
        case None => return Some(q._1)
        case Some(_) =>
      }
    }
    None
  }

}
