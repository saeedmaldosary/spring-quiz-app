package com.saeedmaldosary.quizapp.controller;

import com.saeedmaldosary.quizapp.model.Question;
import com.saeedmaldosary.quizapp.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/v1/question")
@Tag(name = "Question Controller", description = "APIs for managing quiz questions")
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @Operation(
            summary = "Get all questions",
            description = "Retrieves a list of all available quiz questions"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved all questions",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Question.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error occurred",
                    content = @Content
            )
    })
    @GetMapping("all")
    public ResponseEntity<List<Question>> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    @Operation(
            summary = "Get questions by category",
            description = "Retrieves all questions belonging to a specific category"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved questions for the category",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Question.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "No questions found for the specified category",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error occurred",
                    content = @Content
            )
    })
    @GetMapping("category/{category}")
    public ResponseEntity<List<Question>> getQuestionsByCategory(
            @Parameter(description = "Category name to filter questions", required = true)
            @PathVariable String category
    ) {
        return questionService.getQuestionsByCategory(category);
    }

    @Operation(
            summary = "Add a new question",
            description = "Creates a new quiz question in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Question successfully created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error occurred while adding the question",
                    content = @Content
            )
    })
    @PostMapping("add")
    public ResponseEntity<String> addQuestion(
            @Parameter(
                    description = "Question object to be added",
                    required = true,
                    schema = @Schema(implementation = Question.class)
            )
            @RequestBody Question question
    ) {
        return questionService.addQuestion(question);
    }
}