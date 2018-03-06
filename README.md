# sysc4806_redSquadron_project [![Build Status](https://travis-ci.org/gsteelex/sysc4806_redSquadron_project.svg?branch=master)](https://travis-ci.org/gsteelex/sysc4806_redSquadron_project)
SYSC 4806 Project - Red Squadron Team 

[Health Check Link](https://sysc4806-red-squadron-project.herokuapp.com/healthCheck)

[Heroku App Link](https://sysc4806-red-squadron-project.herokuapp.com/healthCheck)


## Summary
A user can track, edit, and update learning outcomes for different programs. Each course has a set of learning objectives in a given year. Each learning objective belongs to a category, and these categories can be edited by the administrator. A course belongs to one or more programs and a program has many courses in a given year.

## Application Endpoints
Base URL: [https://sysc4806-red-squadron-project.herokuapp.com](https://sysc4806-red-squadron-project.herokuapp.com)

|Request Method|Endpoint|
|-------------|-------------|
|GET	| /programs | :heavy_check_mark:
|POST	|	/programs	| :heavy_check_mark:
|GET	|	/programs/id	| :heavy_check_mark:
|PATCH	|	/programs/id	| :heavy_check_mark:
|DELETE	|	/programs/id	| :heavy_check_mark:
|POST	|	/courses | 
|GET	| 	/courses	|
|GET	| 	/courses/id	|
|PATCH	|	/courses/id	|
|DELETE	|	/courses/id	|
|GET	|	/categories	|
|POST	|	/categories	|
|GET	|	/categories/id	|
|PATCH	|	/categories/id	|
|DELETE	|	/categories/id	|
|GET	|	/categories/id/learningOutcomes	|
|POST	|	/categories/id/learningOutcome	|
|GET	|	/categories/id/learningOutcomes/id	|
|PATCH	|	/categories/id/learningOutcomes/id	|
|DELETE	|	/categories/id/learningOutcomes/id	|

## Documentation
UML class diagram runs in [draw.io](https://www.draw.io/)

## Product Backlog for this Iteration
The project has been newly created with a healthcheck endpoint operational, unit tests, integration tests, and a continuous integration deployment pipeline. 

To be completed this iteration (03/08/2018):
* Define endpoints as a team :heavy_check_mark:
* Commit initial UML diagram :pencil2:
* Create model objects for shared use and modification
* Create Endpoints under "/programs" with unit and integration tests :heavy_check_mark:
* Create Endpoints under "/courses" with unit and integration tests (create course)
* Create Endpoints under "/categories" with unit and integration tests
* Create UI page to list all programs with link to view a specific program (display some data, TA approved)

To be completed for next iteration (03/15/2018):
* Create endpoints for courses
* Create endpoints for learning outcomes
* Create courses and programs through GUI
* View courses, programs, categories, and learning outcomes on GUI
