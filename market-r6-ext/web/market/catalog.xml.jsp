<%@ include file="/jspf/head.xml.jsp" %>
<%@ include file="/market/config.jsp" %>
<registry>
	<properties>
		<id>
			<list component="<%= getIdComponent(moduleParam) %>" width="60px">
				<header label="<fmt:message key="doc.006"/>" sort="id"/>
				<style attribute="id" noticeImg="/img/ico/doc/notice.gif"/>
			</list>
			<search>
				<header label="<fmt:message key="doc.098"/>" search="id"/>
			</search>
			<hidden component="HiddenFieldEditor" parent="null" id="id"/>
		</id>
		<itemId>
	  		<hidden component="HiddenFieldEditor" parent="null"/>
		</itemId>
		<duplex>
			<read component="DuplexViewer" id="duplex">
				<style actionUrl="/jsl/MarketItemUser.DuplexByUser.json" title="title" param="id=id,ts=com.kcube.doc.list,tr=tr,mdId=mdId,appId=appId"/>
			</read>
		</duplex>
		<status>
			<list component="StatusBoxColumn" width="85px">
				<header label="<fmt:message key="doc.027"/>" sort="status"/>
				<style attribute="status" options="<kfmt:message key="market.status.002"/>" styleOptions="<kfmt:message key="market.status.001"/>"/>
			</list>
			<hidden component="HiddenFieldEditor" parent="null"/>
		</status>
		<type>
			<list component="ComboColumn" width="85px">
				<header label="<kfmt:message key="market.038"/>" sort="isDonate"/>
				<style attribute="isDonate" options="<kfmt:message key="market.type.001"/>"/>
			</list>
		</type>
		<readCnt>
			<list component="<%= getReadCntComponent(moduleParam) %>" width="45px">
				<header label="<fmt:message key="doc.068"/>" sort="readCnt"/>
				<style attribute="readCnt" count="MarketItemViewer.ListCount" detail="MarketItemViewer.DetailListCount"  itemid="id"/>
			</list>
			<read component="TextViewer">
				<header label="<fmt:message key="doc.068"/>"/>
			</read>
		</readCnt>
		<currentOwner>
			<hidden component="HiddenFieldEditor" parent="null"/>
		</currentOwner>
		<author>
			<read component="EmpHtmlViewer">
				<header label="<fmt:message key="doc.003"/>"/>
				<style vrtl="<fmt:message key="sys.vrtl.userId"/>"/>
			</read>
			<write component="<kfmt:message key="bbs.component.author"/>" id="auth" observe="folder">
				<header label="<kfmt:message key="market.024"/>" required="true"/>
				<style vrtl="<fmt:message key="sys.vrtl.userId"/>"/>
			</write>
			<list component="EmpColumn" sort="userName" width="127px">
				<header label="<kfmt:message key="market.024"/>" sort="userName" search="userName"/>
				<style id="userId" name="userName" vrtl="<fmt:message key="sys.vrtl.userId"/>"/>
			</list>
		</author>
		<seller>
			<list component="EmpColumn" sort="userName" width="127px">
				<header label="<kfmt:message key="market.024"/>" sort="userName" search="userName"/>
				<style id="userId" name="userName" vrtl="<fmt:message key="sys.vrtl.userId"/>"/>
			</list>
		</seller>
		<buyer>
			<list component="EmpColumn" sort="purchaseName" width="127px">
				<header label="<kfmt:message key="market.035"/>" sort="purchaseName" search="purchaseName"/>
				<style id="purchaseUserId" name="purchaseName" vrtl="<fmt:message key="sys.vrtl.purchaseUserId"/>"/>
			</list>
		</buyer>
		<isDonate>
		 	<write component="BooleanFieldEditor" id="isDonate">
				<header label="<kfmt:message key="market.008"/>"/>
			</write>
		</isDonate>
		<startPrice>
			<write component="MarketNumberFieldEditor">
				<header label="<kfmt:message key="market.009"/>"/>
				<style vrtl="<fmt:message key="sys.vrtl.userId"/>" maxLength="11" id="startPrice" attribute="startPrice" year="50,50"/>
			</write>
		</startPrice>
		<startTime>
			<read component="DateViewer">
				<header label="<kfmt:message key="market.002"/>"/>
				<style format="<fmt:message key="date.long"/>"/>
			</read>
			<list component="DateColumn" width="130px">
				<header label="<kfmt:message key="market.002"/>"/>
				<style format="<fmt:message key="date.long"/>" attribute="startTime"/>
			</list>
		</startTime>
		<endTime>
			<read component="DateViewer">
				<header label="<kfmt:message key="market.003"/>"/>
				<style format="<fmt:message key="date.long"/>"/>
			</read>
			<list component="DateColumn" width="130px">
				<header label="<kfmt:message key="market.003"/>"/>
				<style format="<fmt:message key="date.long"/>" attribute="endTime"/>
			</list>
		</endTime>
		<dateTerm>
			<write name="startTime,endTime" component="MarketDateTermEditor" id="dateTerm">
				<header label="<kfmt:message key="market.007"/>" required="true"/>
				<style format="yy.mm.dd" isPop="false" basicPeriod="<%=getBasicPeriod(moduleParam)%>"/>
			</write>
			<edit name="startTime,endTime" component="MarketDateTermEditor" id="dateTerm">
				<header label="<kfmt:message key="market.007"/>"/>
				<style format="yy.mm.dd" mode="edit" isPop="false"/>
			</edit>
			<read component="MarketDateTermViewer" name="startTime,endTime">
				<header label="<kfmt:message key="market.007"/>"/>
				<style format="<fmt:message key="date.long"/>"/>
			</read>
			<list component="MarketDateColumn" width="200px">
				<header label="<kfmt:message key="market.007"/>"/>
				<style format="<fmt:message key="date.long"/>" attribute="startTime"/>
			</list>
		</dateTerm>
		<rgstUser>
		   <list component="EmpColumn" width="70px">
            	<header label="<fmt:message key="usr.10"/>" sort = "rgstName"/>
          	  	<style id="rgstUserid" name="rgstName" vrtl="<fmt:message key="sys.vrtl.userId"/>" />
    	  </list>
		</rgstUser>
		<minimumCheck>
			 <write component="RadioGroupFieldEditor" id="minimumCheck">
				<header label="<kfmt:message key="market.011"/>"/>
	 			<style options="<kfmt:message key="market.012"/>"/>
			</write>
		</minimumCheck>
		<minimumAmount>
			<write component="MarketNumberFieldEditor" id="minimumAmount">
				<header label="<kfmt:message key="market.011"/>"/>
				<style vrtl="<fmt:message key="sys.vrtl.userId"/>" maxLength="11" attribute="minimumAmount" year="50,50"/>
			</write> 
			<read component="MarketNumberViewer">
				<header label="<kfmt:message key="market.011"/>"/>
				<style readOnly="true"></style>
			</read>
			<edit component="MarketNumberFieldEditor" id="minimumAmount">
				<header label="<kfmt:message key="market.011"/>"/>
				<style vrtl="<fmt:message key="sys.vrtl.userId"/>" maxLength="11" readOnly="true" attribute="minimumAmount" year="50,50"/>
			</edit> 
		</minimumAmount>
		<ratePrice>
			<write component="MarketNumberRatio" id="ratePrice">
				<header label="<kfmt:message key="market.013"/>"/>
				<style vrtl="<fmt:message key="sys.vrtl.userId"/>" maxLength="3" attribute="minimumAmount" year="50,50"/>
			</write> 
		</ratePrice>
		<currentPrice>
			<hidden component="HiddenFieldEditor" parent="null"/>
			<read component="MarketNumberViewer">
				<header label="<kfmt:message key="market.004"/>"/>
				<style readOnly="true"></style>
			</read>
			<list component="MarketNumberColumn" width="100px" id="currentPrice" name="currentPrice" >
				<header label="<kfmt:message key="market.004"/>" sort="currentPrice"/>
				<style attribute="currentPrice" align="center" isCommas="true"/>
			</list>
			<write component="MarketNumberFieldEditor" id="currentPrice">
				<header label="<kfmt:message key="market.004"/>"/>
				<style vrtl="<fmt:message key="sys.vrtl.userId"/>" maxLength="11" attribute="currentPrice" year="50,50"/>
			</write> 
			<edit component="MarketNumberFieldEditor" id="currentPrice">
				<header label="<kfmt:message key="market.004"/>"/>
				<style vrtl="<fmt:message key="sys.vrtl.userId"/>" maxLength="11" readOnly="true" attribute="currentPrice" year="50,50"/>
			</edit> 
		</currentPrice>
		<biddingPrice>
			<write component="MarketNumberFieldEditor" id="biddingPrice">
				<header label="<kfmt:message key="market.015"/>"/>
				<style vrtl="<fmt:message key="sys.vrtl.userId"/>" maxLength="11" attribute="currentPrice" year="50,50"/>
			</write>
			<list component="MarketNumberColumn" width="100px" id="biddingPrice" name="biddingPrice" >
				<header label="<kfmt:message key="market.029"/>" sort="biddingPrice"/>
				<style attribute="biddingPrice" align="center" isCommas="true"/>
			</list>
		</biddingPrice>
		<biddingTime>
			<list component="DateColumn" width="130px">
				<header label="<kfmt:message key="market.030"/>"/>
				<style format="<fmt:message key="date.long"/>" attribute="biddingTime"/>
			</list>
		</biddingTime>
		<biddingName>
			<list component="EmpColumn" width="127px">
				<header label="<kfmt:message key="market.031"/>"/>
				<style id="userId" name="userDisp" vrtl="<fmt:message key="sys.vrtl.userId"/>"/>
			</list>
		</biddingName>
		<immediatePurchase>
			<hidden component="HiddenFieldEditor" parent="null"/>
			<read component="MarketNumberViewer" id = "immediatePurchase">
				<header label="<kfmt:message key="market.005"/>"/>
				<style readOnly="true"></style>
			</read>
			<list component="MarketNumberColumn" width="100px" id="immediatePurchase" name="immediatePurchase" >
				<header label="<kfmt:message key="market.005"/>" sort="immediatePurchase"/>
				<style attribute="immediatePurchase" align="center" isCommas="true"/>
			</list>
			<write component="MarketNumberFieldEditor" name="immediatePurchase" id="immediatePurchase">
				<header label="<kfmt:message key="market.005"/>"/>
				<style vrtl="<fmt:message key="sys.vrtl.userId"/>" maxLength="11" id="immediatePurchase" name="immediatePurchase" year="50,50"/>
			</write>
			<edit component="MarketNumberFieldEditor" id="immediatePurchase">
				<header label="<kfmt:message key="market.005"/>"/>
				<style vrtl="<fmt:message key="sys.vrtl.userId"/>" maxLength="11" readOnly="true" attribute="immediatePurchase" year="50,50"/>
			</edit> 
		</immediatePurchase>
		<purchasePrice>
			<hidden component="HiddenFieldEditor" parent="null"/>
			<list component="MarketNumberColumn" width="100px" id="purchasePrice" name="purchasePrice" >
				<header label="<kfmt:message key="market.022"/>" sort="purchasePrice"/>
				<style attribute="purchasePrice" align="center" isCommas="true"/>
			</list>
		</purchasePrice>
		<purchaseTime>
			<hidden component="HiddenFieldEditor" parent="null"/>
			<list component="DateColumn" width="130px">
				<header label="<kfmt:message key="market.023"/>"/>
				<style format="<fmt:message key="date.long"/>" attribute="purchaseTime"/>
			</list>
		</purchaseTime>
		<attachments>
			<list component="FileColumn" width="50px" id="attachments">
				<header label="<fmt:message key="doc.038"/>"/>
				<style select="/jsl/MarketItemUser.AttachmentList.json?id=@{id}"
						inline="/jsl/inline/MarketItemUser.DownloadByUser?id=@{id}"
						attach="/jsl/attach/MarketItemUser.DownloadByUser?id=@{id}"/>
			</list>
			<write component="FileFieldEditor" id="attachments">
		<style fileSize="<%=getFileSize(moduleParam)%>" totalSize="<%=getTotalSize(moduleParam)%>" notSupport="<%=getNotSupportedExt()%>" sessionKey="${sessionKey}"/>
			</write>
			<read component="FileViewer">
				<header label="<fmt:message key="doc.017"/>"/>
				<style inline="/jsl/inline/MarketItemUser.DownloadByUser?id=@{id}"
						attach="/jsl/attach/MarketItemUser.DownloadByUser?id=@{id}"
						attachZip="/jsl/attachZip/MarketItemUser.DownloadZipByUser?id="
						preview="/ext/docviewer/preview.index.jsp?module=MARKET&amp;id=@{id}"
						optional="true"/>
			</read>
			<admin component="FileViewer">
				<header label="<fmt:message key="doc.017"/>"/>
				<style inline="/jsl/inline/MarketItemUser.DownloadByAdmin?id=@{id}"
						attach="/jsl/attach/MarketItemUser.DownloadByAdmin?id=@{id}"
						preview="/ext/docviewer/preview.index.jsp?module=MARKET&amp;id=@{id}"
						optional="true"/>
			</admin>
		</attachments>
		<title>
			<write component="TextFieldEditor" focus="true">
				<header label="<fmt:message key="doc.001"/>" required="true"/>
				<style maxLength="85"/>
			</write>
			<edit component="TextFieldEditor" focus="true">
				<header label="<fmt:message key="doc.001"/>" required="true"/>
				<style maxLength="85" readOnly="true"/>
			</edit>
			<admList component="TitleColumn">
				<header label="<fmt:message key="doc.001"/>" sort="title" search="title"/>
				<style href="javascript:popup('@{id}');" attribute="title" position="pos" rplyIcon="true" newIcon="rgstDate"/>
			</admList>
			<list component="TitleColumn">
				<header label="<fmt:message key="doc.001"/>" sort="title" search="title"/>
				<style href="usr.read.jsp?id=@{id}" attribute="title" bestIcon="best" position="pos" rplyIcon="true" newIcon="rgstDate"
				isSympathy="<%=isSympathy(moduleParam)%>" opnCnt="opnCnt" opnView="MarketItemOpinion.ViewOpinion" />
			</list>
			<read component="MainTitleViewer" id="readTitle" name="title" viewer="true">
				<style detailId="detailInfo">
					<title component="TitleViewer" reference="title">
						<style className="mainTitle_title"/>
					</title>
					<bottom>
						<author component="EmpHtmlViewer" reference="author" styleType="authorInfo">
							<style useMainTitle="true" vrtl="<fmt:message key="sys.vrtl.userId"/>"/>
						</author>
						<sep1/>
							<dateTerm component="MarketDateTermViewer" name="startTime,endTime" reference="startTime,endTime" styleType="dateInfo">
								<style format="<fmt:message key="date.long"/>"/>
							</dateTerm>
						<sep2/>	
							<readCnt component="ReadCountViewer" reference="id,readCnt" styleType="viewInfo">
								<style isReadCntPopup="<%= isReadCntPopup(moduleParam) %>" className="mainTitle_readCnt" count="MarketItemViewer.ListCount" detail="MarketItemViewer.DetailListCount" />
							</readCnt>
					</bottom>
				</style>
			</read>
			<readadmin component="MainTitleViewer" id="readTitle" name="title" viewer="true">
				<style detailId="detailInfo">
					<title component="TitleViewer" reference="title">
						<style className="mainTitle_title"/>
					</title>
					<bottom>
						<author component="EmpHtmlViewer" reference="author" styleType="authorInfo">
							<style useMainTitle="true" vrtl="<fmt:message key="sys.vrtl.userId"/>"/>
						</author>
						<sep1/>
							<dateTerm component="MarketDateTermViewer" name="startTime,endTime" reference="startTime,endTime" styleType="dateInfo">
								<style format="<fmt:message key="date.long"/>"/>
							</dateTerm>
					</bottom>
				</style>
			</readadmin>
		</title>
		<content>
			<write component="WebEditor" hidden="content" id="content">
				<style height="350" name="content" editor="kcube" attachUrl="ImageAction.Download" sessionKey="${sessionKey}"/>
			</write>
			<read component="ContentViewer" id="content" name="content">
				<style layoutClass="bottomBorder"></style>
			</read>
		</content>
		<folder>
			<write component="FolderCompositeEditor" required="true" id="folder">
				<header label="<fmt:message key="doc.210"/>" required="true"/>
				<style scrtCode="2000" viewer="FolderTextViewer" editor="FolderFieldEditor" idx="0"/>
			</write>
		</folder>
		<author>
			<write component="EmpTextFieldEditor" id="auth" observe="folder">
				<header label="<fmt:message key="doc.003"/>" required="true"/>
				<style vrtl="<fmt:message key="sys.vrtl.userId"/>"/>
			</write>
			<hidden name="author.id,author.name" component="HiddenFieldEditor" parent="null"/>
			<list component="EmpColumn" sort="userName" width="127px">
				<header label="<kfmt:message key="market.024"/>" sort="userName" search="userName"/>
				<style id="userId" name="userName" vrtl="<fmt:message key="sys.vrtl.userId"/>"/>
			</list>
		</author>
		<rgstDate>
			<list component="DateColumn" sort="rgstDate" width="127px">
				<header label="<fmt:message key="doc.022"/>" sort="rgstDate" search="rgstDate"/>
				<style format="<fmt:message key="date.medium"/>" attribute="rgstDate"/>
			</list>
		</rgstDate>
		<tags>
			<write component="TagFieldEditor">
				<style autoComplete="true" <%=isRequiredTag()%>/>
			</write>
			<read component="TagViewer" id="tags">
				<header label="<fmt:message key="doc.146"/>"/>
				<style optional="true"/>
			</read>
			<search>
				<header label="<fmt:message key="doc.146"/>" search="tag"/>
			</search>
		</tags>
		<content>
			<write component="WebEditor" hidden="content" id="content">
				<style height="350" name="content" editor="kcube" attachUrl="ImageAction.Download" sessionKey="${sessionKey}"/>
			</write>
			<read component="ContentViewer">
				<style layoutClass="bottomBorder">
				<tag component="TagViewer" reference="tags"/>
					<infos class="Array">
						<info action="refContent" text="JSV.getLang('ContentViewer','reference')"/>
					</infos>
				</style>
			</read>
		</content>		
		<foldingBar>
			<detailInfo component="FoldingBar" foldable="true" closeMsg="<fmt:message key="doc.171"/>" openMsg="<fmt:message key="doc.172"/>" id="detailInfo">
				<style type="hidden"/>
			</detailInfo>
			<writeDetail component="FoldingBar" foldable="true" foldClass="boldAnchor" closeMsg="<fmt:message key="doc.244"/>" openMsg="<fmt:message key="doc.244"/>" id="writeDetail">
				<style color="white"/>
			</writeDetail>
		</foldingBar>
		<buyButton>
			<list component="ButtonColumn" width="70px">
				<header label="<kfmt:message key="market.025"/>"/>
				<style attribute="status">
					<options class="Array">
						<option value="4000" img="<kfmt:message key="btn.pub.buy_small"/>" execute="doDealing('/jsl/MarketItemOwner.doDeal.jsl?id=@{id}',element)"/>
					</options>
				</style>
			</list>
		</buyButton>
		<sellButton>
			<list component="ButtonColumn" width="70px">
				<header label="<kfmt:message key="market.025"/>"/>
				<style attribute="status">
					<options class="Array">
						<option value="5000" img="<kfmt:message key="btn.pub.sell_small"/>" execute="endSell('/jsl/MarketItemOwner.endSell.jsl?id=@{id}',element)"/>
					</options>
				</style>
			</list>
		</sellButton>
		<delButton>
			<list component="ButtonColumn" width="70px" id="delbtn">
				<header label="<kfmt:message key="market.032"/>"/>
				<style attribute="isBiddingCancel">
					<options class="Array">
						<option value="0" img="<kfmt:message key="btn.pub.del_small"/>" execute="delBidding('/jsl/MarketItemAdmin.DoDelBid.jsl?biddingId=@{biddingId}&amp;id=@{id}',element)"/>
					</options>
				</style>
			</list>
		</delButton>
		<securities>
			<read component="SecurityGradeViewer" name="id,scrtLevel" id="scrtRead">
				<header label="<fmt:message key="doc.021"/>"/>
				<style options="<fmt:message key="doc.064"/>" actionUrl="MarketItemUser.ViewSecurities" permUrl="MarketItemUser.AttachemntPermission"/>
			</read>
			<write component="SecurityRadioFieldEditor" name="scrtLevel,securities" id="scrt">
				<header label="<fmt:message key="doc.021"/>"/>
				<style options="<fmt:message key="doc.064"/>" dialogOption="opnerComponent=SecurityRadioFieldEditor" defaultValue = "<%=getScrtLevel(moduleParam)%>">
					<kfmt:message key="market.008"/>
				</style>
			</write>
			<hidden component="HiddenFieldsEditor"><style name="security"/></hidden>
		</securities>
		<opinions>
			<write component="OpnWriter" id="opnWriter" observe="folder">
				<style itemId="id" reloadUrl="/market/usr.read.jsp"  actionUrl="/jsl/MarketItemOpinion.AddOpinion.json" vrtl="<fmt:message key="sys.vrtl.userId"/>"/>
			</write>
			<read component="OpnViewer" observe="folder,opnWriter">
				<style userId="<%=com.kcube.sys.usr.UserService.getUserId()%>"
				isCenter="<%=isAdmin(moduleParam)%>"
				reloadUrl="/market/usr.read.jsp" actionUrl="/jsl/MarketItemOpinion.DeleteOpinion.jsl"  vrtl="<fmt:message key="sys.vrtl.userId"/>"
				actionAddUrl="/jsl/MarketItemOpinion.AddOpinion.json"
				opnView="MarketItemOpinion.ViewOpinion" opnDelete="MarketItemOpinion.DeleteOpinion" opnUpdate="MarketItemOpinion.UpdateOpinion">
					<%if(isSympathy(moduleParam)){%>
					<sympathy component="SympathyViewer" reference="sympathies">
						<style userId="<%=com.kcube.sys.usr.UserService.getUserId()%>"
						options="<%=getSympathyIcons(moduleParam)%>"
						actionAddUrl="/jsl/MarketItemUser.SympathyByUser.json"
						actionDelUrl="/jsl/MarketItemUser.DelSympathyByUser.jsl"
						actionViewUrl="/jsl/MarketItemUser.ViewSympathyByUser.json"/>
					</sympathy>
					<%}%>
				</style>
			</read>
		 </opinions>
	</properties>
    <validators>
    	<title validator="required" message="[<fmt:message key="doc.001"/>] <fmt:message key="doc.016"/>"/>
    	<folder validator="required" message="[<fmt:message key="doc.210"/>] <fmt:message key="doc.016"/>"/>
    </validators>
</registry>
<%@ include file="/jspf/tail.xml.jsp" %>