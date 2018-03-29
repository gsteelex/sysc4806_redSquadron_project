var EMPTY_HTML = '';
var ALL_COURSES_ID = '#allCourses';
var COURSES_BASE_PATH = '/courses';
var DELETE_COURSE_SELECT_ID = '#deleteCourseSelect';

var clearCourses = () => {
    $(ALL_COURSES_ID).html(EMPTY_HTML);
};


var displayCourse = (course) => {
    var outcomeData = $('<td></td>');
    var outcomeListHTML = $('<ul></ul>');

    course.learningOutcomes.forEach((outcome) => {
        outcomeListHTML.append($('<li><a href="#outcome' + outcome.id + '">' + outcome.name + '</a></li>'))
    });

    var courseDiv = $('<tr id="course' + course.id + '">' +
        '<td>' + course.name + '</td>' +
        '<td>' + course.year + '</td>' +
        '</tr>'
        );

    outcomeData.append(outcomeListHTML);
    courseDiv.append(outcomeData);
    $(ALL_COURSES_ID).find('tbody').append(courseDiv);
};

var displayCourseList = () => {
    clearCourses();

    $.get(COURSES_BASE_PATH, (courseList) => {
        $(ALL_COURSES_ID).append($('<tr><th>Name</th><th>Year</th><th>Learning Outcomes</th></tr>'));
        courseList.forEach(displayCourse);

        populateCoursesForProgramForm();
    });
};
var handleCreateCourseFormSubmission = (e) => {
    e.preventDefault();

    var courseData = {};
    var inputs = $('form#courseForm :input').serializeArray().forEach((input) => {
        courseData[input.name] = input.value;
    });

    $.ajax({
        url:'/courses',
        type:'POST',
        data: JSON.stringify(courseData),
        contentType:'application/json',
        dataType:"json",
        success: () => {
            displayCourseList();
            populateDeleteCourseForm();
        }
    });
};

var populateDeleteCourseForm = () => {
    $(DELETE_COURSE_SELECT_ID).html(EMPTY_HTML);

    $.get(COURSES_BASE_PATH, (courses) => {
        courses.forEach((course) => {
            $(DELETE_COURSE_SELECT_ID).append('<option value="' + course.id + '">' + course.id + ': ' + course.name + '</option>');
        });
    });
};

var handleDeleteCourseFormSubmission = (e) => {
    e.preventDefault();

    var id = $(DELETE_COURSE_SELECT_ID).val();

    $.ajax({
        url:COURSES_BASE_PATH + '/' + id,
        type:'DELETE',
        contentType:'application/json',
        dataType:"json",
        success: () => {
            displayCourseList();
            populateDeleteCourseForm();
        },
        error: (errorResult) => {
            alert("Could not delete course: " + errorResult.responseJSON.message);
        }
    });
};

var setUp = () => {
    displayCourseList();
    populateDeleteCourseForm();
    $('#courseForm').submit(handleCreateCourseFormSubmission);
    $('#deleteCourseForm').submit(handleDeleteCourseFormSubmission);
};


$(setUp);