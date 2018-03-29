var EMPTY_HTML = '';
var PROGRAM_SELECT_ID = '#programSelect';
var CATEGORY_SELECT_ID = '#categorySelect';
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
    });
};

var populateCourseToQueryList = () => {
    $(CATEGORY_SELECT_ID).html(EMPTY_HTML);

    $.get(CATEGORY_BASE_PATH, (categories) => {
        categories.forEach((category) => {
            $(CATEGORY_SELECT_ID).append($('<option value="' + category.id + '">' + category.id + ': ' + category.name + '</option>'));
        });
    });
};

var queryCoursesInProgramYear = () => {
    console.log('to be implemented 1');
    var programYear = +$(PROGRAM_YEAR_ID).val();    //auto converts to a number when adding the "+" sign
    var programId =+$(PROGRAM_SELECT_ID).val();       //auto converts to a number when adding the "+" sign
    var tableHeadings = ['Program', 'Program ID', 'Year', 'Course', 'Learning Outcomes'];


    $.get(PROGRAMS_BASE_PATH, (programs) => {
        var programsInYear = [];

        programs.forEach((program) => {
            program.courses.forEach((course) => {

                //if the course is from the corresponding project year it needs to be displayed
                if (course.year === programYear) {
                    //get data of course
                    var data = [];
                    data.push(program.name);
                    data.push(program.id);
                    data.push(course.year);
                    data.push(course.name);
                    data.push(course.learningOutcomes);

                    //add to list of courses
                    programsInYear.push(data);
                }
            });
        });

        displayTable(tableHeadings, programsInYear);
    });
};

var queryLearningOutcomesOfProgramYear = () => {
    console.log('to be implemented 2');
    //TODO
};


var queryCoursesOfCategory = () => {
    console.log('to be implemented 3');
    //TODO
};

//data is a 2d array, rows and items desired in each table column corresponding to the row
var displayTable = (headerArray, data) => {
    console.log(headerArray);   //TODO: remove before PR
    console.log(data);          //TODO: remove before PR

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
    });
};

var setUp = () => {
    populateProgramToQueryList();
    populateCourseToQueryList();

    $('#coursesInProgramYear').click(queryCoursesInProgramYear);
    $('#listLearningOutcomesOfProgramYear').click(queryLearningOutcomesOfProgramYear);
    $('#listCourseOfCategory').click(queryCoursesInProgramYear);
};

$(setUp);