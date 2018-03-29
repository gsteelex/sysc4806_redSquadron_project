var HIDDEN_CLASS = 'hidden';
var CREATE_DIV_ID = '#create';
var DELETE_DIV_ID = '#delete';

var modeChangedHandler = (e) => {

    if (e.target.value === 'create') {
        //show create and hide delete
        $(CREATE_DIV_ID).removeClass(HIDDEN_CLASS);
        $(DELETE_DIV_ID).addClass(HIDDEN_CLASS);

    } else if (e.target.value === 'delete') {
        //show delete and hide create
        $(CREATE_DIV_ID).addClass(HIDDEN_CLASS);
        $(DELETE_DIV_ID).removeClass(HIDDEN_CLASS);
    }
};

var setUp = () => {
    $('input[name="mode"]').change(modeChangedHandler);
};


$(setUp);