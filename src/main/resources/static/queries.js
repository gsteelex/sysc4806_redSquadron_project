var EMPTY_HTML = '';
var PROGRAM_SELECT_ID = '#programSelect';
var CATEGORY_SELECT_ID = '#categorySelect';
var COURSE_SELECT_ID = '#courseSelect';
var PROGRAM_YEAR_ID = '#programYear';
var QUERY_RESULT_TABLE = '#queryResult';

var PROGRAMS_BASE_PATH = '/programs';
var COURSES_BASE_PATH = '/courses';
var CATEGORY_BASE_PATH = '/categories';


var populateProgramToQueryList = () => {
    $(PROGRAM_SELECT_ID).html(EMPTY_HTML);

    $.get(PROGRAMS_BASE_PATH, (programs) => {
        programs.forEach((program) => {
            $(PROGRAM_SELECT_ID).append($('<option value="' + program.id + '">' + program.id + ': ' + program.name + '</option>'));
        });

        populateCourseToQueryList();
    });
};

var populateCategoryToQueryList = () => {
    $(CATEGORY_SELECT_ID).html(EMPTY_HTML);

    $.get(CATEGORY_BASE_PATH, (categories) => {
        categories.forEach((category) => {
            $(CATEGORY_SELECT_ID).append($('<option value="' + category.id + '">' + category.id + ': ' + category.name + '</option>'));
        });
    });
};

var populateCourseToQueryList = () => {
    $(COURSE_SELECT_ID).html(EMPTY_HTML);

    var programId = +$(PROGRAM_SELECT_ID).val();       //auto converts to a number when adding the "+" sign
    $.get(PROGRAMS_BASE_PATH + '/' + programId, (program) => {
        program.courses.forEach((course) => {
            $(COURSE_SELECT_ID).append($('<option value="' + course.id + '">' + course.id + ': ' + course.name + '</option>'));
        });
    });
};

var queryCoursesInProgramYear = () => {
    var programYear = +$(PROGRAM_YEAR_ID).val();    //auto converts to a number when adding the "+" sign
    var programId = +$(PROGRAM_SELECT_ID).val();       //auto converts to a number when adding the "+" sign
    var tableHeadings = ['Program', 'Program ID', 'Year', 'Course', 'Learning Outcomes'];

    $.get(PROGRAMS_BASE_PATH + '/' + programId, (program) => {
        var programsInYear = [];


        program.courses.forEach((course) => {
            //if the course is from the corresponding project year it needs to be displayed
            if (course.year === programYear) {
                //get data of course
                var data = [];
                data.push(program.name);
                data.push(program.id);
                data.push(course.year);
                data.push(course.name);

                var learningOutcomes = course.learningOutcomes.map(outcome => outcome.name);

                data.push(learningOutcomes.join('; '));

                //add to list of courses
                programsInYear.push(data);
            }
        });

        displayTable(tableHeadings, programsInYear);
    });
};

var queryLearningOutcomesOfProgramYear = () => {

    var programYear = +$(PROGRAM_YEAR_ID).val();    //auto converts to a number when adding the "+" sign
        var programId = +$(PROGRAM_SELECT_ID).val();       //auto converts to a number when adding the "+" sign
        var tableHeadings = ['Program', 'Program ID', 'Year', 'Learning Outcome', 'Learning Outcome ID', 'Category ID'];

        $.get(PROGRAMS_BASE_PATH + '/' + programId, (program) => {
            var learningOutcomesInYear = [];

            program.courses.forEach((course) => {

                //if the course is from the corresponding project year it needs to be displayed
                if (course.year === programYear) {

                    course.learningOutcomes.forEach((outcome) => {
                        //get data of learning outcome
                        var data = [];
                        data.push(program.name);
                        data.push(program.id);
                        data.push(course.year);
                        data.push(outcome.name);
                        data.push(outcome.id);
                        data.push(outcome.category);

                        //add to list of courses
                        learningOutcomesInYear.push(data);
                    });
                }
            });

            displayTable(tableHeadings, learningOutcomesInYear);
        });
};


var queryCoursesOfCategory = () => {
    //internally scoped function to check if the course in question has a learning outcome of desired category
    var doesCourseHaveCategory = (course, categoryId) => {
        var found = false;

        course.learningOutcomes.forEach((outcome) => {
            if (outcome.category === categoryId) {
                found = true;
            }
        })

        return found;
    };

    var programYear = +$(PROGRAM_YEAR_ID).val();        //auto converts to a number when adding the "+" sign
    var programId = +$(PROGRAM_SELECT_ID).val();        //auto converts to a number when adding the "+" sign
    var categoryId = +$(CATEGORY_SELECT_ID).val();      //auto converts to a number when adding the "+" sign
    var tableHeadings = ['Program', 'Program ID', 'Year', 'Category ID', 'Course', 'Course ID'];

    //get details under specific program
    $.get(PROGRAMS_BASE_PATH + '/' + programId, (program) => {
        var coursesWithCategory = [];

        program.courses.forEach((course) => {
            if (course.year === programYear && doesCourseHaveCategory(course, categoryId) === true) {
                var data = [];

                data.push(program.name);
                data.push(program.id);
                data.push(course.year);
                data.push(categoryId);
                data.push(course.name);
                data.push(course.id);

                coursesWithCategory.push(data);
            }
        });

        displayTable(tableHeadings, coursesWithCategory);
    });
};

