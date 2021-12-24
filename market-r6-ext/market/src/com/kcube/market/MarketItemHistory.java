package com.kcube.market;

import com.kcube.doc.ItemPermission;
import com.kcube.doc.hist.HistoryCode;
import com.kcube.doc.hist.HistoryManager;
import com.kcube.doc.hist.ReadManager;
import com.kcube.doc.symp.SympathyManager;
import com.kcube.marketBidding.MarketBiddingItem;
import com.kcube.sys.alimi.AlimiManager;
import com.kcube.sys.emp.Employee;
import com.kcube.sys.emp.EmployeeService;
import com.kcube.sys.module.ModuleParam;
import com.kcube.sys.usr.User;

public class MarketItemHistory
{

	public static final Integer MARKET = new Integer(5000);
	public static final Integer BIDDING = new Integer(MARKET + 1);
	public static final Integer ENDBIDDING = new Integer(MARKET + 2);
	public static final Integer DELBIDDING = new Integer(MARKET + 4);
	public static final Integer CLICK = new Integer(MARKET + 3);
	public static final String ALIMI_MARKET = "MARKET";
	public static final String BIDDING_ALIMI = "com.kcube.market.MarketItem.biddingNew";
	public static final String BIDDINGDEL_ALIMI = "com.kcube.market.MarketItem.biddingDel";
	public static final String BIDDINGEND_ALIMI = "com.kcube.market.MarketItem.biddingEnd";
	public static final String CLICK_ALIMI = "com.kcube.market.MarketItem.clickSellButton";
	static SympathyManager _sympathy = new SympathyManager(MarketItem.class);
	static ReadManager _read = new ReadManager(MarketItem.class);

	/**
	 * 게시글 첨부파일 다운로드시 로그를 남긴다.
	 */
	static void downloaded(MarketItem item, MarketItem.Attachment att) throws Exception
	{
		item.setTransientAttachment(att);
		HistoryManager.history(HistoryCode.DOWNLOAD, item);
	}

	/**
	 * 공감 삭제 시 로그를 남긴다.
	 */
	static void deleteSympathy(MarketItem server) throws Exception
	{
		_sympathy.delete(server);
		HistoryManager.history(HistoryCode.SYMPATHY_REMOVED, server);
	}

	/**
	 * 입찰 발생시 전 입찰자에게 알림을 보낸다.
	 */
	static void doBidding(ModuleParam mParam, MarketItem item, MarketBiddingItem bidding, Long userid) throws Exception
	{
		if (bidding.getBiddingPrice() != null)
		{
			AlimiManager.log(
				mParam,
				BIDDING,
				userid,
				item.getTitle(),
				getSystemUser(),
				BIDDING_ALIMI,
				null,
				ALIMI_MARKET,
				item,
				item.getId());
		}
	}

	/**
	 * 경매 종료 알림을 해당 게시물 소유자에게 보낸다.
	 */
	static void endBidding(ModuleParam mParam, MarketItem item) throws Exception
	{
		if (item.getPurchasePrice() != null)
		{
			AlimiManager.log(
				mParam,
				ENDBIDDING,
				item.getRgstUser().getUserId(),
				item.getTitle(),
				getSystemUser(),
				BIDDINGEND_ALIMI,
				null,
				ALIMI_MARKET,
				item,
				item.getId());
		}
	}

	/**
	 * 판매완료 버튼을 누르라고 판매자에게 알림을 보낸다.
	 */
	static void clickCompelete(ModuleParam mParam, MarketItem item) throws Exception
	{
		if (item.getPurchaseComplete() != null)
		{
			AlimiManager.log(
				mParam,
				CLICK,
				item.getRgstUser().getUserId(),
				item.getTitle(),
				getSystemUser(),
				CLICK_ALIMI,
				null,
				ALIMI_MARKET,
				item,
				item.getId());
		}
	}

	/**
	 * 입찰삭제시 입찰자들에게 알림이 가게 한다.
	 * @throws Exception
	 */
	static void deleteBidding(ModuleParam mParam, MarketItem item, MarketBiddingItem bidding) throws Exception
	{
		if (bidding.getAuthor() != null)
		{
			AlimiManager.log(
				mParam,
				DELBIDDING,
				bidding.getAuthor().getUserId(),
				item.getTitle(),
				getSystemUser(),
				BIDDINGDEL_ALIMI,
				null,
				ALIMI_MARKET,
				item,
				item.getId());
		}
	}

	static void addSympathy(ModuleParam mParam, MarketItem item, int type) throws Exception
	{
		_sympathy.add(item, type);
	}

	/**
	 * 시스템의 정보를 User 객체로 리턴해준다.
	 */
	static User getSystemUser() throws Exception
	{
		Employee emp = EmployeeService.getEmployee(0L);
		return new User(emp.getUser());
	}

	/**
	 * 게시글 조회시 로그를 남긴다.
	 */
	static void read(MarketItem item) throws Exception
	{
		_read.read(HistoryCode.READ, item);
		if (!ItemPermission.isOwner(item))
		{
			HistoryManager.history(HistoryCode.READ, item);
		}
	}
}