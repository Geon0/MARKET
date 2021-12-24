package com.kcube.market;

import java.sql.ResultSet;

import com.kcube.doc.ItemPermission;
import com.kcube.lib.action.ActionContext;
import com.kcube.lib.jdbc.DbService;
import com.kcube.lib.sql.SqlSelect;
import com.kcube.marketBidding.MarketBiddingItem;
import com.kcube.sys.module.ModuleParam;
import com.kcube.sys.usr.PermissionDeniedException;
import com.kcube.sys.usr.UserPermission;

/**
 * 사내마켓 관리자 Action
 */
public class MarketItemAdmin
{
	/**
	 * 관리자 권한으로 글을 조회한다.
	 */
	public static class ReadByAdmin extends MarketItemAction
	{
		public void execute(ActionContext ctx) throws Exception
		{
			ModuleParam mp = ctx.getModuleParam();
			MarketItem server = (MarketItem) _storage.load(ctx.getLong("id"));
			ItemPermission.checkAppAdmin(server, mp);

			_factory.marshal(ctx.getWriter(), server);
		}
	}

	/**
	 * 게시글 관리용 목록을 출력한다.
	 */
	public static class ListByAdmin extends MarketItemAction
	{
		public void execute(ActionContext ctx) throws Exception
		{
			ModuleParam mp = ctx.getModuleParam();
			UserPermission.setAdminModuleMenu(mp);

			int status = ctx.getInt("status", -1);
			MarketItemSql sql = new MarketItemSql(
				mp, ctx.getLong("tr", null), ctx.getParameter("ts"), ctx.getBoolean("isCountDisplay"), true);

			SqlSelect select = sql.getAdminVisibleSelect(true, status);
			SqlSelect count = sql.getAdminVisibleCount(true, status);

			sql.writeJson(ctx.getWriter(), select, count);
		}
	}

	/**
	 * 추천게시글의 List를 가져온다
	 */
	public static class RecommendListByAdmin extends MarketItemAction
	{
		public void execute(ActionContext ctx) throws Exception
		{
			ModuleParam mp = ctx.getModuleParam();
			UserPermission.setAdminModuleMenu(mp);

			int status = ctx.getInt("status", -1);
			MarketItemSql sql = new MarketItemSql(
				mp, ctx.getLong("tr", null), ctx.getParameter("ts"), ctx.getBoolean("isCountDisplay"), true);

			SqlSelect select = sql.getAdminRecommendList(true, status);
			SqlSelect count = sql.getAdminRecommendCount(true, status);

			sql.writeJson(ctx.getWriter(), select, count);
		}
	}

	/**
	 * 삭제된 목록을 조회한다.
	 */
	public static class DeletedListByAdmin extends MarketItemAction
	{
		public void execute(ActionContext ctx) throws Exception
		{
			ModuleParam mp = ctx.getModuleParam();
			UserPermission.setAdminModuleMenu(mp);

			MarketItemSql sql = new MarketItemSql(
				mp, ctx.getLong("tr", null), ctx.getParameter("ts"), ctx.getBoolean("isCountDisplay"));

			SqlSelect select = sql.getStatusSelect(MarketItem.DELETED_STATUS);
			SqlSelect count = sql.getStatusCount(MarketItem.DELETED_STATUS);

			sql.writeJson(ctx.getWriter(), select, count);
		}
	}

	/**
	 * 관리자 권한으로 여러 문서를 동시에 삭제한다.
	 */
	public static class DoDeleteByAdmin extends MarketItemAction
	{
		public void execute(ActionContext ctx) throws Exception
		{
			ModuleParam mp = ctx.getModuleParam();

			Long[] ids = ctx.getLongValues("id");
			int size = ids.length;

			for (int i = 0; i < size; i++)
			{
				DbService.begin();
				MarketItem server = (MarketItem) _storage.loadWithLock(ids[i]);
				if (i == 0)
				{
					ItemPermission.checkAppAdmin(server, mp);
				}
				else if (!(UserPermission.isAdmin() || UserPermission.isAppAdmin(mp))
					&& !ItemPermission.hasAppPermission(server, mp))
				{
					throw new PermissionDeniedException();
				}
				MarketItemManager.delete(server);
				DbService.commit();
			}
		}
	}

	/**
	 * 관리자 권한으로 여러 문서를 동시에 폐기한다.
	 */
	public static class DoRemoveByAdmin extends MarketItemAction
	{
		public void execute(ActionContext ctx) throws Exception
		{
			ModuleParam mp = ctx.getModuleParam();

			Long[] ids = ctx.getLongValues("id");
			int size = ids.length;
			for (int i = 0; i < size; i++)
			{
				DbService.begin();
				MarketItem server = (MarketItem) _storage.loadWithLock(ids[i]);
				if (i == 0)
				{
					ItemPermission.checkAppAdmin(server, mp);
				}
				else if (!(UserPermission.isAdmin() || UserPermission.isAppAdmin(mp))
					&& !ItemPermission.hasAppPermission(server, mp))
				{
					throw new PermissionDeniedException();
				}
				ItemPermission.remove(server);
				MarketItemManager.remove(server);
				DbService.commit();
			}
		}
	}

