var EMPTY_HTML = '';
var ALL_CATEGORIES_ID = '#allCategories';
var CATEGORIES_BASE_PATH = '/categories';
var DELETE_CATEGORIES_SELECT_ID = '#deleteCategorySelect';

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
            populateDeleteCategoryForm();
            displayCategoryList();
        }
    });
};

var clearCategories = () => {
    $(ALL_CATEGORIES_ID).html(EMPTY_HTML);
};

var showCategory = (category) => {
    var learningOutcomeData = $('<td></td>');
    var learningOutcomeListHTML = $('<ul></ul>');

    category.learningOutcomes.forEach((learningOutcome) => {
        learningOutcomeListHTML.append($('<li><a href="#outcome' + learningOutcome.id + '">' + learningOutcome.name + '</a></li>'))
    });

    var categoryDiv = $('<tr id="category' + category.id + '">' +
            '<td>' + category.name + '</td>' +
            '</tr>'
            );

    learningOutcomeData.append(learningOutcomeListHTML);
    categoryDiv.append(learningOutcomeData);

    $(ALL_CATEGORIES_ID).find('tbody').append(categoryDiv);
};

var populateDeleteCategoryForm = () => {

    $(DELETE_CATEGORIES_SELECT_ID).html(EMPTY_HTML);

    $.get(CATEGORY_BASE_PATH, (categories) => {
        categories.forEach((category) => {
            $(DELETE_CATEGORIES_SELECT_ID).append('<option value="' + category.id + '">' + category.id + ': ' + category.name + '</option>');
        });
    });
};

var handleDeleteCategoryFormSubmission = (e) => {
    e.preventDefault();

    var id = $(DELETE_CATEGORIES_SELECT_ID).val();

    if (id) {
        $.ajax({
            url: CATEGORY_BASE_PATH + '/' + id,
            type: 'DELETE',
            contentType: 'application/json',
            dataType: "json",
            success: () => {
                populateDeleteCategoryForm();
                populateCategoriesForOutcomeForm();
            },
            error: (errorResult) => {
                alert("Could not delete category: " + errorResult.responseJSON.message);
            }
        });
    }
};

var displayCategoryList = () => {
    clearCategories();
    $.get(CATEGORIES_BASE_PATH, (categories) => {
        $(ALL_CATEGORIES_ID).append($('<tr><th>Name</th><th>LearningOutcomes</th></tr>'));
        categories.forEach(showCategory);
    });
};

var setUp = () => {
    displayCategoryList();
    populateDeleteCategoryForm();
    $('#categoryForm').submit(handleCreateCategoryFormSubmission);
    $('#deleteCategoryForm').submit(handleDeleteCategoryFormSubmission);
};


$(setUp);