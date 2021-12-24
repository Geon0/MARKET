function MarketDateColumn(parent, style) {
	this.parent = parent;
	this.style = style || {};
	this.className = this.style.className || 'DateColumn';
	this.noDate = this.style.noDate || '-';
	parent.appendChild(this);
}
MarketDateColumn.prototype.setValue = function(element, td, tr) {
	
	var dateText = this.noDate;
	if (element && this.style.attribute) {
		var time = parseInt(element[this.style.attribute]);
		var odate = new Date(time);
		if (null != this.style.serverOffset) {
			odate = DateViewer.getServerTime(odate, this.style.serverOffset);
		}
		odate = DateFormat.format(odate, JSV.getLang('DateFormat','fullType3'));
		var startTime = DateFormat.format(new Date(element.startTime), JSV.getLang('DateFormat','fullType3'));
		var endTime = DateFormat.format(new Date(element.endTime), JSV.getLang('DateFormat','fullType3'));
		dateText = (time) ? odate+"~"+endTime : dateText;
	}
	
	$('<div>').addClass(this.className).css({
		'text-align' : this.style.align || 'center'
	}).text(dateText).appendTo(td);
}
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
function MarketDateTermViewer(parent, style) {
	this.style = style || {};
	this.className = this.style.className || 'DateTermViewer';
	this.widget = $('<div>').addClass(this.className).appendTo(parent);
	this.format = this.style.format ? this.style.format : null;
}
MarketDateTermViewer.prototype.setFormat = function(format) {
	this.format = format;
}
MarketDateTermViewer.prototype.setValue = function(value) {
	if (value == null || value.length == 0 ) {
		return;
	}	
	if ( value[0]!= null && value[1] != null){
		this.startDate = value[0];
		this.endDate = value[1];
		if (typeof(this.startDate) != 'number') this.startDate = parseInt(this.startDate);
		if (typeof(this.endDate) != 'number') this.endDate = parseInt(this.endDate);
		str = DateFormat.format(new Date(this.startDate), this.format) + ' ~ ' + DateFormat.format(new Date(this.endDate), this.format);
		this.widget.text(str);
	}else if(value[0] == null && value[1] != null){
		this.startDate ='';
		this.endDate = value[1];
		if (typeof(this.endDate) != 'number') this.endDate = parseInt(this.endDate);
		str = this.startDate+ ' ~ ' + DateFormat.format(new Date(this.endDate), this.format);
		this.widget.text(str);
	}else if(value[0] != null && value[1] == null){
		this.startDate = value[0];
		this.endDate = '';
		if (typeof(this.startDate) != 'number') this.startDate = parseInt(this.startDate);
		str = DateFormat.format(new Date(this.startDate), this.format)+ ' ~ ' + this.endDate;
		this.widget.text(str);
	}else{
		this.widget.text('-');
	}
}
MarketDateTermViewer.prototype.getValue = function() {
	var value = new Array();
	value[0] = this.startDate;
	value[1] = this.endDate;
	return value;
}
/**
 * TD 숨기기 보이기 기능을 위한 메서드 추가
 * 
 * @param parent
 * @param style
 *            message : ItemViewer.js<br>
 * @returns
 */
ItemViewer.hideTD = function(element) {
	while(element) {
		element = element.parentNode;
		if (element.nodeName == 'TD') {
			var $element = $(element);
			$element.attr('hide', 'true');
			
			$element.css('visibility', 'hidden').hide();
			var table = element.parentNode.parentNode;
			ItemViewer.setValueAfterInit(table);
			break;
		}
	}
}
ItemViewer.showTD = function(element) {
	while(element) {
		element = element.parentNode;
		if (element.nodeName == 'TD') {
			var $element = $(element);
			$element.removeAttr('hide');
			$element.show().css('visibility', 'visible');
			if(JSV.browser.msie6){
				ItemViewer.subShow($element);
			}
			var table = element.parentNode.parentNode;
			ItemViewer.setValueAfterInit(table);
			break;
		}
	}
}
/**
 * 콤마처리를 위한 MarketNumberColumn
 * 
 * @param parent
 * @param style
 *            message : MarketNumberColumn.js<br>
 * @returns
 */
