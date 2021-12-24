/**
 * 시간대 컴포넌트
 * @param parent
 * @param style
 * @returns
 */
function MarketDateTermEditor(parent, style) {
	this.style = style|| {};
	this.className = this.style.className || 'MarketDateTermEditor';
	this.widget = $('<div>').addClass(this.className).appendTo(parent);
	this.mode = this.style.mode || 'write';
	
	this.init();
}
MarketDateTermEditor.prototype.init = function() {
	var _this = this;
	if (this.mode == 'edit') {
		var timeDiv = $('<div>\
				<div class="dateView" id="dateStartDiv"></div>\
				<span class="bar">-</span>\
				<div class="date" id="dateEndDiv"></div>\
				<div class="select_box" id="selectEndDiv">\
				</div>\
		</div>').addClass('set_time').appendTo(this.widget);
		var startDiv = $('#selectStartDiv');
		var endDiv = $('#selectEndDiv');

		this.startDate = new DateViewer($('#dateStartDiv'), {format:JSV.getLang('MarketDateTermEditor', 'dateFormat')});
		this.startDate.setValue(new Date().getTime());
		this.endDate = new DateFieldEditor($('#dateEndDiv'), this.style);
		this.endTime = new DropDownGroupFieldEditor(endDiv);
		this.setTimeOption();
	} else {
		var timeDiv = $('<div>\
				<div class="date" id="dateStartDiv"></div>\
				<div class="select_box" id="selectStartDiv">\
				</div>\
				<span class="bar">-</span>\
				<div class="date" id="dateEndDiv"></div>\
				<div class="select_box" id="selectEndDiv">\
				</div>\
		</div>').addClass('set_time').appendTo(this.widget);
		var startDiv = $('#selectStartDiv');
		var endDiv = $('#selectEndDiv');
		this.startDate = new DateFieldEditor($('#dateStartDiv'), this.style);
		this.startDate.setValue(new Date().getTime());
		var thisNow = new Date(Date.parse(new Date()) + (1000*60*30));
		this.startTime = new DropDownGroupFieldEditor(startDiv);
		this.startTime.setValue(this.addZeroStr(thisNow.getHours()) + ':' + this.addZeroStr(this.resetMinute(thisNow.getMinutes())));
		
		this.endDate = new DateFieldEditor($('#dateEndDiv'), this.style);
		var day = new Date();
		var basicPeriod = this.style.basicPeriod;		
		var tomorrow = day.setDate(day.getDate() + parseInt(basicPeriod));
		this.endDate.setValue(tomorrow);
		this.endTime = new DropDownGroupFieldEditor(endDiv);
		this.endTime.setValue(this.addZeroStr(thisNow.getHours()) + ':' + this.addZeroStr(this.resetMinute(thisNow.getMinutes())));
		this.setTimeOption();
	}
} 
MarketDateTermEditor.prototype.setTimeOption = function() {
	for (var i = 0; i < 24; i++) {
		if (this.mode == 'write') {
			this.startTime.add(this.addZeroStr(i) + ':00', this.addZeroStr(i) + ':00');
			this.startTime.add(this.addZeroStr(i) + ':30', this.addZeroStr(i) + ':30');
		}
		
		this.endTime.add(this.addZeroStr(i) + ':00', this.addZeroStr(i) + ':00');
		this.endTime.add(this.addZeroStr(i) + ':30', this.addZeroStr(i) + ':30');
	}
}
MarketDateTermEditor.prototype.setValue = function(values) {
	var start = new Date(values.startTime) || new Date();
	var end = new Date(values.endTime) || new Date();
	if (this.mode == 'write') {
		if ( typeof JSV.getParameter('id') == 'undefined' || JSV.getParameter('id') < 1 ) { //최초 설문등록시에는 startTime는 현재 설문시작시간 + 30분 후 가리키도록 함
			var thisNow = new Date(Date.parse(new Date()) + (1000*60*30));
			this.startDate.setValue(thisNow.getTime());
			this.startTime.setValue(this.addZeroStr(thisNow.getHours()) + ':' + this.addZeroStr(this.resetMinute(thisNow.getMinutes())));
		} else { //나의설문관리 수정시에는 작성한 설문시작시간이 바뀌면 안되므로
			this.startDate.setValue(values.startTime);
			this.startTime.setValue(this.addZeroStr(start.getHours()) + ':' + this.addZeroStr(start.getMinutes()));
		}
	}
	this.endDate.setValue(values.endTime);
	this.endTime.setValue(this.addZeroStr(end.getHours()) + ':' + this.addZeroStr(end.getMinutes()));
}
MarketDateTermEditor.prototype.resetMinute = function(minute) {
	if ( minute == 0) {
		return 0;
	} else if ( (minute > 0) && (minute < 30) ) {
		return 0;
	} else {
		return 30;
	}
}
MarketDateTermEditor.prototype.getValue = function() {
	var arr = [];
	arr.push(this.getStartDate());
	arr.push(this.getEndDate());
	return arr;
}
MarketDateTermEditor.prototype.getStartDate = function() {
	var startDate = new Date(this.startDate.getValue());
	if (this.mode == 'write') {
		var time = null;
		var date = startDate;
		var now = new Date();	// 현재 날짜 및 시간
		var hours = now.getHours();	// 시간
		var minutes = now.getMinutes();	// 분
		/*30분단위 체크 */
		if(minutes < 30){
			minutes = 30;
		}else{
			minutes = 0;
			hours = hours+1;
		}
		/*입력받은 값이 있을때 */
		if(this.startTime.getValue() != null){
		 time = this.startTime.getValue().split(':'); 
		}else{
			var time = hours + ":" + minutes;
			time = time.split(':');
		}
		startDate = new Date(date.getFullYear(), date.getMonth(), date.getDate(), time[0], time[1]);
	}
	startDate = startDate.getTime(); // mode가 edit일 때 write와 같이 데이터 형식을 맞춰주기 위해 getTime을 마지막에 추가
	return startDate;
}
MarketDateTermEditor.prototype.getEndDate = function() {
	var date = new Date(this.endDate.getValue());
	var time = null;
	var date = new Date(this.endDate.getValue());
	var now = new Date();	// 현재 날짜 및 시간
	var hours = now.getHours();	// 시간
	var minutes = now.getMinutes();	// 분
	/*30분단위 체크 */
	if(minutes < 30){
		minutes = 30;
	}else{
		minutes = 0;
		hours = hours+1;
	}
	/*입력받은 값이 있을때 */
	if(this.endTime.getValue() != null){
		time = this.endTime.getValue().split(':');
	}else{
		var time = hours + ":" + minutes;
		time = time.split(':');
	}
	var endDate = new Date(date.getFullYear(), date.getMonth(), date.getDate(), time[0], time[1]).getTime(); 
	return endDate;
}
MarketDateTermEditor.prototype.validate = function(metadata) {
	var currTime = new Date();
	
	if (this.getStartDate() < currTime) {	//2019.05.17 sunyi
		if (this.mode == 'write') { // write일 때만 체크
			return metadata.message3;
		}
	}else if (this.getStartDate() >= this.getEndDate()) {
		return metadata.message1;
	}else if(currTime > this.getEndDate()){
		return metadata.message2;
	}
	return null;
}
MarketDateTermEditor.prototype.addZeroStr = function(str) {
	return MarketDateTermEditor.addZeroStr(str);
}
MarketDateTermEditor.addZeroStr = function(str) {
	var num = parseInt(str);
	return (num < 10) ? '0' + num : '' + num; 
}
/**
 * DropDownGroupFieldEditor 확장
 * <p>
 * refresh prototype 추가
 */
function MarketDropDownGroupFieldEditor(parent, style) {
	DropDownGroupFieldEditor.call(this, parent, $.extend(style, {}));
}
MarketDropDownGroupFieldEditor.prototype = $.extend({}, DropDownGroupFieldEditor.prototype, {
	refresh : function(value) {
		this.widget.empty();
		this.init();
		if (value) {
			var options = value.split(',');
			var option = [];
			for (var i = 0; i < options.length; i++) {
				option = options[i].split(':');
				this.add(option[1], option[0]);
			}
		}
	}
});
DropDownGroupFieldEditor.prototype.setValue = function(value) {
	if (value != null) {
		var an = this.ul.children().removeClass('selected').find('a[value=' + '"' + value + '"' + ']');
		this.txtSpan.html(an.find('span.txt').text());
		an.parent().addClass('selected');
	}
}
