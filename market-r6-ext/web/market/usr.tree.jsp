<%@ include file="/jspf/head.jsp" %>
<%@ include file="/jspf/app.info.jsp" %>
<%@ include file="/market/config.jsp" %>
<script type="text/javascript">
JSV.Block(function(){
	var rootId;
	var axisId = JSV.getParameter('axisId');
	var folderId = JSV.getParameter('folderId');
	var isFirst = true;
	var userId = <%=com.kcube.sys.usr.UserService.getUserId()%>;
	var title = '${currAppName}';
	var isAdmin = ${currAppAdmin};
	var style = {'title':title, 'link':{url:'usr.index.jsp', target:'bottom'},'isAdmin':isAdmin, 'iconType':'${currAppIconType}', 'iconCode':'${currAppIconCode}', 'iconSaveCode':'${currAppIconSaceCode}', 'iconSavePath':'${currAppIconSavePath}'};
	leftMenu = new LeftMenu(document.getElementById('main'), style);
	<% if (useCtgr(moduleParam)) {%>
		leftMenu.addTopMenu('kind', <fmt:message key="leftmenu.icon.kind"/>,
			{'component':'TreeMenu','isPreLoad':true,
				'style':{'skin':'lightblue','isPaging':true, 'axisId':axisId, 'isReadMapSecure':<%=isReadMapSecure(moduleParam)%>}
			});
		
		treeMenu = leftMenu.getChild('kind');
		treeMenu.loadList = function(){
			callTree(folderId);
		}
		
		var axes = treeMenu.axes;
		tree = treeMenu.tree;
		
		<% if (isReadMapSecure(moduleParam)) { %>
		tree.setContentProvider(new ScrtLazyXMLTreeContentProvider(
		 '/jsl/FolderSelector.ChildrenList.xml',
		 '/jsl/FolderSelector.AncestorsOrSelf.xml',
		 '1000,2000'
		));
		<% } else { %>
		tree.setContentProvider(new LazyXMLTreeContentProvider(
		 '/jsl/FolderSelector.ChildrenList.xml',
		 '/jsl/FolderSelector.AncestorsOrSelf.xml'
		));
		<% } %>
		
		<%-- 축이 선택되었을 때 --%>
		axes.onchange = function(axis) {
			axisId = axis.id;
			rootId = axis.rootId;
			if (!isFirst) {
				folderId = rootId;
				callList(folderId);
				doClickMenu('kind');
			}
			tree.setInput('/jsl/FolderSelector.SelfOrChildren.xml?id=' + axis.rootId, folderId);
			isFirst = false;
		}
	
		<%-- 트리의 노드가 선택되었을 때 --%>
		tree.onclick = function(obj) {
			folderId = $(obj).attr('id');
			var nodeId = $(obj).attr('id');
			var path = tree.getPath(nodeId);
			callList(folderId);
			doClickMenu('kind');
		};
	
		<%--   축 목록을 받아온다. --%>
		treeMenu.loadAxis();
		leftMenu.resize();
	<% } else { %>
		leftMenu.addTopMenu('kind', <fmt:message key="leftmenu.icon.noCtgr"/>,
			{'component':'LeftMenuTopLink','isPreLoad':true,
				'style':{'url':'/marekt/usr.list.jsp'}
			});
	<% } %>
	
	leftMenu.addTopMenu('mydoc', <fmt:message key="leftmenu.icon.mydoc"/>,
			{'component':'SelectListMenu','isPreLoad':true,
				'style':{}
			}
		);
			
	leftMenu.setMenu('kind');
	infoMenu = leftMenu.getChild('mydoc');
	infoMenu.beforeClick = function() {
		doClickMenu('mydoc');
	}
	infoMenu.addSubMenu('<kfmt:message key="market.020"/>', 1, '/market/own.sell.list.jsp', 'right');
	infoMenu.addSubMenu('<kfmt:message key="market.021"/>', 2, '/market/own.buy.list.jsp', 'right');
	infoMenu.setValue(1);
	
});
	var tree;
	var leftMenu;
	var treeMenu;
	var infoMenu;
	<%-- 조회화면에서 호출함   --%>
	function callTree(folderId) {
		tree.expand(folderId);
		callList(folderId);
	}
	<%-- 오른쪽 목록을 호출함. --%>
	function callList(folderId){
		JSV.setState('tr', folderId);
		JSV.doGET('/market/usr.list.jsp', 'right');
	}
	function doClickMenu(value) {
		leftMenu.setUnselected(value);
	}
	function doClickMyMap(url) {
		doClickMenu('favorite');
		if (url.indexOf('|') > 0 && url.indexOf('com.kcube.doc.list') > 0) {
			var urlIndex = url.indexOf('com.kcube.doc.list');
			var paramStartIndex = url.indexOf('=', urlIndex);
			var paramEndIndex = url.indexOf('&', urlIndex);
			var param = url.substring(paramStartIndex + 1, paramEndIndex);
			url = url.replace(param, encodeURIComponent(param));
		}
		parent.right.location.href = JSV.getContextPath(url);
	}
</script>
<%@ include file="/jspf/body.jsp" %>
<div id="main"></div>
<%@ include file="/jspf/tail.jsp" %>