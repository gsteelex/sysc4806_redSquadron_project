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
    //TODO
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
    console.log(headerArray);
    console.log(data);
    //TODO
};

var setUp = () => {
    populateProgramToQueryList();
    populateCourseToQueryList();

    $('#coursesInProgramYear').click(queryCoursesInProgramYear);
    $('#listLearningOutcomesOfProgramYear').click(queryLearningOutcomesOfProgramYear);
    $('#listCourseOfCategory').click(queryCoursesInProgramYear);
};

$(setUp);