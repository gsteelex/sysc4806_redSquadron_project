var EMPTY_HTML = '';
var CATEGORIES_BASE_PATH = '/categories';

var handleCreateCategoryFormSubmission = (e) => {
    e.preventDefault();

    var categoryData = {};
    var inputs = $('form#categoryForm :input').serializeArray().forEach((input) => {
        categoryData[input.name] = input.value;
    });

    $.ajax({
        url: CATEGORIES_BASE_PATH,
        type:'POST',
        data: JSON.stringify(categoryData),
        contentType:'application/json',
        dataType:"json",
        success: (result) => {console.log(result);
            populateCategoriesForOutcomeForm();
        }
    });
};


var setUp = () => {

    $('#categoryForm').submit(handleCreateCategoryFormSubmission);
};


$(setUp);