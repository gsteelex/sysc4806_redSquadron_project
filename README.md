# sysc4806_redSquadron_project [![Build Status](https://travis-ci.org/gsteelex/sysc4806_redSquadron_project.svg?branch=master)](https://travis-ci.org/gsteelex/sysc4806_redSquadron_project)
SYSC 4806 Project - Red Squadron Team 

[Health Check Link](https://sysc4806-red-squadron-project.herokuapp.com/healthCheck)

[Heroku App Link](https://sysc4806-red-squadron-project.herokuapp.com/healthCheck)


## Summary
TO BE ADDED

## Application Endpoints
Base URL: [https://sysc4806-red-squadron-project.herokuapp.com](https://sysc4806-red-squadron-project.herokuapp.com)

|Request Method|Endpoint|
|-------------|-------------|
|GET	| /programs |
|POST	|	/programs	|
|GET	|	/programs/id	|
|UPDATE	|	/programs/id	|
|DELETE	|	/programs/id	|
|POST	|	/programs/id/courses |
|GET	| 	/programs/id/courses	|
|POST	|	/programs/id/courses	|
|UPDATE	|	/programs/id/courses/id	|
|DELETE	|	/programs/id/courses/id	|
|GET	|	/categories	|
|POST	|	/categories	|
|GET	|	/categories/id	|
|UPDATE	|	/categories/id	|
|DELETE	|	/categories/id	|
|GET	|	/categories/id/learningOutcomes	|
|POST	|	/categories/id/learningOutcome	|
|GET	|	/categories/id/learningOutcomes/id	|
|UPDATE	|	/categories/id/learningOutcomes/id	|
|DELETE	|	/categories/idlearningOutcomes/id	|

## Product Backlog for this Iteration
The project has been newly created with a healthcheck endpoint operational, unit tests, integration tests, and a continuous integration deployment pipeline. 

To be completed this iteration:
* Define endpoints as a team :heavy_check_mark:
* Commit initial UML diagram
* Create model objects for shared use and modification
* Create Endpoints under "/programs" with unit and integration tests
* Create Endpoints under "/categories" with unit and integration tests
