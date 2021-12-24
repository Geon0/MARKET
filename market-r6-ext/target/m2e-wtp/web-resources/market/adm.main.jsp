<%@ include file="/jspf/head.jsp" %>
<%@ include file="/market/config.jsp" %>
<%
	checkAdmin(moduleParam);
%>
<link type="text/css" href="<c:url value="/space/menu/AppAdmin.css"/>" media="screen,print" rel="stylesheet">
<script type="text/javascript">
JSV.Block(function(){
	header = new HeaderBar(document.getElementById('headerDiv'), {isUnderLine:true});
	header.setLabel('');
	adminHeaderBtn = header.clientArea;
	
	var menu = JSV.getParameter('menu') || 1;
	<% if (useCtgr(moduleParam)) { %>
		$('.hasTreeLnbDiv').removeClass('noMap');
		
		var rootId;
		var axisId = JSV.getParameter('axisId');
		var folderId = JSV.getParameter('folderId');
		var defaultAxis;
		
		var axes = new FlexTabList(document.getElementById('axisTd'), {'skin':'lightblue', 'isPaging':true});
		var tree = new TreeViewer(document.getElementById('treeTd'));
		tree.setContentProvider(new LazyXMLTreeContentProvider(
				 '/jsl/FolderSelector.ChildrenList.xml',
				 '/jsl/FolderSelector.AncestorsOrSelf.xml'
				));
		<%-- 오른쪽 목록을 호출함. --%>
		function callList(folderId){
			JSV.setState('menu', menu);
			JSV.setState('tr', folderId);
			JSV.setState('rootId', rootId);
			JSV.doGET('/market/adm.list.jsp', 'contentFrame');
		}
		
		<%-- 축이 선택되었을 때 --%>
		axes.onchange = function(axis) {
			axisId = axis.id;
			rootId = axis.rootId;
			tree.setInput('/jsl/FolderSelector.SelfOrChildren.xml?id=' + axis.rootId);
			callList(rootId);
		}
		<%-- 트리의 노드가 선택되었을 때 --%>
		tree.onclick = function(obj) {
			folderId = obj.getAttribute('id');
			var nodeId = obj.getAttribute('id');
			var path = tree.getPath(nodeId);
			callList(folderId);
		};
		
		<%-- 트리가 로드되었을 때 필터에 설정된 노드값을 미리 펼친다. --%>
		tree.onload = function() {
			if (folderId == null) return true;
			tree.expand(folderId);
			callList(folderId);
		};
		
		<%--   축 목록을 받아온다. --%>
		var m = JSV.loadJSON(JSV.getModuleUrl('/jsl/AxisSelector.ListByModuleNoScrt.json'));
		var model = m.array;
		if (model.length > 0) {
			for (var j = 0; j < model.length; j++) {
				var axis = {'id':model[j].id, 'name':model[j].name, 'rootId':model[j].rootId};
				if (axisId == axis.id || j==0) {
					defaultAxis = axis;
				}
				axes.add(axis.name, axis);
			}
			axes.setValue(model[0]);
			axes.onchange(model[0]);
		}
		
		$('.treeLnb .scrollArea').slimscroll({
			height: '100%',
			opacity: '0.1',
			color: '#000',
			distance: '0px',
			borderRadius: '8px',
			size: '8px',
			alwaysVisible : true
		});
	<% } else { %>
		JSV.doGET(JSV.getModuleUrl('adm.list.jsp?menu=' + menu), 'contentFrame');
	<% } %>
	
});
var header;
var tree;
var adminHeaderBtn;
function resizeTopIframe(height) {
	var docHeight = document.body.offsetHeight - $('#headerDiv').outerHeight(true) - 10;
	if (height > docHeight) {
		document.getElementById('contentFrame').height = parseInt(height) + 30;
	} else {
		document.getElementById('contentFrame').height = docHeight;
	}
}
</script>
<%@ include file="/jspf/body.jsp" %>
<div class="hasTreeLnbDiv noMap">
	<div id="headerDiv"></div>
	<div class="ApContainer">
		<div class="treeLnbWrap" id="treeLnbWrap">
			<div class="treeLnb">
				<div class="scrollArea">
					<table width="100%" cellpadding="0" cellspacing="0" style="padding:15px 0px 0 0px;">
						<tr id="axisTr">
							<td id="axisTd"></td>
						</tr>
						<tr>
							<td id="treeTd" style="padding:0"></td>
						</tr>
					</table>
				</div>
			</div>
		</div>
		<div class="ApContents">
			<iframe id="contentFrame" name="contentFrame" style="width:100%;" frameborder="0" scrolling="no"></iframe>
		</div>
	</div>
</div>
<%@ include file="/jspf/tail.jsp" %>