//data is a 2d array, rows and items desired in each table column corresponding to the row
var displayTable = (headerArray, data) => {
    $(QUERY_RESULT_TABLE).html(EMPTY_HTML);

    //create and append header
    var tableHeader = $('<tr></tr>');
    headerArray.forEach((header) => {
        tableHeader.append($('<th>' + header + '</th>'));
    });
    $(QUERY_RESULT_TABLE).append(tableHeader);

    //create table rows and append them
    data.forEach((row) => {
        var tr = $('<tr></tr>');

        row.forEach((col) => {
            tr.append($('<td>' + col + '</td>'))
        });

        $(QUERY_RESULT_TABLE).find('tbody').append(tr);

        var csvData = headerArray.join();
        data.forEach((row) => {
            csvData += '\n' + row.join();
        });

        //create export button
        var exportButton = $('<button><a download="table.csv" href="data:text/csv;charset=UTF-8,' + encodeURIComponent(csvData) + '">Download Table as CSV</a></button>');
        $('#export').html(EMPTY_HTML);
        $('#export').append(exportButton);
    });
};


var queryCategoriesOfProgramYear = () => {
    var programYear = +$(PROGRAM_YEAR_ID).val();        //auto converts to a number when adding the "+" sign
    var programId = +$(PROGRAM_SELECT_ID).val();        //auto converts to a number when adding the "+" sign
    var tableHeadings = ['Program', 'Program ID', 'Year', 'Category', 'Category ID'];


    //get details under specific program
        $.get(PROGRAMS_BASE_PATH + '/' + programId, (program) => {
            var categoriesOfYear = [];

            program.courses.forEach((course) => {
                course.learningOutcomes.forEach((outcome) => {
                    // check if the category has already been counted
                    if ($.inArray(outcome.category, categoriesOfYear) < 0) {
                        categoriesOfYear.push(outcome.category);
                    }
                });
            });

            $.get(CATEGORY_BASE_PATH, (categories) => {
                var categoryList = [];

                categories.forEach((category) => {
                    if ($.inArray(category.id, categoriesOfYear) >= 0) {
                        var data = [];

                        data.push(program.name);
                        data.push(program.id);
                        data.push(programYear);
                        data.push(category.name);
                        data.push(category.id);

                        categoryList.push(data);
                    }
                });

                displayTable(tableHeadings, categoryList);
            });
        });
};

var queryLearningOutcomesOfCategoryInProgramYear = () => {
    var programYear = +$(PROGRAM_YEAR_ID).val();    //auto converts to a number when adding the "+" sign
    var programId = +$(PROGRAM_SELECT_ID).val();       //auto converts to a number when adding the "+" sign
    var categoryId = +$(CATEGORY_SELECT_ID).val();      //auto converts to a number when adding the "+" sign

    var tableHeadings = ['Program', 'Program ID', 'Year', 'Learning Outcome', 'Learning Outcome ID', 'Category ID'];

    $.get(PROGRAMS_BASE_PATH + '/' + programId, (program) => {
        var learningOutcomesInYear = [];

        program.courses.forEach((course) => {

            //if the course is from the corresponding project year it needs to be displayed
            if (course.year === programYear) {

                course.learningOutcomes.forEach((outcome) => {

                    if (outcome.category === categoryId) {
                        //get data of learning outcome
                        var data = [];
                        data.push(program.name);
                        data.push(program.id);
                        data.push(course.year);
                        data.push(outcome.name);
                        data.push(outcome.id);
                        data.push(outcome.category);

                        //add to list of courses
                        learningOutcomesInYear.push(data);
                    }
                });
            }
        });

        displayTable(tableHeadings, learningOutcomesInYear);
    });
};


var queryLearningOutcomesOfCourseInGivenProgramYear = () => {
    var programId = +$(PROGRAM_SELECT_ID).val();        //auto converts to a number when adding the "+" sign
    var courseId = +$(COURSE_SELECT_ID).val();          //auto converts to a number when adding the "+" sign
    var tableHeadings = ['Program ID', 'Year', 'Course', 'Course ID', 'Learning Outcome', 'Learning Outcome ID'];

    $.get(COURSES_BASE_PATH + '/' + courseId, (course) => {
        var learningOutcomesInCourse = [];

        course.learningOutcomes.forEach((outcome) => {
            var data = [];
            data.push(programId);
            data.push(course.year);
            data.push(course.name);
            data.push(course.id);
            data.push(outcome.name);
            data.push(outcome.id);

            learningOutcomesInCourse.push(data);
        });

        displayTable(tableHeadings, learningOutcomesInCourse);
    });
};


var setUp = () => {
    populateProgramToQueryList();
    populateCategoryToQueryList();

    $(PROGRAM_SELECT_ID).change(populateCourseToQueryList);

    $('#coursesInProgramYear').click(queryCoursesInProgramYear);
    $('#listLearningOutcomesOfProgramYear').click(queryLearningOutcomesOfProgramYear);
    $('#listCourseOfCategory').click(queryCoursesOfCategory);

    $('#categoriesOfProgramYear').click(queryCategoriesOfProgramYear);
    $('#learningOutcomesOfCategoryInProgramYear').click(queryLearningOutcomesOfCategoryInProgramYear);
    $('#learningOutcomesOfCourseInGivenProgramYear').click(queryLearningOutcomesOfCourseInGivenProgramYear);
};

$(setUp);