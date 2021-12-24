package com.kcube.market;

import java.util.Date;

import com.kcube.doc.opn.OpinionManager;
import com.kcube.doc.opn.OpinionReply;
import com.kcube.lib.action.ActionContext;
import com.kcube.lib.jdbc.DbService;
import com.kcube.lib.jdbc.DbStorage;
import com.kcube.lib.json.JsonMapping;
import com.kcube.lib.json.JsonWriter;
import com.kcube.sys.conf.module.ModuleConfigService;
import com.kcube.sys.module.ModuleParam;
import com.kcube.sys.usr.UserPermission;
import com.kcube.sys.usr.UserService;

public class MarketItemOpinion
{

	static OpinionManager _opinion = new OpinionManager(MarketItem.class, MarketItem.Opinion.class);
	static OpinionReply _opinionReply = new OpinionReply(MarketItem.Opinion.class);

	static JsonMapping _opn = new JsonMapping(MarketItem.Opinion.class);
	static DbStorage _opnStorage = new DbStorage(MarketItem.Opinion.class);

	/**
	 * 의견 리스트를 조회한다.
	 * <p>
	 * 게시글에 대한 의견을 조회시 사용한다.
	 */
	public static class ViewOpinion extends MarketItemAction
	{
		public void execute(ActionContext ctx) throws Exception
		{

			ModuleParam mp = ctx.getModuleParam();
			UserPermission.setModuleMenu(mp);

			Long id = ctx.getLong("id");
			boolean desc = ctx.getBoolean("desc");
			Long opnLastId = ctx.getLong("opnLastId", 0);
			boolean isSympathy = ctx.getBoolean("isSympathy");

			MarketItem server = (MarketItem) _storage.load(id);
			MarketItemPermission.checkUser(server, mp);

			_request.marshal(ctx.getWriter(), _opinion.getItem(server, opnLastId, desc, isSympathy));
		}
	}

	/**
	 * 의견을 등록한다.
	 * <p>
	 * 익명 기능 포함
	 */
	public static class AddOpinion extends MarketItemAction
	{
		public void execute(ActionContext ctx) throws Exception
		{
			ModuleParam mp = ctx.getModuleParam();
			UserPermission.setModuleMenu(mp);

			boolean useCtgr = ModuleConfigService.useCtgr(UserService.getTenantId(), mp.getModuleId(), mp.getAppId());
			MarketItem.Opinion client = (MarketItem.Opinion) _opn.unmarshal(ctx.getParameter("opn"));

			MarketItem server = (MarketItem) _storage.load(client.getItemId());

			MarketItem.Opinion opinion = new MarketItem.Opinion();
			opinion.setGid(client.getGid());
			opinion.setContent(client.getContent());
			opinion.setItemId(client.getItemId());
			opinion.setOpnCode(MarketItem.Opinion.USER);
			opinion.setAlimiMentionUsers(client.getAlimiMentionUsers());
			if (client.getRgstUser() == null)
			{
				opinion.setRgstUser(UserService.getUser());
			}
			else
			{
				opinion.setRgstUser(client.getRgstUser());
			}
			opinion.setRgstUserId(UserService.getUserId());
			opinion.setRgstDate(new Date());
			_opinion.addOpinion(opinion.getItemId(), opinion);
			_opn.marshal(ctx.getWriter(), opinion);
		}
	}

	/**
	 * 의견을 수정한다.
	 */
	public static class UpdateOpinion extends MarketItemAction
	{
		public void execute(ActionContext ctx) throws Exception
		{
			Long id = ctx.getLong("id");
			Long vrtl = ctx.getLong("vrtl", null);
			String name = ctx.getParameter("name");
			String content = ctx.getParameter("content");
			MarketItem.Opinion opn = (MarketItem.Opinion) DbService.load(MarketItem.Opinion.class, id);
			MarketItemPermission.checkOwner(opn);
			_opinion.updateOpinion(opn, vrtl, name, content);
			_opinion.writeUpdate(new JsonWriter(ctx.getWriter()), opn);
		}
	}

	/**
	 * 의견을 삭제한다.
	 */
	public static class DeleteOpinion extends MarketItemAction
	{
		public void execute(ActionContext ctx) throws Exception
		{
			Long id = ctx.getLong("id");
			MarketItem.Opinion opn = (MarketItem.Opinion) DbService.load(MarketItem.Opinion.class, id);
			MarketItemPermission.checkOwner(opn);
			_opinionReply.checkReply(opn);
			_opinion.deleteOpinion(id);
		}
	}

}
