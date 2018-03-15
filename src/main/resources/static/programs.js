var handleProgramData = function() {
    $.get("/programs", function (body) {
        $('#allPrograms').text('');
        body.forEach(function (program) {
            var appendUrl = $('<a href="/showPrograms/' + program.id + '">' + program.name + '</a></br>');
            appendUrl.click(function (e) {
                e.preventDefault();
                var id = e.target.href.split('/')[e.target.href.split('/').length - 1];
                $.get("/programs/" + id, function (program) {
                    //displayProgram(program);
                    alert(JSON.stringify(program));
                });
            });
            $('#allPrograms').append(appendUrl);

        });
    });
};




var handleCreateProgramFormSubmission = function (e) {
    e.preventDefault();

    var programData = {};
    var inputs = $('form#programForm :input').serializeArray().forEach(function(input) {
        programData[input.name] = input.value;
    });

    console.log(programData);
    // $.post('/programs', programData)
    $.ajax({
        url:'/programs',
        type:'POST',
        data: JSON.stringify(programData),
        contentType:'application/json',
        dataType:"json",
        success: handleProgramData
    });
};

var handleCreateCourseFormSubmission = function (e) {
    e.preventDefault();

    var courseData = {};
    var inputs = $('form#courseForm :input').serializeArray().forEach(function(input) {
        courseData[input.name] = input.value;
    });

    console.log(courseData);

    $.ajax({
        url:'/courses',
        type:'POST',
        data: JSON.stringify(courseData),
        contentType:'application/json',
        dataType:"json",

    });
};



var setUp = function(){
    $('#allPrograms a').click(function(e) {
        e.preventDefault();
        var id = e.target.href.split('/')[e.target.href.split('/').length - 1];
        $.get("/programs/" + id, function(program){
            //displayProgram(program);
            alert(JSON.stringify(program));
        });


    });

    $('#programForm').submit(handleCreateProgramFormSubmission);
    $('#courseForm').submit(handleCreateCourseFormSubmission);

};



$(setUp);