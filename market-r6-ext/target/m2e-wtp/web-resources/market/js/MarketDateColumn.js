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
