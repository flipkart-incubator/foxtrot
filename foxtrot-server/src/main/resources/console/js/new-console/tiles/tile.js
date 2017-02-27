function Tile() {
	this.id = null;
	this.type = null;
	this.tables = null;
	this.table = null;
	this.title = "";
	this.displayWidth = null;
	this.displayHeight = null;
	this.width = 0;
	this.height = 0;
	this.query = null;
	this.refresh = true;
	this.queue = null;
	this.cachedData = null;
	this.setupModalName = null;
	//this.url = hostDetails.url("/foxtrot/v1/analytics");
	this.contentType = "application/json";
	this.httpMethod = "POST";
	this.ignoreDigits = 0;
}

function submitClicked(e) {
	console.log('submit clicked');
}

function pushTilesObject(object) {
	tileData.push(object);
	console.log(tileData);
	console.log(JSON.stringify(tileData));
}

function updateTile(object, modal) {
	var selectedTile = $("#"+object.id);
	selectedTile.find(".tile-title").text(object.title);
	var tileid= object.id;
	var prepareTileData = { };
	prepareTileData[object.id] = object;
	pushTilesObject(prepareTileData);
}

function getTileFormValue(form, modal, object) {
	var tileFormValue = {};
	tileFormValue.title = form.find(".tile-title").val();
	tileFormValue.table = form.find(".tile-table").val();
	tileFormValue.id = form.find(".tileId").val();
	tileFormValue.timeUnit = form.find(".tile-time-unit").val();
	tileFormValue.timeValue = form.find(".tile-time-value").val();
	tileFormValue.chartType = form.find(".tile-chart-type").val();
	updateTile(tileFormValue, modal);
}

function setConfigValue(object, index) {
	var form = $("#tile-configuration").find("form");
	console.log('====>')
	console.log(tileData);
	var index = tileData.indexOf(object.id);
	console.log(tileData[index]);
	form.find(".tile-title").val(tileData[index].title);
	form.find(".tile-table").val(tileData[index].table);
	form.find(".tile-time-unit").val(tileData[index].timeUnit);
	form.find(".tile-time-value").val(tileData[index].timeValue);
	form.find(".tile-chart-type").val(tileData[index].chartType);
}

function clearForm() {
	var form = $("#tile-configuration").find("form");
	form.find(".tile-title").val('');
	form.find(".tile-table").val('');
	form.find(".tile-time-unit").val('');
	form.find(".tile-time-value").val('');
	form.find(".tile-chart-type").val('');
}

// create new div
function createNewRow(newDiv, object) {
	newDiv.addClass("col-md-12"); // add class for div which is full width
	if(panelRow.length == 0) { // initial page
		row = 1;
		panelRow.push({type : object.type, id : object.id});
		newDiv.addClass("row-"+row);
	} else { // incremetn row value by one
		panelRow.push({type : object.type, id : object.id});
		row = panelRow.length;
		newDiv.addClass("row-"+row);
	}
	if(object.type != "full") // dont add row add button for full widget
		newDiv.append("<button data-target='#addWidgetModal' class='tile-add-btn tile-add-btn btn btn-primary filter-nav-button glyphicon glyphicon-plus custom-add-btn'onClick='setClicketData(this)'  data-toggle='modal' id='row-"+row+"'></button>");
	return newDiv;
}

function TileFactory() {}
TileFactory.create = function (object) {
	var newDiv = $(handlebars("#tile-template", {
		tileId: object.id
		, title: ''
	}));
	var row = 0;// row
	var ele;// clicked element
	var clickedRow;// clicked row
	if(defaultPlusBtn) { // check its new row
		newDiv = createNewRow(newDiv, object)
	} else {// row button action
		var splitValue = customBtn.id.split("-");
		var rowObject = panelRow[splitValue[1] - 1];
		clickedRow = rowObject.id
		console.log();
		if(object.type != rowObject.type) {// f choosen type and row type is not equal
			newDiv = createNewRow(newDiv, object);
			defaultPlusBtn = true;
		}
	}

	if(object.type == "full") {
		newDiv.find(".tile").addClass('col-md-12');
	} else if(object.type == "medium") {
		newDiv.find(".tile").addClass('col-md-6');
		newDiv.find(".tile").width(590);
	} else if(object.type == "small") {
		newDiv.find(".tile").addClass('col-md-4');
		newDiv.find(".tile").width(300);
		newDiv.find(".tile").height(200);
	}

	if(defaultPlusBtn) {// new row
		newDiv.insertBefore('.float-clear');
	} else {// remove row btn and add new div based on type
		customBtn.remove();
		newDiv.insertAfter('#'+clickedRow);
	}

	newDiv.find(".widget-toolbox").find(".glyphicon-cog").click(function () {
		$("#tile-configuration").modal('show');
		$("#tile-configuration").find(".tileId").val(object.id);
		var tileListIndex = tileList.indexOf(object.id);
		var tileDataIndex = tileData[tileListIndex];
		if(tileDataIndex != undefined) {
			setConfigValue(object, tileDataIndex);
		} else {
			clearForm();
		}
	});

	$("#tile-configuration").find(".save-changes").click( function () {
    var form = $("#tile-configuration").find("form");
    form.off('submit');
    form.on('submit', $.proxy(function (e) {
			getTileFormValue(form, "tile-configuration", object)
	    $("#tile-configuration").modal('hide');
	    e.preventDefault();
    }, object));
	});
};