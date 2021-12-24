package com.kcube.market;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.kcube.doc.Item.Security;
import com.kcube.doc.ItemPermission;
import com.kcube.doc.file.Attachment;
import com.kcube.lib.action.ActionContext;
import com.kcube.lib.jdbc.DbService;
import com.kcube.lib.json.JsonWriter;
import com.kcube.lib.sql.SqlSelect;
import com.kcube.marketBidding.MarketBiddingItem;
import com.kcube.sys.conf.module.ModuleConfigService;
import com.kcube.sys.module.ModuleParam;
import com.kcube.sys.usr.User;
import com.kcube.sys.usr.UserPermission;
import com.kcube.sys.usr.UserService;

/**
 * 게시판 사용자 Action
 */
class MarketItemUser
{

	/**
	 * 사내마켓 목록을 돌려준다.
	 */
	public static class ListByUser extends MarketItemAction
	{
		public void execute(ActionContext ctx) throws Exception
		{
			ModuleParam mp = ctx.getModuleParam();
			UserPermission.setModuleMenu(mp);

			int postType = ctx.getInt("postType", -1);
			Long[] checkBox = ctx.getLongTokens("checkBox");

			MarketItemSql sql = new MarketItemSql(
				mp, ctx.getLong("tr", null), ctx.getParameter("ts"), ctx.getBoolean("isCountDisplay"), true);

			SqlSelect select = sql.getListByUser(postType, checkBox);
			SqlSelect count = sql.getCountByUser(postType, checkBox);

			sql.writeJson(ctx.getWriter(), select, count);
		}
	}

