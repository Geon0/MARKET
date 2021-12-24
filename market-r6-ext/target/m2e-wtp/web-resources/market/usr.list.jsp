<%@ include file="/sys/jsv/template/template.head.jsp" %>
<%@ include file="/market/jsv-market-ext.jsp" %>
<%@page import="com.kcube.market.MarketItemSql"%>
<%
  MarketItemSql mk = new MarketItemSql(moduleParam, ctx.getLong("tr", null), ctx.getParameter("com.kcube.doc.list"), false);
%>
<%-------------------------------------------------- 
			사내마켓 목록 페이지
---------------------------------------------------%>
<script type="text/javascript">
JSV.Block(function () {
	var template = '<template name="com.kcube.doc.list" catalog="/market/catalog.xml.jsp" listType="ALL">\
			<header label="<kfmt:message key="market.001"/>"/>\
			<columns class="Array">\
				<column property="id.list"/>\
				<column property="status.list"/>\
				<column property="type.list"/>\
				<column property="title.list"/>\
				<column property="author.list"/>\
				<column property="dateTerm.list"/>\
				<column property="rgstDate.list"/>\
				<column property="currentPrice.list"/>\
				<column property="immediatePurchase.list"/>\
				<column property="readCnt.list"/>\
				<column property="attachments.list"/>\
			</columns>\
			<search class="Array" periodColumn="rgstDate">\
				<option property="title.list"/>\
				<option property="author.list"/>\
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
		
	<%-- 사내마켓 목록 Table --%>
	var t = new TableTemplate(document.getElementById('main'), template);
	t.setDataUrl('/jsl/MarketItemUser.ListByUser.json', 'ts');
	
	<%-- 사내마켓 즐겨찾기 --%>
	var folderName = FoldersInformationViewer({'folderIds':JSV.getParameter('tr'), 'defaultText':'<kfmt:message key="market.039"/>'});
	ScrapMenu.favoriteList(t.layout.titleLeft, '<%=com.kcube.market.MarketItemHistory.ALIMI_MARKET%>', 'com.kcube.market.MarketItemConfig.fvrtIndexUrl', JSV.getParameter('tr'), folderName);

	<%-- 사내마켓 거래,기부 게시글 종류 CHECK --%>
	var postType = JSV.getParameter('postType') || -1;
	var options = '<kfmt:message key="market.checkType.001"/>';
	var selectPostType = new DropDownGroupFieldEditor(t.layout.mainHeadRight,{'options':options});
	selectPostType.onclick = function(value) {
		JSV.setState('postType', value);
		JSV.setState('com.kcube.doc.list', null);
		JSV.doGET('usr.list.jsp');
	}
	selectPostType.setValue(postType);
	
	<%-- 사내마켓 추천, 인기입찰 CHECKBOX --%>
	var checkBox = JSV.getParameter('checkBox') || '-1';
	var checkOptions = '<kfmt:message key="market.checkbox.001"/>';
	var selectCheckBox = new CheckBoxGroupFieldEditor(t.layout.mainHeadCenter, {'options': checkOptions});
	selectCheckBox.onchange = function(value){	
		checkBox = this.getValue();
		JSV.setState('checkBox', checkBox);
		JSV.setState('com.kcube.doc.list', null);
	    JSV.doGET('usr.list.jsp');
	}
	var checkBoxArray=checkBox.split(',');
	selectCheckBox.setValue(checkBoxArray);
	
	<%-- 글 등록 버튼 --%>
	var write = new KButton(t.layout.titleRight, <kfmt:message key="btn.market.001"/>);
	write.onclick = function() {
		JSV.doGET('usr.write.jsp');
	};
	
});
</script>
<%@ include file="/sys/jsv/template/template.body.jsp" %>
<div id="main"></div>
<%@ include file="/sys/jsv/template/template.tail.jsp" %>