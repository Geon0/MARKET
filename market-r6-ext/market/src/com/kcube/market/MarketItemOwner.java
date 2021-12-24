package com.kcube.market;

import com.kcube.doc.ItemPermission;
import com.kcube.lib.action.ActionContext;
import com.kcube.lib.sql.SqlSelect;
import com.kcube.sys.module.ModuleParam;
import com.kcube.sys.usr.UserPermission;
import com.kcube.sys.usr.UserService;

/**
 * 사내마켓 소유자 Action
 */
public class MarketItemOwner
{
	/**
	 * 나의 판매 게시글 목록을 출력한다.
	 */
	public static class ListByOwner extends MarketItemAction
	{
		public void execute(ActionContext ctx) throws Exception
		{
			ModuleParam mp = ctx.getModuleParam();
			UserPermission.setModuleMenu(mp);

			MarketItemSql sql = new MarketItemSql(
				mp, ctx.getLong("tr", null), ctx.getParameter("ts"), ctx.getBoolean("isCountDisplay"), true);

			Long id = UserService.getUserId();
			int status = ctx.getInt("status", -1);

			SqlSelect select = sql.getOwnerList(status, id);
			SqlSelect count = sql.getOwnerCount(status, id);

			sql.writeJson(ctx.getWriter(), select, count);
		}
	}

	/**
	 * 소유자 권한으로 게시글을 조회한다.
	 */
	public static class ReadByOwner extends MarketItemAction
	{
		public void execute(ActionContext ctx) throws Exception
		{
			ModuleParam mp = ctx.getModuleParam();
			UserPermission.setModuleMenu(mp);

			MarketItem server = (MarketItem) _storage.load(ctx.getLong("id"));

			if (server.getStatus() == MarketItem.DELETED_STATUS)
			{
				throw new MarketItemPermission.ItemDeletedStatus();
			}

			ItemPermission.checkOwner(server);
			MarketItemHistory.read(server);

			_factory.marshal(ctx.getWriter(), server);
		}
	}

	/**
	 * 나의 구매 목록을 돌려준다.
	 */
	public static class BuyList extends MarketItemAction
	{
		public void execute(ActionContext ctx) throws Exception
		{
			ModuleParam mp = ctx.getModuleParam();
			UserPermission.setModuleMenu(mp);

			int status = ctx.getInt("status", -1);
			long id = UserService.getUserId();
			MarketItemSql sql = new MarketItemSql(
				mp, ctx.getLong("tr", null), ctx.getParameter("ts"), ctx.getBoolean("isCountDisplay"), true);

			SqlSelect select = sql.getBuyList(status, id);
			SqlSelect count = sql.getBuyListCount(status, id);

			sql.writeJson(ctx.getWriter(), select, count);
		}
	}

	/**
	 * 소유자의 권한으로 게시글을 수정한다.
	 */
	public static class DoUpdateByOwner extends MarketItemAction
	{
		public void execute(ActionContext ctx) throws Exception
		{
			MarketItem client = unmarshal(ctx);
			MarketItem server = (MarketItem) _storage.loadOrCreateWithLock(client.getId());

			if (client.getId() != null)
			{
				ItemPermission.checkOwner(server);
				MarketItemPermission.update(server);
				client.setSpaceId(server.getSpaceId());
				client.setAppId(server.getAppId());
			}
			MarketItemManager.update(server, client);
		}
	}

	/**
	 * 판매글을 판매대기에서 판매중으로 바꾼다.
	 */
	public static class DoSelling extends MarketItemAction
	{
		public void execute(ActionContext ctx) throws Exception
		{
			ModuleParam mp = ctx.getModuleParam();
			UserPermission.setModuleMenu(mp);

			Long id = ctx.getLong("id");
			MarketItem server = (MarketItem) _storage.loadWithLock(id);

			ItemPermission.checkOwner(server);
			MarketItemPermission.update(server);

			MarketItemManager.selling(server);
		}
	}

	/**
	 * 등록된 글을 소유자 권한으로 삭제한다.
	 */
	public static class DoDeleteByOwner extends MarketItemAction
	{
		public void execute(ActionContext ctx) throws Exception
		{
			ModuleParam mp = ctx.getModuleParam();
			UserPermission.setModuleMenu(mp);

			Long id = ctx.getLong("id");
			MarketItem server = (MarketItem) _storage.loadWithLock(id);

			ItemPermission.checkOwner(server);
			MarketItemManager.delete(server);
		}
	}

	/**
	 * 등록된 글을 소유자 권한으로 경매를 종료한다.
	 */
	public static class DoEndByOwner extends MarketItemAction
	{
		public void execute(ActionContext ctx) throws Exception
		{
			ModuleParam mp = ctx.getModuleParam();
			UserPermission.setModuleMenu(mp);

			Long id = ctx.getLong("id");
			MarketItem server = (MarketItem) _storage.loadOrCreateWithLock(id);
			ItemPermission.checkOwner(server);

			MarketItemManager.setBiddingInfo(server, mp, id);
			MarketItemHistory.endBidding(mp, server);
		}

	}

	/**
	 * 거래대기 상태 글을 거래완료로 변경한다.
	 */
	public static class DoEndDeal extends MarketItemAction
	{
		public void execute(ActionContext ctx) throws Exception
		{
			ModuleParam mp = ctx.getModuleParam();
			UserPermission.setModuleMenu(mp);

			Long id = ctx.getLong("id");
			MarketItem server = (MarketItem) _storage.loadOrCreateWithLock(id);

			MarketItemManager.dealCompelete(server);
			MarketItemHistory.clickCompelete(mp, server);
		}
	}

	/**
	 * 거래완료 상태 글을 판매완료 상태로 변경한다.
	 */
	public static class DoEndSell extends MarketItemAction
	{
		public void execute(ActionContext ctx) throws Exception
		{
			ModuleParam mp = ctx.getModuleParam();
			UserPermission.setModuleMenu(mp);

			Long id = ctx.getLong("id");
			MarketItem server = (MarketItem) _storage.loadOrCreateWithLock(id);
			MarketItemManager.sellCompelete(server);
		}
	}
}
