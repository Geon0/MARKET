<%@ include file="/sys/jsv/template/template.head.jsp" %>
<%@ include file="/market/jsv-market-ext.jsp" %>
<%@ include file="/market/config.jsp" %>
<script type="text/javascript">
<%-------------------------------------------------- 
			사내마켓 조회 페이지
---------------------------------------------------%>
	JSV.Block(function() {
		var template = '<template catalog="/market/catalog.xml.jsp">\
			<header label="<fmt:message key="doc.023"/>"/>\
			<fields class="Array" columns="100px,,,100px,100px,,100px," type="read">\
				<field property="title.read"/>\
				<field property="currentPrice.read" colSpan="4"/>\
				<field property="minimumAmount.read" colSpan="4"/>\
				<field property="immediatePurchase.read"/>\
				<field property="content.read"/>\
				<field property="attachments.read"/>\
				<field property="currentOwner.hidden"/>\
				<field property="id.hidden"/>\
			</fields>\
			<opinions class="Array">\
				<opinion property="opinions.read"/>\
				<opinion property="opinions.write"/>\
			</opinions>'
			<% if (isDuplex(moduleParam)) { %>
			+ '<duplex property="duplex.read"/>'
			<% } %>
		+ '</template>';
		
		<%-- 사내마켓 ReadByUser --%>
		var model = <%ctx.execute("MarketItemUser.ReadByUser");%>;
		var t = new ItemTemplate(document.getElementById('main'), template);
		t.setValue(model);
		
		<%-- 사내마켓 목록버튼 --%>
	 	t.viewer.setListBtn(function() {
			JSV.doGET('usr.list.jsp');
		});
	 	
	 	<%-- 사내마켓 버튼 --%>
 		var tds = t.viewer.getTitleBtnArea();
 		
 		<%-- 즐겨찾기 버튼  --%>
 		ScrapMenu.favoriteItem(tds, '<%=com.kcube.market.MarketItemHistory.ALIMI_MARKET%>', 'com.kcube.market.MarketItemConfig.fvrtIndexUrl', model.id, model.title);
 		
 		<%-- 기부 게시글일시 즉시구매영역 제거  --%>
 		if(model.immediatePurchase == 0){
 			t.getChild('immediatePurchase').setVisbie();
 		}
 		
 		<%-- 게시글 소유자일때  --%>
 		if(model.currentOwner==true){
 			var imstart = new KButton(tds,<fmt:message key="btn.doc.043"/>);
 			var edit = new KButton(tds, <fmt:message key="btn.doc.005"/>);
			var delBtn = new KButton(tds, <fmt:message key="btn.pub.delete_icon"/>);
 		if(model.status==3000){
 			imstart.onclick = function(){
 				if(t.getProperty('status') == <%=com.kcube.market.MarketItem.REGISTERED_STATUS%>)
 					url='/jsl/MarketItemOwner.DoSelling.jsl?id=@{id}';
 					t.action.setRedirect(JSV.setUrlAlert('/market/usr.list.jsp'),'<kfmt:message key="market.pub.003"/>');
					t.submit(url);
 			}
 		}else if(model.status == <%=com.kcube.market.MarketItem.SELLING_STATUS%>){
 			var imEnd = new KButton(tds,<fmt:message key="btn.doc.045"/>);
 			imstart.hide();
 			delBtn.hide();
 		}else if(model.status == <%=com.kcube.market.MarketItem.TRADEWAIT_STATUS%> || model.status==<%=com.kcube.market.MarketItem.BUYCOMPELETE_STATUS%>){
 			imstart.hide();
 			edit.hide();
 			delBtn.hide();
 		}else if (model.status == <%=com.kcube.market.MarketItem.SELLCOMPLELETE_STATUS%>){
 			imstart.hide();
 			edit.hide();
 		}
 		<%-- 수정 버튼 클릭  --%>
			edit.onclick = function() {
				JSV.doGET('usr.edit.jsp?id=@{id}');
			}
		<%-- 경매 종료 버튼 클릭  --%>
			imEnd.onclick = function() {
				if (confirm('<kfmt:message key="market.pub.004"/>')) {
					if (t.getProperty('status') == <%=com.kcube.market.MarketItem.SELLING_STATUS%>){
						url='/jsl/MarketItemOwner.DoEndByOwner.jsl?id=@{id}';
						t.action.setRedirect(JSV.setUrlAlert('/market/usr.list.jsp'),'<kfmt:message key="market.pub.005"/>');
						t.submit(url);
					}else{
						alert('<kfmt:message key="market.pub.018"/>');
					}
				}
			}
		<%-- 삭제 버튼 클릭  --%>
			delBtn.onclick = function() {
				if (confirm('<fmt:message key="pub.009"/>')) {
					if (t.getProperty('status') == <%=com.kcube.market.MarketItem.REGISTERED_STATUS%>){
						url='/jsl/MarketItemOwner.DoDeleteByOwner.jsl?id=@{id}';
						t.action.setRedirect(JSV.setUrlAlert('/market/usr.list.jsp'),'<fmt:message key="modl.bldr.071"/>');
						t.submit(url);
					}else{
						alert('<kfmt:message key="market.pub.019"/>');
					}
				}
			}
		<%-- 게시글 소유자가 아닐때  --%>
		}else if(model.currentOwner==false){
			if(model.status == <%=com.kcube.market.MarketItem.SELLING_STATUS%>){
				var imPurchase = new KButton(tds,<fmt:message key="btn.doc.046"/>);
				var bidding = new KButton(tds,<fmt:message key="btn.doc.047"/>);
			}
		<%-- 즉시 구매 버튼  --%>
		imPurchase.onclick = function(){
				url='/jsl/MarketItemUser.imPurchase.jsl?id=@{id}';
				t.action.setRedirect(JSV.setUrlAlert('/market/usr.list.jsp'),'<kfmt:message key="market.pub.008"/>');
				t.submit(url);
			}
		<%-- 입찰하기 버튼  --%>
		bidding.onclick = function(){
				var id = JSV.getParameter('id');
				var targetUrl = '/market/usr.popup.bidding.jsp?id=' + id + '&currentPrice=' + model.currentPrice + '&minimumAmount=' + model.minimumAmount + 'immediatePurchase=' + model.immediatePurchase;
				var url = JSV.getContextPath(JSV.getModuleUrl(targetUrl));
				window.open(url,"width=530, height=690, status=no, toolbar=no, scrollbars=no, menubar=no,location=no, resizable=no");
			}
		}
		<%-- 기부게시글 즉시구매 버튼 hide처리  --%>
 		if(model.isDonate == true){
 			imPurchase.hide();
 		}
	});	
</script>
<%@ include file="/sys/jsv/template/template.body.jsp" %>
<div id="main${PAGE_ID}"></div>
<%@ include file="/sys/jsv/template/template.tail.jsp" %>