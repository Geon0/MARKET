<%@ include file="/sys/jsv/template/template.head.jsp" %>
<%@page import="com.kcube.market.MarketItemSql"%>
<%
  MarketItemSql mk = new MarketItemSql(moduleParam, ctx.getLong("tr", null), ctx.getParameter("com.kcube.doc.list"), false);
%>
<%@ include file="/market/jsv-market-ext.jsp" %>
<%-------------------------------------------------- 
			사내마켓 조회 페이지
---------------------------------------------------%>
<script type="text/javascript">
JSV.Block(function () {
	var template = '<template name="com.kcube.doc.list" catalog="/market/catalog.xml.jsp" listType="LIST">\
			<header label="<kfmt:message key="market.036"/>"/>\
			<columns class="Array">\
				<column property="id.list"/>\
				<column property="status.list"/>\
				<column property="title.list"/>\
				<column property="seller.list"/>\
				<column property="purchasePrice.list"/>\
				<column property="purchaseTime.list"/>\
				<column property="buyButton.list"/>\
			</columns>\
			<search class="Array" periodColumn="rgstDate">\
				<option property="title.list"/>\
				<option property="seller.list"/>\
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
 
	<%-- 사내마켓 나의 구매 목록 --%>
	var t = new TableTemplate(document.getElementById('main'), template);
	t.setDataUrl('/jsl/MarketItemOwner.BuyList.json', 'ts');
	
	<%-- 사내마켓 게시물 상태 CHECK --%>
	var status = JSV.getParameter('status') || -1;
	var options = '<kfmt:message key="market.checkType.003"/>';
	var selectStatus = new DropDownGroupFieldEditor(t.layout.mainHeadRight,{'options':options});
	selectStatus.onclick = function(value) {
		JSV.setState('status', value);
		JSV.setState('com.kcube.doc.list', null);
		JSV.doGET('own.buy.list.jsp');
	}
	selectStatus.setValue(status);
});
<%-- 거래완료 처리 --%>
function doDealing(url, element){
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