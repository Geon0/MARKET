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
