# Quiz App Documentation

This application provides a REST API for creating, managing, and taking quizzes.

## Inputs

### Create Quiz

* **category:** The category of questions for the quiz (e.g., "Java").
* **numQ:** The number of questions in the quiz.
* **title:** The title of the quiz.

### Get Quiz Questions

* **id:** The ID of the quiz.

### Submit Quiz

* **id:** The ID of the quiz.
* **responses:** A list of `Response` objects, each containing:
    * **id:** The ID of the question.
    * **response:** The user's answer to the question.

### Add Question
* **question:** A question object with below fields:
    * **id:** question id.
    * **category:** question category
    * **difficultylevel:** question difficulty
    * **option1:** option 1
    * **option2:** option 2
    * **option3:** option 3
    * **option4:** option 4
    * **questionTitle:** question title
    * **rightAnswer:** correct answer
## Outputs

### Create Quiz

* **201 Created:** Quiz created successfully.
* **400 Bad Request:** Invalid input parameters.
* **404 Not Found:** No questions found for the given category.
* **500 Internal Server Error:** An error occurred while creating the quiz.

### Get Quiz Questions

* **200 OK:** A list of `QuestionWrapper` objects, each containing:
    * **id:** The ID of the question.
    * **questionTitle:** The text of the question.
    * **option1:** Option 1.
    * **option2:** Option 2.
    * **option3:** Option 3.
    * **option4:** Option 4.
* **400 Bad Request:** Invalid quiz ID.
* **404 Not Found:** Quiz not found.
* **500 Internal Server Error:** An error occurred while retrieving the quiz questions.

### Submit Quiz

* **200 OK:** The number of correct answers.
* **400 Bad Request:** Invalid quiz ID or response format.
* **404 Not Found:** Quiz not found.
* **500 Internal Server Error:** An error occurred while calculating the result.

### Add Question
* **201 Created:** Question successfully added.
* **500 Internal Server Error:** Question cannot be added.

### Get Questions By Category
* **200 OK:** Questions based on provided category.
* **204 No Content:** No questions found for the specified category.
* **500 Internal Server Error:** An error occurred while retrieving questions.

### Get All Questions
* **200 OK:** All questions.
* **500 Internal Server Error:** An error occurred while retrieving questions.
