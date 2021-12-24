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
