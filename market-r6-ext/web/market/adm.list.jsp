<%@ include file="/sys/jsv/template/template.head.jsp" %>
<%@ include file="/market/config.jsp" %>
<%@ include file="/market/jsv-market-ext.jsp" %>
<%@ page import="com.kcube.market.MarketItemSql" %>
<% checkAdmin(moduleParam); %>
<%
	MarketItemSql mk = new MarketItemSql(moduleParam, ctx.getLong("tr", null), ctx.getParameter("com.kcube.doc.list"), false);
%>
<style>
.cTable{display:none;margin-bottom:12px;}
</style>
<%-------------------------------------------------- 
			사내마켓 admin 목록 페이지
---------------------------------------------------%>
<script type="text/javascript">
JSV.Block(function () {
	var template = '<template name="com.kcube.doc.list" catalog="/market/catalog.xml.jsp" listType="LIST">\
		<header label="<kfmt:message key="market.042"/>"/>\
		<columns class="Array">\
			<column component="CheckboxColumn" width="25px">\
				<header label="<fmt:message key="doc.037"/>" search="id" toggle="id" />\
				<style name="id" attribute="id" />\
			</column>\
			<column property="id.list"/>\
			<column property="status.list"/>\
			<column property="title.admList"/>\
			<column property="author.list"/>\
			<column property="startTime.list"/>\
			<column property="endTime.list"/>\
			<column property="currentPrice.list"/>\
			<column property="immediatePurchase.list"/>\
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
	
	var model = <%
		setTableState(request, "ts", "com.kcube.doc.list", "com.kcube.doc.rowsPerPage");
		switch (ctx.getInt("menu"))
		{
			case 1: ctx.execute("MarketItemAdmin.ListByAdmin"); break;
			case 2: ctx.execute("MarketItemAdmin.DeletedListByAdmin"); break;
			case 3: ctx.execute("MarketItemAdmin.RecommendListByAdmin"); break;
		}
	%>;

	var hArr = {'ManageBbs':'<kfmt:message key="market.043"/>'};
	var cArr = {'ManageBbs':'<kfmt:message key="market.044"/>'};

	var t = new TableTemplate(document.getElementById('main'), template);
	t.setValue(model);
	
	var c = new DescriptionViewer(t.layout.mainMenuCenter);
	
	var cTable = $('<table align="center">\
			<tr>\
			<td id="etcTd1" width="80"></td>\
			<td id="etcTd2" width="50" align="right"></td>\
			<td id="etcTd3" width="80"></td>\
			<td id="etcTd4" width="80"></td>\
			<td id="etcTd5" width="50"></td>\
			<td id="etcTd6" width="90"></td>\
			<td id="etcTd7" width="100"></td>\
			<td id="etcTd8" width="60"></td>\
			<td id="etcTd9" width="80"></td>\
			<td id="etcTd10" width="40"></td>\
		</tr>\
	</table>').addClass('cTable').appendTo(t.layout.mainMenuCenter);
	
	JSV.setState('menu', JSV.getParameter('menu'));
	var btnArea = parent.header.clientArea;
	
	<%-- 게시글관리 --%>
	if (JSV.getParameter('menu') == 1) {
		hArr = {'ManageBbs':'<kfmt:message key="market.043"/>'};
		cArr = {'ManageBbs':'<kfmt:message key="market.044"/>'};
		$(btnArea).empty();

		<% if (!useCtgr(moduleParam)) { %>
			c.$widget.remove();
			$('#main').css('margin-top', '15px');
		<% } %>
		
		<%if (useCtgr(moduleParam) && (com.kcube.map.FolderCache.getChildrenCount(ctx.getLong("tr")) == 0)) { %>
		<%} %>
		
		<%-- 상태값 체크 버튼 --%>
		var status = JSV.getParameter('status') || -1;
		var options = '<kfmt:message key="market.checkType.002"/>';
		var selectStatus = new DropDownGroupFieldEditor(t.layout.mainHeadRight,{'options':options});
		selectStatus.onclick = function(value) {
			JSV.setState('status', value);
			JSV.setState('com.kcube.doc.list', null);
			JSV.doGET('adm.list.jsp');
		}
		selectStatus.setValue(status);
		
		<%-- 삭제 버튼 --%>
		var del = new KButton(btnArea, <fmt:message key="btn.pub.delete"/>);
		del.onclick = function() {
			if (BooleanFieldEditor.isChecked('id')) {
				if (!confirm('<fmt:message key="pub.009"/>')) {
					return false;
				}
				t.action.setRedirect(JSV.setUrlAlert('/market/adm.list.jsp'), '<fmt:message key="pub.004"/>');
				JSV.setState('com.kcube.doc.list', null);
				t.submit('/jsl/MarketItemAdmin.DoDeleteByAdmin.jsl');
			} else {
				alert('<fmt:message key="pub.007"/>');
			}	
		}
		
		<%-- 추천게시물 버튼 --%>
		var recommend = new KButton(btnArea, <kfmt:message key="btn.pub.recommend"/>);
		recommend.onclick = function() {
			if (BooleanFieldEditor.isChecked('id')) {
				if (!confirm('<kfmt:message key="market.pub.001"/>')) {
					return false;
				}
				t.action.setRedirect(JSV.setUrlAlert('/market/adm.list.jsp'), '<kfmt:message key="market.pub.002"/>');
				t.submit('/jsl/MarketItemAdmin.DoRecommendByAdmin.jsl');
			} else {
				alert('<fmt:message key="pub.007"/>');
			}
		}
		
	<%-- 삭제된 게시글 --%>
	} else if (JSV.getParameter('menu') == 2) {
		hArr = {'ManageBbs':'<kfmt:message key="market.045"/>'};
		cArr = {'ManageBbs':'<kfmt:message key="market.046"/>'};
		$(btnArea).empty();

		<%-- 복원 버튼 --%>
		var recover = new KButton(btnArea, <fmt:message key="btn.pub.recover"/>);
		recover.onclick = function() {
			if (BooleanFieldEditor.isChecked('id')) {
				if (!confirm('<fmt:message key="pub.024"/>')) {
					return false;
				}	
				t.action.setRedirect(JSV.setUrlAlert('/market/adm.list.jsp'), '<fmt:message key="pub.025"/>');
				t.submit('/jsl/MarketItemAdmin.DoRecoverByAdmin.jsl');
			} else {
				alert('<fmt:message key="pub.007"/>');
			}	
		}

		<%-- 폐기 버튼 --%>
		var remove = new KButton(btnArea, <fmt:message key="btn.pub.remove"/>);
		remove.onclick = function() {
			if (BooleanFieldEditor.isChecked('id')) {
				if (!confirm('<fmt:message key="pub.045"/>')) {
					return false;
				}
				t.action.setRedirect(JSV.setUrlAlert('/market/adm.list.jsp'), '<fmt:message key="pub.034"/>');
				t.submit('/jsl/MarketItemAdmin.DoRemoveByAdmin.jsl');
			} else {
				alert('<fmt:message key="pub.007"/>');
			}	
		}
		
	<%-- 추천 게시글 --%>
	} else if (JSV.getParameter('menu') == 3) {
		hArr = {'ManageBbs':'<kfmt:message key="market.026"/>'};
		cArr = {'ManageBbs':'<kfmt:message key="market.027"/>'};
		$(btnArea).empty();
		
		<%-- 추천게시물 해제 --%>
		var release = new KButton(btnArea, <kfmt:message key="btn.pub.norecommend"/>);
		release.onclick = function() {
			if (BooleanFieldEditor.isChecked('id')) {
				if (!confirm('<kfmt:message key="market.pub.015"/>')) {
					return false;
				}
				t.action.setRedirect(JSV.setUrlAlert('/market/adm.list.jsp'), '<kfmt:message key="market.pub.016"/>');
				t.submit('/jsl/MarketItemAdmin.NoRecommendByAdmin.jsl');
			} else {
				alert('<fmt:message key="pub.007"/>');
			}
		}
	} 
	
	parent.header.setLabel(hArr['ManageBbs']);
	c.setHtml(cArr['ManageBbs']);
	
	if (parent && parent.resizeTopIframe) {
		parent.resizeTopIframe(document.body.scrollHeight);
	}
});
	<%-- 팝업 게시물 --%>
	function popup(id) {
		var menu = JSV.getParameter('menu');
		var targetUrl = '/market/adm.popup.read.jsp?id=' + id + '&menu=' + menu;
		var url = JSV.getContextPath(JSV.getModuleUrl(targetUrl));
		window.open(url,"width=800,height=700,scrollbars=no,status=no,toolbar=no,menubar=no,location=no,resizable=yes");
	}
	
	function ListRefresh() {
		JSV.doGET(JSV.getModuleUrl('adm.list.jsp'));
	}
</script>
<%@ include file="/sys/jsv/template/template.body.jsp" %>
<div id="main"></div>
<%@ include file="/sys/jsv/template/template.tail.jsp" %>
