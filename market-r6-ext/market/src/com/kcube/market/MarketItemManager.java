package com.kcube.market;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.kcube.doc.file.AttachmentManager;
import com.kcube.lib.event.EventService;
import com.kcube.lib.jdbc.DbService;
import com.kcube.lib.sql.SqlSelect;
import com.kcube.sys.emp.Employee;
import com.kcube.sys.emp.EmployeeService;
import com.kcube.sys.module.ModuleParam;
import com.kcube.sys.usr.User;
import com.kcube.sys.usr.UserService;

public class MarketItemManager
{
	private static MarketItemListener _listener = (MarketItemListener) EventService.getDispatcher(
		MarketItemListener.class);
	private static AttachmentManager _attachment = new AttachmentManager(true);

	/**
	 * client 값을 받아 server에 업데이트 한다.
	 */
	static void update(MarketItem server, MarketItem client) throws Exception
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
		server.setFolder(client.getFolder());
		server.updateFolderLevel();
		server.setIsDonate(client.getIsDonate());
		if (client.getStartTime() != null)
		{
			server.setStartTime(client.getStartTime());
			server.setEndTime(client.getEndTime());
		}
		if (client.getStartPrice() != null)
		{
			server.setCurrentPrice(client.getStartPrice());
		}
		if (client.getCurrentPrice() != null)
		{
			server.setCurrentPrice(client.getCurrentPrice());
		}
		if (client.getMinimumAmount() == null || client.getMinimumAmount() == 0)
		{
			server.setMinimumAmount(1000L);
		}
		else
		{
			server.setMinimumAmount(client.getMinimumAmount());
		}
		server.setStartPrice(client.getStartPrice());
		if (client.getImmediatePurchase() == null || client.getImmediatePurchase() == 0)
		{
			server.setIsDonate(true);
			server.setImmediatePurchase(0L);
		}
		else
		{
			server.setImmediatePurchase(client.getImmediatePurchase());
		}

		server.setMobile(client.isMobile());
		server.setContent(client.getContent());
		server.setFolder(client.getFolder());
		server.setFolders(client.getFolders());
		server.setScrtLevel(client.getScrtLevel());
		server.setSecurities(client.getSecurities());
		server.setTitle(client.getTitle());
		server.setLastUpdt(new Date());
		server.setTags(client.getTags());
		server.setModuleId(client.getModuleId());
		server.setAppId(client.getAppId());
		server.setClassId(client.getClassId());
		server.setSpaceId(client.getSpaceId());
		server.updateAttachments(_attachment.update(client.getAttachments(), server));
		server.updateVisible(server.isVisible());
		if (client.getId() != null && server.isVisible())
		{
			_listener.modified(server);
		}
	}

	/**
	 * 경매글을 판매등록상태로 한다.
	 */
	static void register(MarketItem server) throws Exception
	{
		server.setStatus(MarketItem.REGISTERED_STATUS);
		server.setLastUpdt(new Date());
		server.setRgstDate(new Date());
		server.updateVisible(true);
		_listener.registered(server);
	}

	/**
	 * 경매글을 판매중상태로 한다.
	 */
	static void selling(MarketItem server) throws Exception
	{
		server.setStatus(MarketItem.SELLING_STATUS);
		server.setLastUpdt(new Date());
		server.setRgstDate(new Date());
		server.updateVisible(true);
	}

	/**
	 * 경매글을 거래대기상태로 한다.
	 */
	static void endBidding(MarketItem server) throws Exception
	{
		server.setStatus(MarketItem.TRADEWAIT_STATUS);
	}

	/**
	 * 경매글을 거래완료상태로 한다.
	 */
	static void dealCompelete(MarketItem server) throws Exception
	{
		server.setStatus(MarketItem.BUYCOMPELETE_STATUS);
		server.setPurchaseComplete(new Date());
	}

	/**
	 * 경매글을 판매완료상태로 한다.
	 */
	static void sellCompelete(MarketItem server) throws Exception
	{
		server.setStatus(MarketItem.SELLCOMPLELETE_STATUS);
		server.setSalesComplete(new Date());
	}

	/**
	 * 판매글을 즉시 구매한다.
	 */
	static void imPurchase(MarketItem server, MarketItem client) throws Exception
	{
		server.setPurchasePrice(server.getImmediatePurchase());
		server.setPurchaseUserid(UserService.getUserId());
		server.setPurchaseName(UserService.getUser().getName());
		server.setPurchaseDisp(UserService.getUser().getDisplayName());
		server.setPurchaseTime(new Date());
		server.setStatus(MarketItem.TRADEWAIT_STATUS);
	}

	/**
	 * 경매글을 삭제한다.
	 */
	static void delete(MarketItem server) throws Exception
	{
		server.setStatusDelete(server.getStatus());
		server.setStatus(MarketItem.DELETED_STATUS);
		server.setLastUpdt(new Date());
		server.updateVisible(false);
		_listener.deleted(server);
	}

	/**
	 * 경매글을 폐기한다. db에서 삭제하고 첨부파일도 삭제한다. 복원할 수 없다.
	 */
	static void remove(MarketItem server) throws Exception
	{
		_attachment.remove(server.getAttachments());
		DbService.remove(server);
	}

	/**
	 * 경매글을 복원한다 (만약 경매 종료시간보다 현재시간이 지나있을시 이전상태값이 아닌 경매종료상태로 변경한다.)
	 */
	static void recover(MarketItem server) throws Exception
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date currentTime = new Date();
		String current = format.format(currentTime);

		Date today = format.parse(current);
		Date endTime = server.getEndTime();

		int result = today.compareTo(endTime);

		if (result == 1 || result == 0)
			server.setStatus(MarketItem.TRADEWAIT_STATUS);
		else
		{
			server.setStatus(server.getStatusDelete());
		}
		server.updateVisible(true);
	}

	/**
	 * 경매글을 추천 게시물로 설정한다..
	 */
	static void recommend(MarketItem server) throws Exception
	{
		server.setIsBest(true);
	}

	/**
	 * 추천 경매 게시글을 해제한다..
	 */
	static void Norecommend(MarketItem server) throws Exception
	{
		server.setIsBest(false);
	}

	/**
	 * 경매글의 입찰자정보를 SET해준다.
	 */
	static void setBiddingInfo(MarketItem server, ModuleParam mp, long id) throws Exception
	{
		MarketItemSql sql = new MarketItemSql(mp);

		SqlSelect select = sql.getBidLast(id);
		ResultSet rs = select.query();

		if (rs.next())
		{
			server.setPurchaseName(rs.getString("name"));
			server.setPurchaseTime(new Date());
			server.setPurchasePrice(rs.getLong("bidding_price"));
			server.setPurchaseUserid(rs.getLong("userid"));
		}

		SqlSelect sub = sql.getUserId(id);
		ResultSet subRs = sub.query();
		if (subRs.next())
		{
			Employee emp = EmployeeService.getEmployee(subRs.getLong("userid"));
			server.setPurchaseDisp(emp.getUser().getDisplayName());
		}

		server.setStatus(MarketItem.TRADEWAIT_STATUS);
	}

	/**
	 * 판매중 상태의 게시물을 업데이트 한다.
	 */
	public static void updateSelling(MarketItem server, MarketItem client) throws Exception
	{
		if (client.getContent() != null)
		{
			server.setContent(client.getContent());
		}
		if (client.getAttachments() != null)
		{
			server.updateAttachments(_attachment.update(client.getAttachments(), server));
		}

		if (client.getEndTime() != null)
		{
			server.setEndTime(client.getEndTime());
		}
	}
}
