package com.kcube.market;

import java.util.HashMap;
import java.util.Map;

import com.kcube.doc.rfrn.ReferenceListener;
import com.kcube.lib.action.Action;
import com.kcube.lib.action.ActionContext;
import com.kcube.lib.event.EventService;
import com.kcube.lib.jdbc.DbStorage;
import com.kcube.lib.json.JsonMapping;
import com.kcube.marketBidding.MarketBiddingItem;

public abstract class MarketItemAction implements Action
{
	static DbStorage _storage = new DbStorage(MarketItem.class);
	static DbStorage _storageBidding = new DbStorage(MarketItem.Bidding.class);
	static JsonMapping _factory = new JsonMapping(MarketItem.class, "read");
	static JsonMapping _factoryBidding = new JsonMapping(MarketBiddingItem.class, "bidding");
	static JsonMapping _request = new JsonMapping(MarketItem.class, "user");
	static Map<String, String> COLUMNS = new HashMap<String, String>();
	static Map<String, String> ATTRIBUTES = new HashMap<String, String>();

	static
	{
		ATTRIBUTES.put("itemid", "id");
		ATTRIBUTES.put("kmid", "kmid");
		ATTRIBUTES.put("title", "title");
		ATTRIBUTES.put("auth_userid", "userId");
		ATTRIBUTES.put("auth_name", "userName");
		ATTRIBUTES.put("start_time", "startTime");
		ATTRIBUTES.put("end_time", "endTime");
		ATTRIBUTES.put("current_price", "currentPrice");
		ATTRIBUTES.put("immediate_purchase", "immediatePurchase");
		ATTRIBUTES.put("status", "status");
		ATTRIBUTES.put("file_ext", "fileExt");
		ATTRIBUTES.put("isvisb", "isVisb");
		ATTRIBUTES.put("purchase_price", "purchasePrice");
		ATTRIBUTES.put("purchase_time", "purchaseTime");
		ATTRIBUTES.put("purchase_name", "purchaseName");
		ATTRIBUTES.put("purchase_userid", "purchaseUserId");

	}

	public static ReferenceListener _referenceListener = (ReferenceListener) EventService.getDispatcher(
		ReferenceListener.class);

	static final String FIELD = "k.itemid"
		+ ", k.status"
		+ ", k.title"
		+ ", k.auth_name"
		+ ", k.start_time"
		+ ", k.purchase_price"
		+ ", k.purchase_time"
		+ ", k.purchase_name"
		+ ", k.purchase_userid"
		+ ", k.end_time"
		+ ", k.current_price"
		+ ", k.immediate_purchase"
		+ ", k.kmid"
		+ ", k.auth_userid"
		+ ", k.file_ext ";

	/**
	 * ActionContext에서 Item 객체를 추출한다.
	 * <p>
	 * content가 별도의 parameter로 전달된 경우, xml 값보다 우선한다.
	 */
	MarketItem unmarshal(ActionContext ctx) throws Exception
	{
		MarketItem client = (MarketItem) _factory.unmarshal(ctx.getParameter("item"));
		String content = ctx.getParameter("content");
		if (content != null)
		{
			client.setContent(content);
		}
		return client;
	}

	/**
	 * ActionContext에서 Item 객체를 추출한다.
	 * <p>
	 * content가 별도의 parameter로 전달된 경우, xml 값보다 우선한다.
	 */
	MarketBiddingItem unmarshall(ActionContext ctx) throws Exception
	{
		MarketBiddingItem client = (MarketBiddingItem) _factoryBidding.unmarshal(ctx.getParameter("item"));
		String content = ctx.getParameter("content");
		if (content != null)
		{
			client.setContent(content);
		}
		return client;
	}
}
