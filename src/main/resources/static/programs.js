var setUp = function(){
    console.log('here');
    $('#allPrograms>a').click(function(e) {
        console.log(e);
        e.preventDefault();
        var id = e.target.href.split('/')[e.target.href.split('/').length - 1];

        console.log(id);
        $.get("/programs/" + id, function(program){
            //displayProgram(program);
            alert(JSON.stringify(program));
        });


    });

};
$(setUp);