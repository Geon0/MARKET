<%@ include file="/sys/jsv/template/template.head.jsp" %>
<%@page import="com.kcube.market.MarketItemSql"%>
<%
  MarketItemSql mk = new MarketItemSql(moduleParam, ctx.getLong("tr", null), ctx.getParameter("com.kcube.doc.list"), false);
%>
<%@ include file="/market/jsv-market-ext.jsp" %>
<script type="text/javascript">
JSV.Block(function () {
	var template = '<template name="com.kcube.doc.list" catalog="/market/catalog.xml.jsp" listType="LIST">\
			<header label="<kfmt:message key="market.033"/>"/>\
			<columns class="Array">\
				<column property="id.list"/>\
				<column property="status.list"/>\
				<column property="title.list"/>\
				<column property="startTime.list"/>\
				<column property="endTime.list"/>\
				<column property="currentPrice.list"/>\
				<column property="immediatePurchase.list"/>\
				<column property="attachments.list"/>\
				<column property="buyer.list"/>\
				<column property="sellButton.list"/>\
			</columns>\
			<search class="Array" periodColumn="rgstDate">\
				<option property="title.list"/>\
				<option property="buyer.list"/>\
				<option property="tags.search"/>\
				<option property="id.search"/>\
			</search>'
			<% if (mk.isCountCondition()) { %>
		 		+ '<footer/>'
		 		+ '<count/>'
		 		+ '<pageMover/>'
			<% } else { %>
		 		+ '<footer count="disabled"/>'
			<% } %>
				+ '<rows name="com.kcube.doc.rowsPerPage"/>\
		</template>';

	<%-- tr 세팅 --%>
	if (JSV.getParameter('tr')) {
		JSV.setState('tr', JSV.getParameter('tr'));
	}
	
	<%-- 사내마켓 나의 판매 목록 --%>
	var t = new TableTemplate(document.getElementById('main'), template);
	t.setDataUrl('/jsl/MarketItemOwner.ListByOwner.json', 'ts');
	
	<%-- 사내마켓 게시물 상태 CHECK --%>
	var status = JSV.getParameter('status') || -1;
	var options = '<kfmt:message key="market.checkType.002"/>';
	var selectStatus = new DropDownGroupFieldEditor(t.layout.mainHeadRight,{'options':options});
	selectStatus.onclick = function(value) {
		JSV.setState('status', value);
		JSV.setState('com.kcube.doc.list', null);
		JSV.doGET('own.sell.list.jsp');
	}
	selectStatus.setValue(status);
});
<%-- 판매완료 처리 --%>
function endSell(url, element){
 if (confirm('<kfmt:message key="market.pub.006"/>')){
 	var f = new ItemForm();
 	var l = location.toString();
 	f.setRedirect(i < 0 ? l : l.substring(0, i));
 	f.submit(JSV.mergeXml(url, element));
 		}
 	}
</script>
<%@ include file="/sys/jsv/template/template.body.jsp" %>
<div id="main"></div>
<%@ include file="/sys/jsv/template/template.tail.jsp" %>