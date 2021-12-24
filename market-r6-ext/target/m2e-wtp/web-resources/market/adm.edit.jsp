<%@ include file="/sys/jsv/template/template.head.jsp" %>
<%@ include file="/market/config.jsp" %>
<%@ include file="/market/jsv-market-ext.jsp" %>
<%-------------------------------------------------- 
			사내마켓 admin 수정페이지
---------------------------------------------------%>
<script type="text/javascript">
JSV.Block(function() {
	var model = <%ctx.execute("MarketItemAdmin.ReadByAdmin");%>;
	var useCtgr = <%=useCtgr(moduleParam)%>;
	var template = '<template catalog="/market/catalog.xml.jsp">\
		<header label="<kfmt:message key="market.040"/>"/>\
		<fields columns="100px,,,100px,100px,,100px," type="write" class="Array">\
			<field property="title.write"/>\
			<field property="folder.write"/>\
			<field property="currentPrice.edit"/>\
			<field property="dateTerm.write"/>\
			<field property="immediatePurchase.write"/>\
			<field property="minimumAmount.write"/>\
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
	t.setValue(model);
	
	
	<%-- 기부게시글일시 즉시구매가 컴포넌트 HIDE --%>
	var donate = model.isDonate;
	var widget = t.getChild('immediatePurchase').widget.get(0);
	if(donate == true){
		ItemViewer.hideTR(widget);
	}

	<%-- 취소버튼 --%>
	var cancel = new KButton(t.layout.mainHeadRight,<fmt:message key="btn.pub.cancel_round"/>);
	cancel.onclick = function() {
		JSV.doGET('adm.popup.read.jsp?id=@{id}');
	};
	
	<%-- 수정버튼 --%>
	var Modify = new KButton(t.layout.mainHeadRight,<fmt:message key="btn.doc.001"/>);
	Modify.onclick = function() {
			t.action.setRedirect(JSV.setUrlAlert('/market/adm.popup.read.jsp?id=@{id}'));
			t.submit(JSV.getModuleUrl('/jsl/MarketItemAdmin.DoUpdateByAdmin.jsl'));
	}
});
</script>
<%@ include file="/sys/jsv/template/template.body.jsp" %>
<div id="main"></div>
<%@ include file="/sys/jsv/template/template.tail.jsp" %>