function MarketNumberColumn(parent, style) {
	TextColumn.call(this, parent, $.extend(style, {}));
	this.isCommas = this.style.isCommas? eval(this.style.isCommas) : false;
}
MarketNumberColumn.prototype = $.extend({}, TextColumn.prototype, {
	setValue : function(element, td, tr) {
		$(td).attr('align', this.align);
		var div = $('<div>').addClass(this.className).appendTo(td);
		if (this.padding)
			div.css('padding', this.padding);
		if (this.style.noticeImg && element['announced'] == 1) {
			div.html('<img class="TextColumnImg" src="' + JSV.getLocaleImagePath(this.style.noticeImg) + '"/>');
		} else {
			if (this.labelProvider == null) {
				this.setLabelProvider(new TextColumnLabelProvider());
			}
			this.textValue = (element && this.style.attribute)
					? this.labelProvider.getText(element, this.style.attribute)
					: '';
			this.textValue = (this.style.defaultValue == this.textValue && this.style.defaultDisplay)
					? this.style.defaultDisplay
					: this.textValue;
			this.textValue = this.style.pre ? this.style.pre + this.textValue : this.textValue;
			this.textValue = this.style.post ? this.textValue + this.style.post : this.textValue;
			if ($.isNumeric(this.textValue) && this.isCommas) {
				this.textValue = JSV.getLang('DataFormat','moneyType2')+this.textValue.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
			}
			$('<nobr>').addClass('nobr').appendTo(div).attr('title', this.textValue || '').text(
					this.style.isMenu ? '[ ' + this.textValue + ' ]' : this.textValue);
		}
	}
});
/**
 * 현재가격, 즉시구매가를 입력받는다
 * 가격정보를 출력한다.
 */
function MarketNumberFieldEditor(parent, style) {
	var _this = this;
	this.style = style || {};
	this.prefix = this.style.prefix || '';
	this.className = this.style.className || 'TextFieldEditor';
	this.widget = $('<input type="text">').val(JSV.getLang('DataFormat','moneyType2')+'0').addClass(this.className).appendTo(parent).bind('keyup', this, function(e) {
		$(this).val(_this.inputNumberFormat($(this).val().replace(/[^0-9]/gi, "")));
		if (e.data.onkeyup)
			e.data.onkeyup(e);
		if (e.keyCode == 13)
			return false;
	}).bind('focus', this, function(e) {
		$(this).addClass(e.data.className + '_focus');
		e.data.onfocus(e);
	}).bind('blur', this, function(e) {
		$(this).removeClass(e.data.className + '_focus');
		e.data.onfocusout(e);
	}).bind('click', this, function(e) {
		if (e.data.onclick)
			e.data.onclick(e);
	});

	if (this.style.readOnly == 'true') {
		this.widget.prop('readOnly', true).addClass(this.className + '_readOnly');
	} else {
		this.widget.removeProp('readOnly').removeClass(this.className + '_readOnly');
	}
	this.onEvent = true;

	if (this.style.width)
		this.widget.width(this.style.width);
	if (this.style.value)
		this.widget.val(this.prefix + this.style.value);
	if (this.style.maxLength)
		this.widget.attr('maxLength', this.style.maxLength);
	if (this.style.imeMode)
		this.widget.css('imeMode', this.style.imeMode);
	if (this.style.autoComplete) {
		JSV.register(new AutoComplete(this.widget.get(0), this), this, 'autoComplete');
	}
	if (this.style.numText) {
		$('<span></span>').addClass(this.className + '_numText').text(this.style.numText).appendTo(parent);
	}
}
MarketNumberFieldEditor.prototype.setValue = function(value) {
	if (value != null && value != '') {
		value = this.inputNumberFormat(value);
		this.setOriginal(this.prefix + value);
	} else if (this.style.example) {
		this.setExam();
	} else {
		this.widget.val('');
	}
	JSV.notify(value, this);
}
MarketNumberFieldEditor.prototype.inputNumberFormat = function(value) {
	value = JSV.getLang('DataFormat','moneyType2')+this.comma(this.uncomma(value));
	return value;
}
MarketNumberFieldEditor.prototype.comma = function(str) {
	str = String(str);
	return str.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
}
MarketNumberFieldEditor.prototype.uncomma = function(str) {
	str = String(str);
	return str.replace(/[^\d]+/g, '');
}
MarketNumberFieldEditor.prototype.getValue = function() {
	if (this.style.example && this.onEvent) {
		return '';
	}
	var value = $.trim(this.uncomma(this.widget.val()));
	return parseInt(value || 0);
}
MarketNumberFieldEditor.prototype.focus = function() {
	this.widget.focus();
}
MarketNumberFieldEditor.prototype.onfocus = function() {
	if (this.preFocus) {
		this.preFocus();
	}
	if (this.style.example && this.onEvent) {
		this.setOriginal('');
	}
}
MarketNumberFieldEditor.prototype.onfocusout = function() {
	if (this.preFocusout) {
		this.preFocusout();
	}
	if (this.style.example && this.getValue() == '') {
		this.setExam();
	}
}
MarketNumberFieldEditor.prototype.setMaxlength = function(maxLength) {
	this.widget.attr('maxLength', maxLength);
}
MarketNumberFieldEditor.prototype.setReadOnly = function(value) {
	if (value) {
		this.widget.prop('readOnly', true).addClass(this.className + '_readOnly');
	} else {
		this.widget.removeProp('readOnly').removeClass(this.className + '_readOnly');
	}
}
MarketNumberFieldEditor.prototype.setEditable = function(value) {
	this.widget.attr('disabled', !value);
}
MarketNumberFieldEditor.prototype.setExam = function() {
	this.widget.val(this.style.example).removeClass(this.className + '_original').addClass(this.className + '_exam');
	this.onEvent = true;
}
MarketNumberFieldEditor.prototype.setOriginal = function(value) {
	this.widget.val(value).removeClass(this.className + '_exam').addClass(this.className + '_original');
	this.onEvent = false;
}
MarketNumberFieldEditor.prototype.autoComplete = function(value, observable) {
	this.setValue(value);
}
MarketNumberFieldEditor.prototype.clear = function() {
	this.widget.remove();
}
MarketNumberFieldEditor.prototype.setVisbie = function() {
	this.widget.parent().parent().remove();
}
/**
 * 현재가격, 즉시구매가를 입력받는다
 * 가격정보를 출력한다.
 */
