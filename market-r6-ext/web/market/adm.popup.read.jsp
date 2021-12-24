<%@ include file="/sys/jsv/template/template.head.jsp" %>
<%@ include file="/market/config.jsp" %>
<%@ include file="/market/jsv-market-ext.jsp" %>
<% checkAdmin(moduleParam); %>
<%-------------------------------------------------- 
			사내마켓 admin 팝업 페이지
---------------------------------------------------%>
<script type="text/javascript">
JSV.Block(function(){
	var template = '<template catalog="/market/catalog.xml.jsp">\
		<header label="<fmt:message key="doc.023"/>"/>\
		<fields class="Array" columns="100px,,,100px,100px,,100px," type="read">\
			<field property="title.readadmin"/>\
			<field property="currentPrice.read"/>\
			<field property="immediatePurchase.read"/>\
			<field property="content.read"/>\
			<field property="attachments.read"/>\
			<field property="id.hidden"/>\
		</fields>\
		<opinions class="Array">\
				<opinion property="opinions.read"/>\
				<opinion property="opinions.write"/>\
		</opinions>\
		</template>';
		
    var model = <%ctx.execute("MarketItemAdmin.ReadByAdmin");%>;
	var t = new ItemTemplate(document.getElementById('main'),template);
	t.setValue(model);

	<%-- 입찰자 조회 버튼 --%>
	var biddingView = new KButton(t.viewer.getTitleBtnArea(), <kfmt:message key="btn.market.002"/>);
	biddingView.onclick = function() {
		var id = JSV.getParameter('id');
		var targetUrl = '/market/adm.popup.biddingView.jsp?id=' + id;
		var url = JSV.getContextPath(JSV.getModuleUrl(targetUrl));
		window.open(url,"width=530, height=690, status=no, toolbar=no, scrollbars=no, menubar=no,location=no, resizable=no");
	}
	
	<%-- 수정 버튼 --%>
	var edit = new KButton(t.viewer.getTitleBtnArea(), <fmt:message key="btn.pub.modify_round"/>);
	edit.onclick = function() {
		var id = JSV.getParameter('id');
		JSV.doGET(JSV.getModuleUrl('adm.edit.jsp?id=@{id}'));
	}
});
</script>
<%@ include file="/sys/jsv/template/template.body.jsp" %>
<div id="main"></div>
<%@ include file="/sys/jsv/template/template.tail.jsp" %>