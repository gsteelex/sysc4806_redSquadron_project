var EMPTY_HTML = '';
var ALL_COURSES_ID = '#allCourses';
var COURSES_BASE_PATH = '/courses';

var clearCourses = function() {
    $(ALL_COURSES_ID).html(EMPTY_HTML);
};


var displayCourse = function(course) {
    var learningOutcomes = course.learningOutcomes.map(outcome => outcome.name);

    //TODO: this will be revisited once the courses can have learning outcomes
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
var handleCreateCourseFormSubmission = function (e) {
    e.preventDefault();

    var courseData = {};
    var inputs = $('form#courseForm :input').serializeArray().forEach(function(input) {
        courseData[input.name] = input.value;
    });

 

    $.ajax({
        url:'/courses',
        type:'POST',
        data: JSON.stringify(courseData),
        contentType:'application/json',
        dataType:"json",
        success: displayCourseList
    });
};


var setUp = function() {
    displayCourseList();
    $('#courseForm').submit(handleCreateCourseFormSubmission);
};


$(setUp);