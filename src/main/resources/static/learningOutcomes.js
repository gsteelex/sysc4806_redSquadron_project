var EMPTY_HTML = '';
var CREATE_OUTCOME_CATEGORY_SELECT_ID = '#categorySelect';
var CATEGORY_BASE_PATH = '/categories';

var handleCreateOutcomeFormSubmission = (e) => {
    e.preventDefault();

    var outcomeData = {};

    var id;
    var inputs = $('form#outcomeForm :input').serializeArray().forEach((input) => {
        if (input.name === 'name') {
            outcomeData[input.name] = input.value;
        } else if (input.name = 'courses[]') {
            id = input.value;
            outcomeData.category = id;
        }

    });

    $.ajax({
        url:'/categories/' + id + '/learningOutcomes',
        type:'POST',
        data: JSON.stringify(outcomeData),
        contentType:'application/json',
        dataType:"json",

    });
};

var populateCategoriesForOutcomeForm = () => {
    //remove previous options
    $(CREATE_OUTCOME_CATEGORY_SELECT_ID).html(EMPTY_HTML);

    //get list of all courses
    $.get(CATEGORY_BASE_PATH, (categories) => {
        //append each course to the multi select when creating a course
        categories.forEach((category) => {
            $(CREATE_OUTCOME_CATEGORY_SELECT_ID).append('<option value="' + category.id + '">' + category.id + ': ' + category.name + '</option>');
        });
    });
};







var setUp = () => {
    populateCategoriesForOutcomeForm();
    $('#outcomeForm').submit(handleCreateOutcomeFormSubmission);
};


$(setUp);