	/**
	 * 관리자 권한으로 삭제된 여러 문서를 동시에 복원한다.
	 */
	public static class DoRecoverByAdmin extends MarketItemAction
	{
		public void execute(ActionContext ctx) throws Exception
		{
			ModuleParam mp = ctx.getModuleParam();

			Long[] ids = ctx.getLongValues("id");
			int size = ids.length;
			for (int i = 0; i < size; i++)
			{
				DbService.begin();
				MarketItem server = (MarketItem) _storage.loadWithLock(ids[i]);
				if (i == 0)
				{
					ItemPermission.checkAppAdmin(server, mp);
				}
				else if (!(UserPermission.isAdmin() || UserPermission.isAppAdmin(mp))
					&& !ItemPermission.hasAppPermission(server, mp))
				{
					throw new PermissionDeniedException();
				}

				while (server.getStatus() == MarketItem.DELETED_STATUS)
				{
					MarketItemManager.recover(server);
					if (server.getPid() == null)
					{
						break;
					}
				}
				DbService.commit();
			}
		}
	}

	/**
	 * 관리자 권한으로 게시물을 추천한다.
	 */
	public static class DoRecommendByAdmin extends MarketItemAction
	{
		public void execute(ActionContext ctx) throws Exception
		{
			ModuleParam mp = ctx.getModuleParam();

			Long[] ids = ctx.getLongValues("id");
			int size = ids.length;

			for (int i = 0; i < size; i++)
			{
				DbService.begin();
				MarketItem server = (MarketItem) _storage.loadWithLock(ids[i]);
				if (i == 0)
				{
					ItemPermission.checkAppAdmin(server, mp);
				}
				else if (!(UserPermission.isAdmin() || UserPermission.isAppAdmin(mp))
					&& !ItemPermission.hasAppPermission(server, mp))
				{
					throw new PermissionDeniedException();
				}
				MarketItemManager.recommend(server);
				DbService.commit();
			}
		}
	}

	/**
	 * 관리자 권한으로 게시물을 추천해제한다.
	 */
	public static class NoRecommendByAdmin extends MarketItemAction
	{
		public void execute(ActionContext ctx) throws Exception
		{
			ModuleParam mp = ctx.getModuleParam();

			Long[] ids = ctx.getLongValues("id");
			int size = ids.length;

			for (int i = 0; i < size; i++)
			{
				DbService.begin();
				MarketItem server = (MarketItem) _storage.loadWithLock(ids[i]);
				if (i == 0)
				{
					ItemPermission.checkAppAdmin(server, mp);
				}
				else if (!(UserPermission.isAdmin() || UserPermission.isAppAdmin(mp))
					&& !ItemPermission.hasAppPermission(server, mp))
				{
					throw new PermissionDeniedException();
				}
				MarketItemManager.Norecommend(server);
				DbService.commit();
			}
		}
	}

	/**
	 * 입찰자 정보를 가져온다
	 */
	public static class getBidInfo extends MarketItemAction
	{
		public void execute(ActionContext ctx) throws Exception
		{

			ModuleParam mp = ctx.getModuleParam();

			MarketItemSql sql = new MarketItemSql(
				mp, ctx.getLong("tr", null), ctx.getParameter("ts"), ctx.getBoolean("isCountDisplay"), true);

			MarketItem server = (MarketItem) _storage.load(ctx.getLong("id"));
			ItemPermission.checkAppAdmin(server, mp);

			SqlSelect select = sql.getBid(ctx.getLong("id"));
			SqlSelect count = sql.bidCount(ctx.getLong("id"));

			sql.writeJsonBidding(ctx.getWriter(), select, count);
		}
	}

	/**
	 * 관리자 권한으로 업데이트한다.
	 */
	public static class DoUpdateByAdmin extends MarketItemAction
	{
		public void execute(ActionContext ctx) throws Exception
		{
			MarketItem client = unmarshal(ctx);
			ModuleParam mp = ctx.getModuleParam();
			MarketItem server = (MarketItem) _storage.loadWithLock(client.getId());
			ItemPermission.checkAppAdmin(server, mp);

			MarketItemManager.update(server, client);
			ctx.setParameter("id", server.getId().toString());
		}
	}

	/**
	 * 입찰을 취소한다.
	 */
	public static class DoDelBid extends MarketItemAction
	{
		public void execute(ActionContext ctx) throws Exception
		{
			ModuleParam mp = ctx.getModuleParam();
			MarketItemSql sql = new MarketItemSql(mp);

			MarketBiddingItem bidding = (MarketBiddingItem) _storageBidding.loadWithLock(ctx.getLong("biddingId"));
			MarketItem server = (MarketItem) _storage.loadOrCreateWithLock(ctx.getLong("id"));

			ItemPermission.checkAppAdmin(server, mp);
			MarketBiddingItemManager.remove(bidding);
			MarketItemHistory.deleteBidding(mp, server, bidding);

			DbService.flush();

			SqlSelect select = sql.getBidLast(ctx.getLong("biddingId"));
			ResultSet rs = select.query();

			Long price = null;
			if (rs.next())
			{
				price = rs.getLong("bidding_price");
				server.setCurrentPrice(price);
			}
			else if (price == null)
			{
				server.setCurrentPrice(server.getStartPrice());
			}
		}
	}
}