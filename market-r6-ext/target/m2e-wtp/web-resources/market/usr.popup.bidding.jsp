<%@ include file="/sys/jsv/template/template.head.jsp" %>
<%@ include file="/market/jsv-market-ext.jsp" %>
<script type="text/javascript">
<%-------------------------------------------------- 
			사내마켓 입찰 페이지
---------------------------------------------------%>
JSV.Block(function() {
	var template = '<template catalog="/market/catalog.xml.jsp">\
		<header label="<fmt:message key="doc.252"/>"/>\
		<fields class="Array" columns="80,,80," type="write">\
			<field property="currentPrice.read"/>\
			<field property="minimumAmount.read"/>\
			<field property="biddingPrice.write"/>\
			<field property="itemId.hidden"/>\
			<field property="id.hidden"/>\
		</fields>\
		</template>';
		
	<%-- 입찰에 필요한 정보 가져오기 --%>	
    var model = <%ctx.execute("MarketItemUser.ReadByUser");%>;
	var t = new ItemTemplate(document.getElementById('main'), template);
	t.setValue(model);

	<%-- 입찰에 필요한 정보 --%>	
	var cp = model.currentPrice;
	var ma = model.minimumAmount;
	var immediatePurchase = model.immediatePurchase;
	var bdP = cp + ma;
	
	<%-- 입찰 버튼 --%>	
	var bidding = new KButton(t.layout.bottomCenterArea,<fmt:message key="btn.doc.047"/>);
	<%-- 입찰가격 기본 SETTING --%>
	var biddingPrice =  t.getChild('biddingPrice').setValue(bdP);
	
	<%-- 입찰 버튼 클릭 --%>	
	bidding.onclick = function() {
		var bdpInput =  t.getChild('biddingPrice').getValue();
		if(bdpInput > cp){
			if(bdpInput >= bdP){
				if(immediatePurchase>bdpInput || immediatePurchase == 0){
					t.submitInner('/jsl/MarketItemUser.DoBidding.jsl?id=@{id}', function(e) {
						alert('<kfmt:message key="market.pub.014"/>');
						opener.location.reload();
						window.close();
					});	
				}else{
					alert('<kfmt:message key="market.pub.017"/>');
				}
			}else{
				alert('<kfmt:message key="market.017"/>');
			}
		}else{
			alert('<kfmt:message key="market.016"/>');
		}	
	}	
	
	<%-- 닫기 버튼 --%>	
	var cancel = new KButton(t.layout.mainHeadRight,<kfmt:message key="market.btn.003"/>);
	cancel.onclick = function(){
		opener.location.reload();
		window.close();
	}
	
	<%-- UP 버튼 --%>
	var up = new KButton(t.layout.bottomCenterArea,<kfmt:message key="market.btn.001"/>);
	up.onclick = function(){
		var biddingPrice =  t.getChild('biddingPrice').getValue();
		var bdPUp = biddingPrice + ma;
		t.getChild('biddingPrice').setValue(bdPUp);
	}
	
	<%-- DOWN 버튼 --%>
	var down = new KButton(t.layout.bottomCenterArea,<kfmt:message key="market.btn.002"/>);
	down.onclick = function(){
		var biddingPrice =  t.getChild('biddingPrice').getValue();
		var bdPDown = biddingPrice - ma;
		t.getChild('biddingPrice').setValue(bdPDown);
		biddingPrice =  t.getChild('biddingPrice').getValue();
		if(biddingPrice<=0){
			alert('<kfmt:message key="market.016"/>');
			t.getChild('biddingPrice').setValue(ma);
		}
	}
});
</script>
<%@ include file="/sys/jsv/template/template.body.jsp" %>
<div id="main"></div>
<%@ include file="/sys/jsv/template/template.tail.jsp" %>
