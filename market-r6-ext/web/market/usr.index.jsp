<%@ include file="/jspf/head.frame.jsp" %>
<%@ include file="/market/config.jsp" %>
<script type="text/javascript">
JSV.Block(function () {	
	var folderId = (JSV.getParameter('folderId')) ? JSV.getParameter('folderId') : '<%=getDefaultFolderId(moduleParam)%>';
	var axisId = (JSV.getParameter('axisId')) ? JSV.getParameter('axisId') : '<%=getDefaultAxisId(moduleParam)%>';
	doLeft(JSV.getModuleUrl('usr.tree.jsp?folderId=' + folderId + '&axisId=' + axisId));
	
	var right = JSV.getParameter('url');
	if (right) {
		doRight(JSV.getModuleUrl(right));
	} else {
		doRight(JSV.getModuleUrl('/market/usr.list.jsp?tr=' + folderId + '&com.kcube.jsv.state=tr'));
	}
});
</script>
<%@ include file="/jspf/tail.frame.jsp" %>