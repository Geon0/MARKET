<%@ include file="/sys/jsv/template/template.head.jsp" %>
<%@ include file="/market/config.jsp" %>
<%@ include file="/market/jsv-market-ext.jsp" %>
<%@page import="com.kcube.market.MarketItemSql"%>
<%
  MarketItemSql mk = new MarketItemSql(moduleParam, ctx.getLong("tr", null), ctx.getParameter("com.kcube.doc.list"), false);
%>
<% checkAdmin(moduleParam); %>
<%-------------------------------------------------- 
			사내마켓 입찰자 조회 페이지
---------------------------------------------------%>
<script type="text/javascript">
JSV.Block(function () {
	var template = '<template name="com.kcube.doc.list" catalog="/market/catalog.xml.jsp">\
		<header label="<kfmt:message key="market.028"/>"/>\
		<columns class="Array">\
			<column property="biddingName.list"/>\
			<column property="biddingTime.list"/>\
			<column property="biddingPrice.list"/>\
			<column property="delButton.list"/>\
		</columns>'
		<% if (mk.isCountCondition()) { %>
 			+ '<footer/>'
 			+ '<count/>'
 			+ '<pageMover/>'
		<% } else { %>
 			+ '<footer count="disabled"/>'
		<% } %>
			+ '<rows name="com.kcube.doc.rowsPerPage"/>\
	</template>';
	
	<%-- id 세팅 --%>	
	if (JSV.getParameter('id')) {
		JSV.setState('id', JSV.getParameter('id'));
	}	
		
    var t = new TableTemplate(document.getElementById('main'), template);
    t.setDataUrl('/jsl/MarketItemAdmin.getBidInfo.json', 'ts');
	
	<%-- 닫기 버튼 --%>
	var cancel = new KButton(t.layout.bottomCenterArea,<kfmt:message key="market.btn.003"/>);
	cancel.onclick = function(){
		opener.location.reload();
		window.close();
	}
});
	<%-- 입찰 취소 버튼 --%>
function delBidding(url, element){
	 if (confirm('<kfmt:message key="market.pub.007"/>')){
	 	var f = new ItemForm();
	 	var l = location.toString();
	 	var id = JSV.getParameter('id');
	 	f.setRedirect(JSV.getModuleUrl('/market/adm.popup.biddingView.jsp?id=' + id));
	 	f.submit(JSV.mergeXml(url, element));
	 }
}
</script>
<%@ include file="/sys/jsv/template/template.body.jsp" %>
<div id="main"></div>
<%@ include file="/sys/jsv/template/template.tail.jsp" %>