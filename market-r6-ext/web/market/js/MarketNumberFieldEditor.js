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
