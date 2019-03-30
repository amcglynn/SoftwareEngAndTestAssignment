# Assignment
Build a comprehensive test suite that verifies the behaviour of a class within this project.

## Project Structure
This project contains code for a video rental application. It contains the various classes
* Film: contains information about a film
* FilmRepository: an interface for film repositories.
* InMemoryFilmRepository: A demo film repository that stores films in a map in memory
* FilmService: contains main service logic for the application. This class needs to be tested.
* FilmApplication: application used for demonstration only
* FilmNotFoundException: Exception which should be thrown when a film is not found in the repository
* NotInStockException: Exception that should be thrown when a film is not in stock

## Requirements
* Your task is to build out unit tests for FilmService.java.
* Unit tests must use the JUnit framework V4.12.
* FilmService contains dependencies object(s). To make the tests reliable, you must use test mocks to mock out behaviour on the dependencies. 
* Mockito V2.23.4 must be used as the mocking framework to mock dependencies.
* A comprehensive test suite for the FilmService class must be developed. This must ensure all code paths are tested.
* No changes must be made to the code in the src/main/java directory
* The tests must run when running the command 'mvn test'

## Submission
* Assignment must be committed and pushed to a fork on Github.
* A Pull Request (PR) must be opened from the fork to the origin repository.
* Your name and college ID must be added to the description in the PR.
* Submission date is 15th of April, 2019.
 
