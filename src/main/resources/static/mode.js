var HIDDEN_CLASS = 'hidden';
var CREATE_DIV_ID = '#create';
var DELETE_DIV_ID = '#delete';
var UPDATE_DIV_ID = '#update';


var modeChangedHandler = (e) => {

    if (e.target.value === 'create') {
        //show create only
        $(CREATE_DIV_ID).removeClass(HIDDEN_CLASS);
        $(DELETE_DIV_ID).addClass(HIDDEN_CLASS);
        $(UPDATE_DIV_ID).addClass(HIDDEN_CLASS);

    } else if (e.target.value === 'delete') {
        //show delete only
        $(CREATE_DIV_ID).addClass(HIDDEN_CLASS);
        $(UPDATE_DIV_ID).addClass(HIDDEN_CLASS);
        $(DELETE_DIV_ID).removeClass(HIDDEN_CLASS);

    } else if (e.target.value === 'update') {
        //show update only
        $(UPDATE_DIV_ID).removeClass(HIDDEN_CLASS);
        $(CREATE_DIV_ID).addClass(HIDDEN_CLASS);
        $(DELETE_DIV_ID).addClass(HIDDEN_CLASS);
    }

};

var setUp = () => {
    $('input[name="mode"]').change(modeChangedHandler);
};


$(setUp);