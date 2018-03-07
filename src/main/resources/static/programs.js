var setUp = function(){
    $('#allPrograms>a').click(function(e) {
        e.preventDefault();
        var id = e.target.href.split('/')[e.target.href.split('/').length - 1];
        $.get("/programs/" + id, function(program){
            //displayProgram(program);
            alert(JSON.stringify(program));
        });


    });

};
$(setUp);