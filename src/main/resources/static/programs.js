var EMPTY_HTML = '';
var ALL_PROGRAMS_ID = '#allPrograms';
var CREATE_PROGRAM_COURSES_SELECT_ID = '#programCoursesSelect';
var DELETE_PROGRAMS_SELECT_ID = '#deleteProgramSelect';
var PROGRAMS_BASE_PATH = '/programs';
var COURSES_BASE_PATH = '/courses';

var clearPrograms = () => {
    $(ALL_PROGRAMS_ID).html(EMPTY_HTML);
};

var handleCreateProgramFormSubmission = (e) => {
    e.preventDefault();

    var programData = {
        courses:[]
        };
    var inputs = $('form#programForm :input').serializeArray().forEach((input) => {
        if (input.name === 'name') {
            programData[input.name] = input.value;
        } else if (input.name = 'courses[]') {
            programData['courses'].push(input.value);
        }


    });

    $.ajax({
        url:'/programs',
        type:'POST',
        data: JSON.stringify(programData),
        contentType:'application/json',
        dataType:"json",
        success: () => {
            displayProgramList();
            populateDeleteProgramForm();
        }
    });
};

var showProgram = (program) => {
    var courseData = $('<td></td>');
    var courseListHTML = $('<ul></ul>');

    program.courses.forEach((course) => {
        courseListHTML.append($('<li><a href="#course' + course.id + '">' + course.name + '</a></li>'))
    });

    var programDiv = $('<tr id="program' + program.id + '">' +
            '<td>' + program.name + '</td>' +
            '</tr>'
            );

    courseData.append(courseListHTML);
    programDiv.append(courseData);

    $(ALL_PROGRAMS_ID).find('tbody').append(programDiv);
};

var displayProgramList = () => {
    clearPrograms();
    $.get(PROGRAMS_BASE_PATH, (programs) => {
        $(ALL_PROGRAMS_ID).append($('<tr><th>Name</th><th>Courses</th></tr>'));
        programs.forEach(showProgram);
    });
};

var populateCoursesForProgramForm = () => {
    //remove previous options
    $(CREATE_PROGRAM_COURSES_SELECT_ID).html(EMPTY_HTML);

    //get list of all courses
     $.get(COURSES_BASE_PATH, (courses) => {
            //append each course to the multi select when creating a course
            courses.forEach((course) => {
                $(CREATE_PROGRAM_COURSES_SELECT_ID).append('<option value="' + course.id + '">' + course.id + ': ' + course.name + '</option>');
            });
        });
};

var populateDeleteProgramForm = () => {

    $(DELETE_PROGRAMS_SELECT_ID).html(EMPTY_HTML);

    $.get(PROGRAMS_BASE_PATH, (programs) => {
        programs.forEach((program) => {
            $(DELETE_PROGRAMS_SELECT_ID).append('<option value="' + program.id + '">' + program.id + ': ' + program.name + '</option>');
        });
    });
};

var handleDeleteProgramFormSubmission = (e) => {
    e.preventDefault();

    var id = $('#deleteProgramSelect').val();

    $.ajax({
        url:'/programs/' + id,
        type:'DELETE',
        contentType:'application/json',
        dataType:"json",
        success: () => {
            displayProgramList();
            populateDeleteProgramForm();
        }
    });
};

var setUp = () => {
    displayProgramList();
    populateCoursesForProgramForm();
    populateDeleteProgramForm();
    $('#programForm').submit(handleCreateProgramFormSubmission);
    $('#deleteProgramForm').submit(handleDeleteProgramFormSubmission);
};


$(setUp);