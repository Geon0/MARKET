<%@page import="org.apache.poi.util.SystemOutLogger"%>
<%@page import="com.kcube.sys.usr.UserService,com.kcube.sys.conf.module.ModuleConfigService,com.kcube.sys.module.ModuleParam"%>
<%!private static com.kcube.doc.file.AttachmentConfig _attachmentConfig = (com.kcube.doc.file.AttachmentConfig) com.kcube.sys.conf.ConfigService.getConfig(
		com.kcube.doc.file.AttachmentConfig.class);

	private static com.kcube.doc.ItemConfig _itemConfig = (com.kcube.doc.ItemConfig) com.kcube.sys.conf.ConfigService.getConfig(
		com.kcube.doc.ItemConfig.class);

	private static final String LIST_ID_ROWNUM = "com.kcube.market.MarketItemConfig.listIdRownum";
	private static final String READ_CNT_POPUP = "com.kcube.market.MarketItemConfig.readCntPopup";
	private static final String READ_MAP_SECURE = "com.kcube.market.MarketItemConfig.readMapSecure";
	private static final String DUPLEX = "com.kcube.market.MarketItemConfig.duplex";
	private static final String LEVEL_ANNOUNCE = "com.kcube.market.MarketItemConfig.levelAnnounce";
	private static final String MARKET_FILE_SIZE = "com.kcube.market.MarketItemConfig.marketFileSize";
	private static final String MARKET_TOTAL_SIZE = "com.kcube.market.MarketItemConfig.marketTotalSize";
	private static final String SYMPATHY = "com.kcube.market.MarketItemConfig.sympathy";
	private static final String SYMPATHY_ICONS = "com.kcube.market.MarketItemConfig.sympathyIcons";
	private static final String DEFAULT_FOLDERID = "com.kcube.market.MarketItemConfig.defaultFolderId";
	private static final String DEFAULT_AXISID = "com.kcube.market.MarketItemConfig.defaultAxisId";
	private static final String BIDDINGCNT = "com.kcube.market.MarketItemConfig.biddingCnt";
	private static final String BASICPERIOD = "com.kcube.market.MarketItemConfig.basicPeriod";
	private static final String SCRT_LEVEL = "com.kcube.market.MarketItemConfig.scrtLevel";

	private static Long getDefaultFolderId(ModuleParam mParam)
	{
		if (mParam == null)
			return null;
		String defaultFolderId = ModuleConfigService.getProperty(mParam, DEFAULT_FOLDERID);
		return (defaultFolderId == null || "".equals(defaultFolderId)) ? null : Long.parseLong(defaultFolderId);
	}

	private static Long getDefaultAxisId(ModuleParam mParam)
	{
		if (mParam == null)
			return null;
		String defaultAxisId = ModuleConfigService.getProperty(mParam, DEFAULT_AXISID);
		return (defaultAxisId == null || "".equals(defaultAxisId)) ? null : Long.parseLong(defaultAxisId);
	}

	private boolean isSympathy(ModuleParam mParam)
	{
		return mParam == null ? false : ModuleConfigService.getBooleanProperty(mParam, SYMPATHY);
	}

	private String getSympathyIcons(ModuleParam mParam)
	{
		return mParam == null ? "" : ModuleConfigService.getProperty(mParam, SYMPATHY_ICONS);
	}

	private boolean useCtgr(ModuleParam mParam)
	{
		return mParam == null
			? false
			: ModuleConfigService.useCtgr(UserService.getTenantId(), mParam.getModuleId(), mParam.getAppId());
	}

	private int getFileSize(ModuleParam mParam)
	{
		return mParam == null ? -1 : Integer.parseInt(ModuleConfigService.getProperty(mParam, MARKET_FILE_SIZE));
	}

	private int getBasicPeriod(ModuleParam mParam)
	{
		return mParam == null ? 7 : Integer.parseInt(ModuleConfigService.getProperty(mParam, BASICPERIOD));
	}

	private String getNotSupportedExt()
	{

		return _attachmentConfig.getNotSupportedExt();
	}

	private int getBiddingCnt(ModuleParam mParam)
	{
		return mParam == null ? -1 : Integer.parseInt(ModuleConfigService.getProperty(mParam, BIDDINGCNT));
	}

	private int getTotalSize(ModuleParam mParam)
	{
		return mParam == null ? -1 : Integer.parseInt(ModuleConfigService.getProperty(mParam, MARKET_TOTAL_SIZE));
	}

	private boolean isReadMapSecure(ModuleParam mParam)
	{
		return mParam == null
			? false
			: ModuleConfigService.getBooleanProperty(mParam, READ_MAP_SECURE)
				&& ModuleConfigService.useCtgrReadScrt(
					UserService.getTenantId(),
					mParam.getModuleId(),
					mParam.getAppId());
	}

	private String getIdComponent(ModuleParam mParam)
	{
		return mParam == null
			? "IdColumn"
			: ModuleConfigService.getBooleanProperty(mParam, LIST_ID_ROWNUM) ? "RownumColumn" : "IdColumn";
	}

	private String getReadCntComponent(ModuleParam mParam)
	{
		return mParam == null
			? "TextColumn"
			: ModuleConfigService.getBooleanProperty(mParam, READ_CNT_POPUP) ? "ReadCountColumn" : "TextColumn";
	}

	private static boolean isReadCntPopup(ModuleParam mParam)
	{
		return mParam == null ? false : ModuleConfigService.getBooleanProperty(mParam, READ_CNT_POPUP);
	}

	private boolean isDuplex(ModuleParam mParam)
	{
		return mParam == null ? false : ModuleConfigService.getBooleanProperty(mParam, DUPLEX);
	}

	private String isRequiredTag()
	{
		return (_itemConfig.isRequiredTag()) ? "required='true'" : "";
	}

	private void checkAdmin(ModuleParam mParam) throws Exception
	{
		com.kcube.sys.usr.UserPermission.checkAppAdmin(mParam);
	}

	private static int getScrtLevel(ModuleParam mParam)
	{
		return mParam == null ? 0 : Integer.parseInt(ModuleConfigService.getProperty(mParam, SCRT_LEVEL));
	}
	
	private boolean isAdmin(ModuleParam mParam)
	{
		return com.kcube.sys.usr.UserPermission.isAppAdmin(mParam);
	}%>