function MarketNumberRatio(parent, style) {
	var _this = this;
	this.style = style || {};
	this.prefix = this.style.prefix || '';
	this.className = this.style.className || 'TextFieldEditor';
	this.widget = $('<input type="text">').addClass(this.className).appendTo(parent).bind('keyup', this, function(e) {
		$(this).val(_this.inputNumberFormat($(this).val()));
		if (e.data.onkeyup)
			e.data.onkeyup(e);
		if (e.keyCode == 13)
			return false;
	}).bind('focus', this, function(e) {
		$(this).addClass(e.data.className + '_focus');
		e.data.onfocus(e);
	}).bind('blur', this, function(e) {
		$(this).removeClass(e.data.className + '_focus');
		e.data.onfocusout(e);
	}).bind('click', this, function(e) {
		if (e.data.onclick)
			e.data.onclick(e);
	});

	if (this.style.readOnly == 'true') {
		this.widget.prop('readOnly', true).addClass(this.className + '_readOnly');
	} else {
		this.widget.removeProp('readOnly').removeClass(this.className + '_readOnly');
	}
	this.onEvent = true;

	if (this.style.width)
		this.widget.width(this.style.width);
	if (this.style.value)
		this.widget.val(this.prefix + this.style.value);
	if (this.style.maxLength)
		this.widget.attr('maxLength', this.style.maxLength);
	if (this.style.imeMode)
		this.widget.css('imeMode', this.style.imeMode);
	if (this.style.autoComplete) {
		JSV.register(new AutoComplete(this.widget.get(0), this), this, 'autoComplete');
	}
	if (this.style.numText) {
		$('<span></span>').addClass(this.className + '_numText').text(this.style.numText).appendTo(parent);
	}
}
MarketNumberRatio.prototype.setValue = function(value) {
	if (value != null && value != '') {
		value = JSV.getLang('DataFormat','moneyType3')+this.inputNumberFormat(value);
		this.setOriginal(this.prefix + value);
	} else if (this.style.example) {
		this.setExam();
	} else {
		this.widget.val('');
	}
	JSV.notify(value, this);
}
MarketNumberRatio.prototype.inputNumberFormat = function(value) {
	value = JSV.getLang('DataFormat','moneyType3')+this.limit(this.uncomma(value));
	return value;
}
MarketNumberRatio.prototype.comma = function(str) {
	str = String(str);
	return str.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
}
MarketNumberRatio.prototype.limit = function(str) {
	str = String(str);
	return str.replace(/[^\d]?$|^100$/g, '');
}
MarketNumberRatio.prototype.uncomma = function(str) {
	str = String(str);
	return str.replace(/[^\d]+/g, '');
}
MarketNumberRatio.prototype.getValue = function() {
	if (this.style.example && this.onEvent) {
		return '';
	}
	var value = $.trim(this.uncomma(this.widget.val()));
	return parseInt(value || 0);
}
MarketNumberRatio.prototype.focus = function() {
	this.widget.focus();
}
MarketNumberRatio.prototype.onfocus = function() {
	if (this.preFocus) {
		this.preFocus();
	}
	if (this.style.example && this.onEvent) {
		this.setOriginal('');
	}
}
MarketNumberRatio.prototype.onfocusout = function() {
	if (this.preFocusout) {
		this.preFocusout();
	}
	if (this.style.example && this.getValue() == '') {
		this.setExam();
	}
}
MarketNumberRatio.prototype.setMaxlength = function(maxLength) {
	this.widget.attr('maxLength', maxLength);
}
MarketNumberRatio.prototype.setReadOnly = function(value) {
	if (value) {
		this.widget.prop('readOnly', true).addClass(this.className + '_readOnly');
	} else {
		this.widget.removeProp('readOnly').removeClass(this.className + '_readOnly');
	}
}
MarketNumberRatio.prototype.setEditable = function(value) {
	this.widget.attr('disabled', !value);
}
MarketNumberRatio.prototype.setExam = function() {
	this.widget.val(this.style.example).removeClass(this.className + '_original').addClass(this.className + '_exam');
	this.onEvent = true;
}
MarketNumberRatio.prototype.setOriginal = function(value) {
	this.widget.val(value).removeClass(this.className + '_exam').addClass(this.className + '_original');
	this.onEvent = false;
}
MarketNumberRatio.prototype.autoComplete = function(value, observable) {
	this.setValue(value);
}
MarketNumberRatio.prototype.clear = function() {
	this.widget.remove();
}
MarketNumberRatio.prototype.setVisbie = function() {
	this.widget.parent().parent().remove();
}
function MarketNumberViewer(parent, style) {
	var _this = this;
	this.style = style || {};
	this.className = this.style.className || 'MarketNumberViewer';
	this.seq = JSV.SEQUENCE++;
	var spanStr = '<span class="'+this.className+'" id="Err'+this.className+this.seq+'">\
		<span class="textSpan" id="TextViewTextSpan' + this.seq + '"></span>\
		<span class="valueSpan" id="TextViewValueSpan' + this.seq + '"></span>\
	</span>';

	try {
		this.widget = $(spanStr).addClass(this.className).appendTo(parent);
	} catch (e) {
		$(parent).append(spanStr);
		this.widget = $(parent).find('#Err' + this.className + this.seq);
	}
}
MarketNumberViewer.prototype.setValue = function(value) {
	if (this.style.text) {
		this.widget.find('#TextViewTextSpan' + this.seq).text(this.style.text);
	}
	if (value != null && new String(value).trim()) {
		this.textValue = (this.style.defaultValue == value && this.style.defaultDisplay)
				? this.style.defaultDisplay
				: JSV.getLocaleStr(value);
		this.widget.find('#TextViewValueSpan' + this.seq).text(JSV.getLang('DataFormat','moneyType2')+this.comma(value) + (this.style.suffix || ''));
	}else{
		this.widget.find('#TextViewValueSpan' + this.seq).html('&nbsp;');
	}
}
MarketNumberViewer.prototype.inputNumberFormat = function(value) {
	value = this.comma(this.uncomma(value));
	return value;
}
MarketNumberViewer.prototype.comma = function numberWithCommas(x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}
MarketNumberViewer.prototype.setVisbie = function() {
	this.widget.parent().parent().remove();
}
MarketNumberViewer.prototype.getValue = function() {
	return this.textValue;
}
