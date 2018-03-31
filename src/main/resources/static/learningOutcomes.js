var EMPTY_HTML = '';
var ALL_LEARNING_OUTCOMES_ID = '#allLearningOutcomes';
var CREATE_OUTCOME_CATEGORY_SELECT_ID = '#categorySelect';
var CATEGORY_BASE_PATH = '/categories';

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
        if (input.name === 'name') {
            outcomeData[input.name] = input.value;
        } else if (input.name === 'courses') {
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
        success: () => {
            displayLearningOutcomeList();
            populateOutcomesForCourseForm();
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






var setUp = () => {
    displayLearningOutcomeList();
    populateCategoriesForOutcomeForm();
    $('#outcomeForm').submit(handleCreateOutcomeFormSubmission);
};


$(setUp);