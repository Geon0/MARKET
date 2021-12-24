<%@ include file="/sys/jsv/template/template.head.jsp" %>
<%@ include file="/market/config.jsp" %>
<%@ include file="/market/jsv-market-ext.jsp" %>
<script type="text/javascript">
<%-------------------------------------------------- 
			사내마켓 수정 페이지
---------------------------------------------------%>
JSV.Block(function() {
	var model = <%ctx.execute("MarketItemUser.ReadByUser");%>;
	var template = '<template catalog="/market/catalog.xml.jsp">\
		<header label="<kfmt:message key="market.040"/>"/>\
		<fields columns="100px,,,100px,100px,,100px," type="write" class="Array">';
			if (model.status == <%=com.kcube.market.MarketItem.REGISTERED_STATUS%>){
				template += '<jsp:include page="usr.edit.form" flush="true"/>';
			}else{
				template += '<jsp:include page="usr.edit.selling.form" flush="true"/>';
			}
			template += '</fields>\
		</template>';
	
	var t = new ItemTemplate(document.getElementById('main'), template);
	t.setValue(model);
	
	<%-- 취소 버튼 --%>
	var cancel = new KButton(t.layout.mainHeadRight,<fmt:message key="btn.pub.cancel_round"/>);
	cancel.onclick = function() {
		JSV.doGET('usr.list.jsp');
	};
	
	<%-- 수정 버튼 --%>
	var Modify = new KButton(t.layout.mainHeadRight,<fmt:message key="btn.doc.001"/>);
	Modify.onclick = function() {
		var OrginStart = model.startTime;
		var inputStart = t.getChild('dateTerm').getStartDate();
		var inputEnd = t.getChild('dateTerm').getEndDate();
		var day = new Date();
		if(OrginStart>inputStart){
			alert('<kfmt:message key="market.pub.011"/>');
		}else if (inputEnd < day){
			alert('<kfmt:message key="market.pub.010"/>');
		}else{
			t.action.setRedirect(JSV.setUrlAlert('/market/usr.list.jsp'));
			t.submit('/jsl/MarketItemOwner.DoUpdateByOwner.jsl');
		}
	}
});
</script>
<%@ include file="/sys/jsv/template/template.body.jsp" %>
<div id="main"></div>
<%@ include file="/sys/jsv/template/template.tail.jsp" %>