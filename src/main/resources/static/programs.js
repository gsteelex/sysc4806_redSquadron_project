
var EMPTY_HTML = '';
var ALL_PROGRAMS_ID = '#allPrograms';
var PROGRAMS_BASE_PATH = '/programs';

var clearPrograms = () => {
    $(ALL_PROGRAMS_ID).html(EMPTY_HTML);
};

var handleCreateProgramFormSubmission = (e) => {
    e.preventDefault();

    var programData = {};
    var inputs = $('form#programForm :input').serializeArray().forEach((input) => {
        programData[input.name] = input.value;
    });

    $.ajax({
        url:'/programs',
        type:'POST',
        data: JSON.stringify(programData),
        contentType:'application/json',
        dataType:"json",
        success: displayProgramList
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

var setUp = () => {
    displayProgramList();
    $('#programForm').submit(handleCreateProgramFormSubmission);
};


$(setUp);