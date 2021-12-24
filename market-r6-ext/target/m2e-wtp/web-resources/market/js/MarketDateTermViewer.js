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
