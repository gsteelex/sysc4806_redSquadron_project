var EMPTY_HTML = '';
var ALL_COURSES_ID = '#allCourses';
var COURSES_BASE_PATH = '/courses';

var clearCourses = function() {
    $(ALL_COURSES_ID).html(EMPTY_HTML);
};


var displayCourse = function(course) {
    var learningOutcomes = course.learningOutcomes.map(outcome => outcome.name);

    //TODO: this will be come back to one the courses can have learning outcomes
    var courseDiv = $('<tr id="course' + course.id + '">' +
        '<td>' + course.name + '</td>' +
        '<td>' + course.year + '</td>' +
        '<td>' + JSON.stringify(learningOutcomes) + '</td>' +
        '</tr>'
        );

    $(ALL_COURSES_ID).find('tbody').append(courseDiv);
};

var displayCourseList = function() {
    clearCourses();

    $.get(COURSES_BASE_PATH, function (courseList) {
        $(ALL_COURSES_ID).append($('<tr><th>Name</th><th>Year</th><th>Learning Outcomes</th></tr>'));
        courseList.forEach(displayCourse);
    });
};


var setUp = function() {
    displayCourseList();
};


$(setUp);