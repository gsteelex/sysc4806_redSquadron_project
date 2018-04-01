var EMPTY_HTML = '';
var ALL_COURSES_ID = '#allCourses';
var COURSES_BASE_PATH = '/courses';
var OUTCOME_SELECT = '#outcomeSelect';
var DELETE_COURSE_SELECT_ID = '#deleteCourseSelect';
var CATEGORY_BASE_PATH = '/categories';
var OUTCOME_BASE_PATH = '/learningOutcomes';

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

    var courseData = {
        learningOutcomes:[]
    };
    var inputs = $('form#courseForm :input').serializeArray().forEach((input) => {

        if (input.name === 'name') {
            courseData[input.name] = input.value;
        } else if (input.name === 'outcomes[]') {
            courseData['learningOutcomes'].push(input.value);
        } else if(input.name === 'year'){
            courseData[input.name] = input.value;
        }
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

    //only delete if a course is selected
    if (id) {
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
    }
};
var populateOutcomesForCourseForm = () => {
    //remove previous options
    $(OUTCOME_SELECT).html(EMPTY_HTML);

    //get list of all courses
    $.get(CATEGORY_BASE_PATH, (categories) => {

        //append each course to the multi select when creating a course
        categories.forEach((category) => {
            $.get(CATEGORY_BASE_PATH +'/' + category.id + '/' + OUTCOME_BASE_PATH, (learningOutcomes) => {
                learningOutcomes.forEach((outcome) => {
                    $(OUTCOME_SELECT).append('<option value="' + outcome.id + '">' + outcome.id + ': ' + outcome.name + '</option>')
                });
            });

        });
    });
};
var setUp = () => {
    displayCourseList();
    populateDeleteCourseForm();
    populateOutcomesForCourseForm();
    $('#courseForm').submit(handleCreateCourseFormSubmission);
    $('#deleteCourseForm').submit(handleDeleteCourseFormSubmission);
};


$(setUp);