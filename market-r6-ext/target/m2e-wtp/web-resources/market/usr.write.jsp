<%@ include file="/sys/jsv/template/template.head.jsp" %>
<%@ include file="/market/config.jsp" %>
<%@ include file="/market/jsv-market-ext.jsp" %>
<script type="text/javascript">
<%-------------------------------------------------- 
			사내마켓 등록 페이지
---------------------------------------------------%>
JSV.Block(function() {
	var template = '<template catalog="/market/catalog.xml.jsp">\
		<header label="<kfmt:message key="market.041"/>"/>\
		<fields columns="100px,,,100px,100px,,100px," type="write" class="Array">\
			<field property="title.write"/>\
			<field property="folder.write"/>\
			<field property="dateTerm.write" colSpan="4"/>\
			<field property="isDonate.write" colSpan="4"/>\
			<field property="startPrice.write" colSpan="4"/>\
			<field property="immediatePurchase.write" colSpan="4"/>\
			<field property="minimumCheck.write"/>\
			<field property="minimumAmount.write" colSpan="4"/>\
			<field property="ratePrice.write" colSpan="4"/>\
			<field property="content.write"/>\
			<field property="tags.write"/>\
			<field property="attachments.write"/>\
			<field property="foldingBar.writeDetail"/>\
			<field property="securities.write"/>\
			<field property="id.hidden"/>\
		</fields>\
	</template>';		
	
	var t = new ItemTemplate(document.getElementById('main'), template);
	t.viewer.defaultFolded('writeDetail');

	<%-- 최소입찰가격 컴포넌트 show hide  --%>
	var mA = t.getChild('minimumAmount').widget.get(0);
	var rP = t.getChild('ratePrice').widget.get(0);
	ItemViewer.hideTD(mA);
	ItemViewer.hideTD(rP);
	t.getChild('minimumCheck').onchange = function(value) {
		if(value==0){
			ItemViewer.showTD(mA);
			ItemViewer.hideTD(rP);
		}else{
			ItemViewer.showTD(mA);
			ItemViewer.showTD(rP);
		}
	}
	
	<%-- isDonate 값에 따라 immediatePurchase 컴포넌트 show hide  --%>
	t.getChild('isDonate').onclick = function() {
		var donate = this.getValue();
		var widget = t.getChild('immediatePurchase').widget.get(0);
		var ratio = t.getChild('minimumCheck').widget;
		if(donate == true){
			ItemViewer.hideTD(widget);
		}else{
			ItemViewer.showTD(widget);
		}
	}

	<%-- 비율지정 최소입찰가격  --%>
	t.getChild('ratePrice').onfocusout = function(value) {
		var rP = t.getChild('ratePrice').getValue();
		var iP =  t.getChild('immediatePurchase').getValue();
		var result = (iP * rP) / 100;
		t.getChild('minimumAmount').setValue(result);
	}
	
	<%-- 취소버튼  --%>
	var cancel = new KButton(t.layout.mainHeadRight,<fmt:message key="btn.pub.cancel_round"/>);
	cancel.onclick = function() {
		JSV.doGET('usr.list.jsp');
	};
	
	<%-- PreWrite 미존재로 인한 ScrtLevel 값 setting  --%>
	t.getChild('scrt').setValue(<%=getScrtLevel(moduleParam)%>);
	
	
	<%-- 등록버튼  --%>
	var register = new KButton(t.layout.mainHeadRight,<fmt:message key="btn.doc.001"/>);
	register.onclick = function() {
		var start = t.getChild('dateTerm').getStartDate();
		var end = t.getChild('dateTerm').getEndDate();
		var day = new Date();
		if(t.validate('title,folder')){
			if(start>end){
				alert('<kfmt:message key="market.pub.009"/>');
			}else if (start<day){
				alert('<kfmt:message key="market.pub.010"/>');
			}else{
				t.action.setRedirect(JSV.setUrlAlert('/market/usr.list.jsp'));
				t.submit('/jsl/MarketItemUser.DoRegister.jsl');
			}
		}
	}
});
<%-- endTime 기본값 setting을 위한 setValue --%>
DropDownGroupFieldEditor.prototype.setValue = function(value) {
	if (value != null) {
		var an = this.ul.children().removeClass('selected').find('a[value=' + '"' + value + '"' + ']');
		this.txtSpan.html(value);
		an.parent().addClass('selected');
	}
}
</script>
<%@ include file="/sys/jsv/template/template.body.jsp" %>
<div id="main"></div>
<%@ include file="/sys/jsv/template/template.tail.jsp" %>