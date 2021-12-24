package com.kcube.market;

import java.sql.ResultSet;
import java.util.HashMap;

import com.kcube.lib.action.ActionContext;
import com.kcube.lib.sql.SqlDialect;
import com.kcube.lib.sql.SqlSelect;
import com.kcube.sys.conf.module.ModuleConfigService;
import com.kcube.sys.module.ModuleParam;
import com.kcube.sys.usr.UserPermission;
import com.kcube.sys.usr.UserService;

public class MarketJob
{
	/**
	 * 지정한 시작일에 도달하면 판매대기 게시물을 판매중 상태로 변경한다.
	 */
	public static class StartBidding extends MarketItemAction
	{
		public void execute(ActionContext ctx) throws Exception
		{
			UserPermission.isAdmin();
			SqlSelect master = new SqlSelect();
			master.select("*");
			master.from("market_item");
			master.where("status = ?", MarketItem.REGISTERED_STATUS);
			master.where("start_time <= " + SqlDialect.sysdate());
			ResultSet rs = master.query();
			Long id = null;
			while (rs.next())
			{
				id = rs.getLong("itemid");
				MarketItem server = (MarketItem) _storage.loadWithLock(id);
				MarketItemManager.selling(server);
			}
		}
	}

	/**
	 * 지정한 종료시간에 도달하면 판매중 게시물을 경매종료상태로 변경한다. 최신입찰상태의 정보를 SET해준다.
	 */
	public static class EndBidding extends MarketItemAction
	{
		public void execute(ActionContext ctx) throws Exception
		{
			UserPermission.isAdmin();
			SqlSelect master = new SqlSelect();
			master.select("*");
			master.from("market_item");
			master.where("status = ?", MarketItem.SELLING_STATUS);
			master.where("end_time <= " + SqlDialect.sysdate());
			ResultSet rs = master.query();
			Long id = null;
			while (rs.next())
			{
				id = rs.getLong("itemid");
				MarketItem server = (MarketItem) _storage.loadWithLock(id);
				ModuleParam mp = new ModuleParam(
					server.getClassId(), server.getModuleId(), server.getSpaceId(), null, server.getAppId());
				MarketItemManager.setBiddingInfo(server, mp, id);
				MarketItemManager.endBidding(server);
				MarketItemHistory.endBidding(mp, server);
			}
		}
	}

	/**
	 * 현재시간에서 config에서 설정한 날짜 값만큼 지난 게시물을 판매완료 상태로 만든다.
	 */
	public static class ClickSellCompelete extends MarketItemAction
	{
		public void execute(ActionContext ctx) throws Exception
		{
			UserPermission.isAdmin();
			HashMap<String, Long> key = getModuleIdAppByClass(MarketItem.class.getName());
			String value = null;
			if (key != null)
			{
				value = ModuleConfigService.getProperty(
					UserService.getTenantId(),
					key.get("module"),
					key.get("app"),
					"com.kcube.market.MarketItemConfig.sellCompeleteDay");
			}
			SqlSelect master = new SqlSelect();
			master.select("*");
			master.from("market_item");
			master.where("status = ?", MarketItem.BUYCOMPELETE_STATUS);
			master.where("purchase_complete <= sysdate-" + value);
			ResultSet rs = master.query();
			Long id = null;
			while (rs.next())
			{
				id = rs.getLong("itemid");
				MarketItem server = (MarketItem) _storage.loadWithLock(id);
				MarketItemManager.sellCompelete(server);
			}
		}
	}

	/**
	 * 모듈ID와 APPID를 가져온다.
	 */
	static HashMap<String, Long> getModuleIdAppByClass(String className) throws Exception
	{
		HashMap<String, Long> key = new HashMap<>();
		Long moduleId = null;
		Long appId = null;
		SqlSelect stmt = new SqlSelect();
		stmt.select("s.appid,s.moduleid");
		stmt.from("wb_app s, wb_class c");
		stmt.where("s.classid = c.classid");
		stmt.where("c.isvisb = 1");
		stmt.where("s.tenantid = ?", UserService.getTenantId().longValue());
		stmt.where("c.class_name like ?", className + "%");

		ResultSet rs = stmt.query();
		if (rs.next())
		{
			moduleId = rs.getLong("moduleid");
			appId = rs.getLong("appid");
			key.put("module", moduleId);
			key.put("app", appId);
		}
		return key;
	}
}