	/**
	 * 사내마켓 판매글을 조회한다.
	 */
	public static class ReadByUser extends MarketItemAction
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
			MarketItemPermission.checkUser(server, mp);
			MarketItemHistory.read(server);
			_factory.marshal(ctx.getWriter(), server);
		}
	}

	/**
	 * 사내마켓 판매글을 등록한다.
	 */
	public static class DoRegister extends MarketItemAction
	{
		public void execute(ActionContext ctx) throws Exception
		{
			ModuleParam mp = ctx.getModuleParam();
			UserPermission.setModuleMenu(mp);

			MarketItem client = unmarshal(ctx);
			MarketItem server = (MarketItem) _storage.loadOrCreateWithLock(client.getId());

			MarketItemManager.update(server, client);
			MarketItemManager.register(server);

			ctx.setParameter("id", server.getId().toString());
		}
	}

	/**
	 * 사내마켓 판매글에 입찰을 등록한다.
	 */
	public static class DoBidding extends MarketItemAction
	{
		public void execute(ActionContext ctx) throws Exception
		{

			ModuleParam mp = ctx.getModuleParam();
			UserPermission.setModuleMenu(mp);
			MarketItemSql sql = new MarketItemSql(mp);

			MarketBiddingItem client = unmarshall(ctx);
			MarketBiddingItem bidding = (MarketBiddingItem) _storageBidding.loadOrCreateWithLock(client.getItemId());
			MarketItem server = (MarketItem) _storage.loadWithLock(client.getId());

			MarketBiddingItemPermission.canBidding(server, client);

			MarketBiddingItemManager.bidding(client, server, bidding);
			MarketBiddingItemManager.isMultiBid(mp, server, client.getId());

			Long userid = sql.getBeforeBid(client.getId());
			MarketItemHistory.doBidding(mp, server, bidding, userid);
		}
	}

	/**
	 * 사내마켓 거래타입 게시글의 게시물을 즉시 구매한다.
	 */
	public static class imPurchase extends MarketItemAction
	{
		public void execute(ActionContext ctx) throws Exception
		{
			ModuleParam mp = ctx.getModuleParam();
			UserPermission.setModuleMenu(mp);

			MarketItem client = unmarshal(ctx);
			Long id = ctx.getLong("id");

			MarketItem server = (MarketItem) _storage.loadWithLock(id);
			MarketItemManager.imPurchase(server, client);

			MarketItemHistory.endBidding(mp, server);
		}
	}

	/**
	 * 첨부파일을 다운로드 한다.
	 */
	public static class DownloadByUser extends MarketItemAction
	{
		public void execute(ActionContext ctx) throws Exception
		{
			ModuleParam mp = ctx.getModuleParam();
			UserPermission.setModuleMenu(mp);

			boolean useCtgr = ModuleConfigService.useCtgr(UserService.getTenantId(), mp.getModuleId(), mp.getAppId());
			MarketItem.Attachment att = (MarketItem.Attachment) DbService.load(
				MarketItem.Attachment.class,
				ctx.getLong("id"));

			MarketItem server = (MarketItem) att.getItem();
			if (useCtgr)
				ItemPermission.checkFolderPermission(server, mp);
			MarketItemPermission.checkAttachUser(server, mp);
			MarketItemHistory.downloaded(server, att);
			ctx.store(att);
		}
	}

	/**
	 * 목록에서 첨부파일을 조회할 때에 사용할 첨부파일의 목록을 돌려준다.
	 */
	public static class AttachmentList extends MarketItemAction
	{
		public void execute(ActionContext ctx) throws Exception
		{
			ModuleParam mp = ctx.getModuleParam();
			UserPermission.setModuleMenu(mp);

			boolean useCtgr = ModuleConfigService.useCtgr(UserService.getTenantId(), mp.getModuleId(), mp.getAppId());
			MarketItem server = (MarketItem) _storage.load(ctx.getLong("id"));

			if (useCtgr)
				ItemPermission.checkFolderPermission(server, mp);

			MarketItemPermission.checkAttachUser(server, mp);
			ItemPermission.read(server);

			JsonWriter writer = new JsonWriter(ctx.getWriter());
			writer.writeListHeader();
			Collection<? extends Attachment> c = server.getAttachments();
			if (c != null)
			{
				for (Iterator<? extends Attachment> i = c.iterator(); i.hasNext();)
				{
					MarketItem.Attachment att = (MarketItem.Attachment) i.next();
					writer.startList();
					writer.setFirstAttr(true);
					writer.setAttribute("id", att.getId());
					writer.setAttribute("filename", att.getFilename());
					writer.setAttribute("size", att.getFilesize());
					writer.endList();
				}
			}
			writer.writeListFooter();
		}
	}

	/**
	 * 게시글을 공감한다.
	 */
	public static class SympathyByUser extends MarketItemAction
	{
		public void execute(ActionContext ctx) throws Exception
		{
			ModuleParam mp = ctx.getModuleParam();
			UserPermission.setModuleMenu(mp);
			boolean useCtgr = ModuleConfigService.useCtgr(UserService.getTenantId(), mp.getModuleId(), mp.getAppId());
			MarketItem server = (MarketItem) _storage.load(ctx.getLong("id"));
			if (useCtgr)
				ItemPermission.checkFolderPermission(server, mp);

			MarketItemPermission.checkUser(server, mp);
			MarketItemHistory.addSympathy(mp, server, ctx.getInt("type"));

			JsonWriter writer = new JsonWriter(ctx.getWriter());
			writer.writeHeader();
			writer.setAttribute("userId", UserService.getUser().getUserId());
			writer.setAttribute("name", UserService.getUser().getName());
			writer.setAttribute("displayName", UserService.getUser().getDisplayName());
			writer.setAttribute("sympType", ctx.getInt("type"));
			writer.writeFooter();
		}
	}

	/**
	 * 보안설정 공유범위 목록을 돌려준다.
	 * <p>
	 * 나의지식의 공유범위 조회와 함께 사용됨으로 Owner일경우 checkItemStatus permission체크를 사용하지 않는다.
	 */
	public static class ViewSecurities extends MarketItemAction
	{
		public void execute(ActionContext ctx) throws Exception
		{
			ModuleParam mp = ctx.getModuleParam();
			UserPermission.setModuleMenu(mp);

			boolean useCtgr = ModuleConfigService.useCtgr(UserService.getTenantId(), mp.getModuleId(), mp.getAppId());

			MarketItem server = (MarketItem) _storage.load(ctx.getLong("id"));
			MarketItemPermission.checkUser(server, mp);

			if (useCtgr)
				ItemPermission.checkFoldersPermission(server, mp);

			if (!ItemPermission.isOwner(server))
			{
				ItemPermission.read(server);
			}
			List<Security> securities = server.getSecurities();

			JsonWriter writer = new JsonWriter(ctx.getWriter());
			writer.writeListHeader();
			for (Security sec : securities)
			{
				writer.startList();
				writer.setFirstAttr(true);
				writer.setAttribute("title", sec.getTitle());
				writer.endList();
			}
			writer.writeListFooter();
		}
	}

	/**
	 * 해당 컨텐츠의 이전글, 다음글을 출력한다.
	 */
	public static class DuplexByUser extends MarketItemAction
	{
		public void execute(ActionContext ctx) throws Exception
		{
			ModuleParam mp = ctx.getModuleParam();
			UserPermission.setModuleMenu(mp);

			Long id = ctx.getLong("id");
			MarketItemSql sql = new MarketItemSql(
				mp, ctx.getLong("tr", null), ctx.getParameter("ts"), ctx.getBoolean("isCountDisplay"), true);

			SqlSelect select = sql.getVisibleSelect(false);
			sql.writeDuplexJson(ctx.getWriter(), select, id);
		}
	}

	/**
	 * 첨부파일을 조회할 수 있는 권한이 있는지를 돌려준다.
	 */
	public static class AttachemntPermission extends MarketItemAction
	{
		public void execute(ActionContext ctx) throws Exception
		{
			MarketItem server = (MarketItem) _storage.load(ctx.getLong("id"));
			boolean isUser = ItemPermission.isAttachUser(server);
			JsonWriter writer = new JsonWriter(ctx.getWriter());
			writer.writeHeader();
			writer.setAttribute("isUser", isUser);
			writer.setAttribute("array");
			writer.writeAloneListHeader();
			if (!isUser)
			{
				User author = server.getAuthor();
				writeAuthor(writer, author);
			}
			writer.writeAloneListFooter();
			writer.writeFooter();
		}
	}

	private static void writeAuthor(JsonWriter writer, User author) throws Exception
	{
		writer.startList();
		writer.setFirstAttr(true);
		writer.setAttribute("id", author.getUserId());
		writer.setAttribute("name", author.getName());
		writer.setAttribute("displayName", author.getDisplayName());
		writer.endList();
	}

	/**
	 * 게시글을 공감 삭제한다.
	 */
	public static class DelSympathyByUser extends MarketItemAction
	{
		public void execute(ActionContext ctx) throws Exception
		{
			MarketItem server = (MarketItem) _storage.load(ctx.getLong("id"));
			MarketItemHistory.deleteSympathy(server);
		}
	}

	/**
	 * 공감 리스트를 조회한다. 게시글에 대한 공감 더보기 시 사용한다.
	 */
	public static class ViewSympathyByUser extends MarketItemAction
	{
		public void execute(ActionContext ctx) throws Exception
		{
			ModuleParam mp = ctx.getModuleParam();
			UserPermission.setModuleMenu(mp);

			Long id = ctx.getLong("id");
			Long sympLastUserId = ctx.getLong("sympLastUserId", 0);

			MarketItem server = (MarketItem) _storage.load(id);
			MarketItemPermission.checkUser(server, mp);

			_request.marshal(ctx.getWriter(), MarketItemHistory._sympathy.getItem(server, sympLastUserId));
		}
	}

}