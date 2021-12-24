package com.kcube.market;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.kcube.lib.jdbc.DbService;
import com.kcube.lib.sql.SqlSelect;
import com.kcube.marketBidding.MarketBiddingItem;
import com.kcube.sys.conf.module.ModuleConfigService;
import com.kcube.sys.module.ModuleParam;
import com.kcube.sys.usr.User;
import com.kcube.sys.usr.UserService;

public class MarketBiddingItemManager
{
	/**
	 * client의 값으로 server의 값을 update한다.
	 */
	static void DoBidding(MarketBiddingItem server, MarketBiddingItem client) throws Exception
	{
		User author = client.getAuthor();
		if (author != null)
		{
			server.setAuthor(author);
		}
		else if (server.getAuthor() == null)
		{
			server.setAuthor(UserService.getUser());
		}
		server.setModuleId(client.getModuleId());
		server.setAppId(client.getAppId());
		server.setClassId(client.getClassId());
		server.setSpaceId(client.getSpaceId());
		server.updateVisible(server.isVisible());
		server.setStatus(MarketItem.BUYCOMPELETE_STATUS);
	}

	/**
	 * 입찰정보를 삭제한다.
	 */
	static void remove(MarketBiddingItem server) throws Exception
	{
		DbService.remove(server);
	}

	/**
	 * 입찰정보를 등록한다.
	 */
	static void bidding(MarketBiddingItem client, MarketItem server, MarketBiddingItem bidding) throws Exception
	{
		bidding.setItemId(client.getId());
		bidding.setBiddingPrice(client.getBiddingPrice());
		bidding.setBiddingTime(new Date());
		server.setCurrentPrice(client.getBiddingPrice());
		bidding.setAuthor(UserService.getUser());
	}

	/**
	 * 다입찰 게시물 확인 config에서 value를 받아와 값을 비교 한 후 해당 값 만족시 해당 컬럼 값 True
	 */
	static void isMultiBid(ModuleParam mp, MarketItem server, Long id) throws Exception
	{

		MarketItemSql sql = new MarketItemSql(mp);
		int biddingCount = sql.getBidCount(id);
		List<Long> moduleIds = getModuleIdAppidByClass(MarketItem.class.getName());
		String value = null;
		if (moduleIds.size() > 0)
		{
			value = ModuleConfigService.getProperty(
				UserService.getTenantId(),
				moduleIds.get(1),
				moduleIds.get(0),
				"com.kcube.market.MarketItemConfig.biddingCnt");
		}
		int config = Integer.parseInt(value);
		if (biddingCount >= config)
		{
			server.setIsPriceBest(true);
		}
	}

	/**
	 * 모듈ID와 APPID를 가져온다.
	 */
	private static List<Long> getModuleIdAppidByClass(String className) throws Exception
	{
		List<Long> moduleIds = new ArrayList<Long>();
		SqlSelect stmt = new SqlSelect();
		stmt.select("s.appid,s.moduleid");
		stmt.from("wb_app s, wb_class c");
		stmt.where("s.classid = c.classid");
		stmt.where("c.isvisb = 1");
		stmt.where("s.tenantid = ?", UserService.getTenantId().longValue());
		stmt.where("c.class_name like ?", className + "%");

		ResultSet rs = stmt.query();
		while (rs.next())
		{
			moduleIds.add(rs.getLong(1));
			moduleIds.add(rs.getLong(2));

		}
		return moduleIds;
	}

}
