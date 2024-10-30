package com.saeedmaldosary.quizapp.controller;

import com.saeedmaldosary.quizapp.model.QuestionWrapper;
import com.saeedmaldosary.quizapp.Response;
import com.saeedmaldosary.quizapp.service.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/quiz")
@Tag(name = "Quiz Controller", description = "APIs for managing quizzes including creation, retrieval, and submission")
public class QuizController {

    @Autowired
    QuizService quizService;

    @Operation(
            summary = "Create a new quiz",
            description = "Creates a quiz with specified number of questions from a category"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Quiz created successfully",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input parameters - empty category/title or invalid number of questions",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No questions found for the given category",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))
            )
    })
    @PostMapping("create")
    public ResponseEntity<String> createQuiz(
            @Parameter(description = "Category of questions", required = true, example = "Java")
            @RequestParam String category,

            @Parameter(description = "Number of questions in the quiz", required = true, example = "5")
            @RequestParam int numQ,

            @Parameter(description = "Title of the quiz", required = true, example = "Java Basics Quiz")
            @RequestParam String title
    ) {
        return quizService.createQuiz(category, numQ, title);
    }

    @Operation(
            summary = "Get quiz questions",
            description = "Retrieves all questions for a specific quiz by ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved quiz questions",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(type = "array", implementation = QuestionWrapper.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid quiz ID",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Quiz not found",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content
            )
    })
    @GetMapping("get/{id}")
    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(
            @Parameter(description = "ID of the quiz to retrieve", required = true, example = "1")
            @PathVariable Integer id
    ) {
        return quizService.getQuizQuestions(id);
    }

    @Operation(
            summary = "Submit quiz answers",
            description = "Submit answers for a quiz and get the score"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully calculated quiz result",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(type = "integer", example = "4", description = "Number of correct answers")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid quiz ID or response format",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Quiz not found",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content
            )
    })
    @PostMapping("submit/{id}")
    public ResponseEntity<Integer> submitQuiz(
            @Parameter(description = "ID of the quiz being submitted", required = true, example = "1")
            @PathVariable Integer id,

            @Parameter(description = "List of responses for quiz questions", required = true)
            @RequestBody List<Response> responses
    ) {
        return quizService.calculateResult(id, responses);
    }
}