var EMPTY_HTML = '';
var ALL_PROGRAMS_ID = '#allPrograms';
var CREATE_PROGRAM_COURSES_SELECT_ID = '#programCoursesSelect';
var DELETE_PROGRAMS_SELECT_ID = '#deleteProgramSelect';
var UPDATE_PROGRAM_SELECT_ID = '#updateProgramSelect';
var UPDATE_PROGRAM_NAME_ID = '#updateProgramName';
var UPDATE_PROGRAM_COURSES_ID = '#programUpdateCoursesSelect';
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
        } else if (input.name === 'courses[]') {
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
            populateUpdateProgramForm();
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

    var id = $(DELETE_PROGRAMS_SELECT_ID).val();

    //only delete a program if one is selected
    if (id) {
        $.ajax({
            url:PROGRAMS_BASE_PATH + '/' + id,
            type:'DELETE',
            contentType:'application/json',
            dataType:"json",
            success: () => {
                displayProgramList();
                populateDeleteProgramForm();
                populateUpdateProgramForm();
            }
        });
    }
};

var populateUpdateProgramForm = () => {
    $(UPDATE_PROGRAM_SELECT_ID).html(EMPTY_HTML);

    $.get(PROGRAMS_BASE_PATH, (programs) => {
        programs.forEach((program) => {
            $(UPDATE_PROGRAM_SELECT_ID).append('<option value="' + program.id + '">' + program.id + ': ' + program.name + '</option>');
        });

        populateUpdateProgramFormWithSelectedProgram();
    });
};

var populateUpdateProgramFormWithSelectedProgram = () => {
    $(UPDATE_PROGRAM_COURSES_ID).html(EMPTY_HTML);
    var programId = $(UPDATE_PROGRAM_SELECT_ID).val();

    //only populate the update form if a program is selected
    if (programId) {
        //get the program
        $.get(PROGRAMS_BASE_PATH + '/' + programId, (program) => {

            //keep track of the courses currently part of the program
            var programCourses = program.courses.map(course => course.id);

            //set the name field for the program
            $(UPDATE_PROGRAM_NAME_ID).val(program.name);

            //get all courses
            $.get(COURSES_BASE_PATH, (courses) => {
                //append each course to the multi select, and select the ones that are in the program
                courses.forEach((course) => {
                    if ($.inArray(course.id, programCourses) > -1) {
                        $(UPDATE_PROGRAM_COURSES_ID).append('<option value="' + course.id + '" selected>' + course.id + ': ' + course.name + '</option>');
                    } else {
                        $(UPDATE_PROGRAM_COURSES_ID).append('<option value="' + course.id + '">' + course.id + ': ' + course.name + '</option>');
                    }
                });
            });
        });
    }
};


var handleUpdateProgramSubmit = (e) => {
    e.preventDefault();

    var programId = $(UPDATE_PROGRAM_SELECT_ID).val();
    var newProgramName = $(UPDATE_PROGRAM_NAME_ID).val();
    var newProgramCourses = $(UPDATE_PROGRAM_COURSES_ID).val();

    //only update if a program is selected
    if (programId) {
        var programData = {
            name: newProgramName,
            courses: newProgramCourses? newProgramCourses: []  //check for null value, if null set to empty array
        };

        $.ajax({
            url:PROGRAMS_BASE_PATH + '/' + programId,
            type:'PATCH',
            contentType:'application/json',
            dataType:"json",
            data: JSON.stringify(programData),
            success: () => {
                displayProgramList();
                populateDeleteProgramForm();
                populateUpdateProgramForm();
            }
        });
    }
};

var setUp = () => {
    displayProgramList();
    populateCoursesForProgramForm();
    populateDeleteProgramForm();
    populateUpdateProgramForm();
    $(UPDATE_PROGRAM_SELECT_ID).change(populateUpdateProgramFormWithSelectedProgram);
    $('#programForm').submit(handleCreateProgramFormSubmission);
    $('#deleteProgramForm').submit(handleDeleteProgramFormSubmission);
    $('#updateProgramForm').submit(handleUpdateProgramSubmit);
};


$(setUp);