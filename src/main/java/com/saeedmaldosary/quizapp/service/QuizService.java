package com.saeedmaldosary.quizapp.service;

import com.saeedmaldosary.quizapp.model.Question;
import com.saeedmaldosary.quizapp.model.QuestionWrapper;
import com.saeedmaldosary.quizapp.model.Quiz;
import com.saeedmaldosary.quizapp.Response;
import com.saeedmaldosary.quizapp.dao.QuestionDao;
import com.saeedmaldosary.quizapp.dao.QuizDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    @Autowired
    private QuizDao quizDao;

    @Autowired
    private QuestionDao questionDao;

    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {
        try {
            // Validate input parameters
            if (category == null || category.trim().isEmpty()) {
                return new ResponseEntity<>("Category cannot be empty", HttpStatus.BAD_REQUEST);
            }
            if (numQ <= 0) {
                return new ResponseEntity<>("Number of questions must be greater than 0", HttpStatus.BAD_REQUEST);
            }
            if (title == null || title.trim().isEmpty()) {
                return new ResponseEntity<>("Title cannot be empty", HttpStatus.BAD_REQUEST);
            }

            List<Question> questions = questionDao.findRandomQuestionsByCategory(category, numQ);

            if (questions.isEmpty()) {
                return new ResponseEntity<>("No questions found for the given category", HttpStatus.NOT_FOUND);
            }

            if (questions.size() < numQ) {
                return new ResponseEntity<>("Not enough questions available in the category", HttpStatus.BAD_REQUEST);
            }

            Quiz quiz = new Quiz();
            quiz.setTitle(title);
            quiz.setQuestions(questions);
            quizDao.save(quiz);

            return new ResponseEntity<>("Quiz created successfully", HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>("Error creating quiz: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {
        try {
            if (id == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Optional<Quiz> quizOptional = quizDao.findById(id);

            if (quizOptional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            List<Question> questionsFromDB = quizOptional.get().getQuestions();
            List<QuestionWrapper> questionsForUser = new ArrayList<>();

            for (Question q : questionsFromDB) {
                QuestionWrapper qw = new QuestionWrapper(
                        q.getId(),
                        q.getQuestionTitle(),
                        q.getOption1(),
                        q.getOption2(),
                        q.getOption3(),
                        q.getOption4()
                );
                questionsForUser.add(qw);
            }

            return new ResponseEntity<>(questionsForUser, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {
        try {
            if (id == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            if (responses == null || responses.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Optional<Quiz> quizOptional = quizDao.findById(id);

            if (quizOptional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Quiz quiz = quizOptional.get();
            List<Question> questions = quiz.getQuestions();

            // Validate if number of responses matches number of questions
            if (responses.size() != questions.size()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            int right = 0;
            for (int i = 0; i < responses.size(); i++) {
                Response response = responses.get(i);
                if (response.getResponse() != null &&
                        response.getResponse().equals(questions.get(i).getRightAnswer())) {
                    right++;
                }
            }

            return new ResponseEntity<>(right, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}