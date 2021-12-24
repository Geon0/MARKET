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
