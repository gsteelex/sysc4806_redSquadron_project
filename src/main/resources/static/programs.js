var handleProgramData = function(){
    $.get("/programs", function(body){
        $('#allPrograms').text('');
        body.forEach(function(program){
            $('#allPrograms').append($('<a href="/showPrograms/'+program.id  + '">' + program.name + '</a></br>'));
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



var setUp = function(){
    $('#allPrograms>a').click(function(e) {
        e.preventDefault();
        var id = e.target.href.split('/')[e.target.href.split('/').length - 1];
        $.get("/programs/" + id, function(program){
            //displayProgram(program);
            alert(JSON.stringify(program));
        });


    });

    $('#programForm').submit(handleCreateProgramFormSubmission);

};



$(setUp);