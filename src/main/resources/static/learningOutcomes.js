var EMPTY_HTML = '';
var ALL_LEARNING_OUTCOMES_ID = '#allLearningOutcomes';
var CREATE_OUTCOME_CATEGORY_SELECT_ID = '#categorySelect';
var CATEGORY_BASE_PATH = '/categories';
var OUTCOME_BASE_PATH = '/learningOutcomes';
var DELETE_OUTCOMES_SELECT_ID = '#deleteOutcomeSelect';

var clearLearningOutcomes = () => {
    $(ALL_LEARNING_OUTCOMES_ID).html(EMPTY_HTML);
};

var showLearningOutcome = (lo) => {
    var categoryData = $('<td></td>');
    $.get(CATEGORY_BASE_PATH + '/' + lo.category, (cat) => {
        categoryData.append($('<a href="#category' + cat.id + '">' + cat.name + '</a>'));
    });

    var learningOutcomeDiv = $('<tr id="outcome' + lo.id + '">' +
        '<td>' + lo.name + '</td>' +
        '</tr>'
    );
    learningOutcomeDiv.append(categoryData);

    $(ALL_LEARNING_OUTCOMES_ID).find('tbody').append(learningOutcomeDiv);
};

var displayLearningOutcomeList = () => {
    clearLearningOutcomes();
    $.get(CATEGORY_BASE_PATH, (categories) => {
        $(ALL_LEARNING_OUTCOMES_ID).append($('<tr><th>Name</th><th>Category</th></tr>'));
        categories.forEach((category) => {
            category.learningOutcomes.forEach((outcome) => {showLearningOutcome(outcome);});
        });
    });
};

var handleCreateOutcomeFormSubmission = (e) => {
    e.preventDefault();

    var outcomeData = {};

    var id;
    var inputs = $('form#outcomeForm :input').serializeArray().forEach((input) => {
        outcomeData[input.name] = input.value;
    });

    $.ajax({
        url:'/categories/' + outcomeData.category + '/learningOutcomes',
        type:'POST',
        data: JSON.stringify(outcomeData),
        contentType:'application/json',
        dataType:"json",
        success: () => {
            displayLearningOutcomeList();
            populateDeleteOutcomeForm();
            populateOutcomesForCourseForm();
            displayCategoryList();
        }
    });
};

var populateCategoriesForOutcomeForm = () => {
    //remove previous options
    $(CREATE_OUTCOME_CATEGORY_SELECT_ID).html(EMPTY_HTML);

    //get list of all categories
    $.get(CATEGORY_BASE_PATH, (categories) => {
        //append each category to the select when creating a learningOutcome
        categories.forEach((category) => {
            $(CREATE_OUTCOME_CATEGORY_SELECT_ID).append('<option value="' + category.id + '">' + category.id + ': ' + category.name + '</option>');
        });
    });
};

var populateDeleteOutcomeForm = () => {

    $(DELETE_OUTCOMES_SELECT_ID).html(EMPTY_HTML);

    $.get(CATEGORY_BASE_PATH, (categories) => {
        categories.forEach((category) => {
            category.learningOutcomes.forEach((outcome) => {
                $(DELETE_OUTCOMES_SELECT_ID).append('<option value="' + outcome.category + ' ' + outcome.id + '">' + outcome.id + ': ' + outcome.name + '</option>');
            });
        });
    });
};

var handleDeleteOutcomeFormSubmission = (e) => {
    e.preventDefault();

    var vals = $(DELETE_OUTCOMES_SELECT_ID).val();

    if (vals) {
        var ids = vals.split(" ");
        $.ajax({
            url: CATEGORY_BASE_PATH + '/' + ids[0] + OUTCOME_BASE_PATH + '/' + ids[1],
            type: 'DELETE',
            contentType: 'application/json',
            dataType: "json",
            success: () => {
                displayLearningOutcomeList();
                populateDeleteOutcomeForm();
                displayCourseList();
                populateOutcomesForCourseForm();
                displayCategoryList();
            }
        });
    }
};


var setUp = () => {
    displayLearningOutcomeList();
    populateCategoriesForOutcomeForm();
    populateDeleteOutcomeForm();
    $('#outcomeForm').submit(handleCreateOutcomeFormSubmission);
    $('#deleteOutcomeForm').submit(handleDeleteOutcomeFormSubmission);
};


$(setUp);