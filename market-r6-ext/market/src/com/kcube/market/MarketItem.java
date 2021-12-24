package com.kcube.market;

import java.util.Date;
import java.util.Set;

import com.kcube.doc.Item;
import com.kcube.sys.usr.User;
import com.kcube.sys.usr.UserService;

/**
 * 게시판 Bean class
 */
public class MarketItem extends Item
{
	private static final long serialVersionUID = 8471378302451600432L;

	private String _cstmField1;
	private String _cstmField2;
	private String _cstmField3;
	private int _statusDelete;
	private Boolean _isBest;
	private Boolean _isPriceBest;
	private Date _startTime;
	private Date _endTime;
	private Boolean _isDonate;
	private Long _startPrice;
	private Long _minimumAmount;
	private Long _immediatePurchase;
	private Long _currentPrice;
	private Date _purchaseTime;
	private Long _purchasePrice;
	private Long _purchaseUserid;
	private String _purchaseName;
	private String _purchaseDisp;
	private Date _purchaseComplete;
	private Date _salesComplete;
	private Set<Bidding> _biddings;

	/**
	 * @see com.kcube.doc.Item#getRgstUser()
	 */

	/**
	 * 판매등록 상태
	 */
	public static final int REGISTERED_STATUS = 3000;

	/**
	 * 판매중 상태
	 */
	public static final int SELLING_STATUS = 3200;

	/**
	 * 삭제 상태
	 */
	public static final int DELETED_STATUS = 9000;

	/**
	 * 경매종료 상태
	 */
	public static final int TRADEWAIT_STATUS = 4000;

	/**
	 * 구매완료 상태
	 */
	public static final int BUYCOMPELETE_STATUS = 5000;

	/**
	 * 판매완료 상태
	 */
	public static final int SELLCOMPLELETE_STATUS = 6000;

	public User getRgstUser()
	{
		User rgstUser = super.getRgstUser();
		if (rgstUser == null)
		{
			rgstUser = UserService.getUser();
			setRgstUser(rgstUser);
		}
		return rgstUser;
	}

	/**
	 * 필요에 따라 정의하는 custom field의 값을 돌려준다.
	 */
	public String getCstmField1()
	{
		return _cstmField1;
	}

	public void setCstmField1(String cstmField1)
	{
		_cstmField1 = cstmField1;
	}

	/**
	 * @see #getCstmField1()
	 */
	public String getCstmField2()
	{
		return _cstmField2;
	}

	public void setCstmField2(String cstmField2)
	{
		_cstmField2 = cstmField2;
	}

	/**
	 * @see #getCstmField1()
	 */
	public String getCstmField3()
	{
		return _cstmField3;
	}

	public void setCstmField3(String cstmField3)
	{
		_cstmField3 = cstmField3;
	}

	/**
	 * 첨부파일을 나타내는 클래스이다.
	 */
	public static class Attachment extends com.kcube.doc.file.Attachment
	{
		private static final long serialVersionUID = -8314404918214654922L;
	}

	/**
	 * 입찰정보를 나타내는 클래스이다.
	 */
	public static class Bidding extends com.kcube.marketBidding.MarketBiddingItem
	{
		private static final long serialVersionUID = 1211804268843831075L;
	}

	/**
	 * 지식의 의견을 나타내는 클래스이다.
	 */
	public static class Opinion extends com.kcube.doc.opn.Opinion
	{
		private static final long serialVersionUID = 3620516429084485829L;

		private Long _rgstUserId;

		/**
		 * 등록자의 UserId
		 */
		public Long getRgstUserId()
		{
			return _rgstUserId;
		}

		public void setRgstUserId(Long rgstUserId)
		{
			_rgstUserId = rgstUserId;
		}

		/**
		 * 현재 사용자가 의견의 작성자 인지의 여부를 돌려준다.
		 */
		public boolean isCurrentOwner()
		{
			Long userId = UserService.getUserId();
			if (userId == null)
			{
				return false;
			}
			return (userId.equals(getRgstUserId()));
		}

		public void setCurrentOwner(boolean currentOwner)
		{
		}

	}

	public int getStatusDelete()
	{
		return _statusDelete;
	}

	public void setStatusDelete(int statusDelete)
	{
		_statusDelete = statusDelete;
	}

	public Boolean getIsBest()
	{
		return _isBest;
	}

	public void setIsBest(Boolean isBest)
	{
		_isBest = isBest;
	}

	public Boolean getIsPriceBest()
	{
		return _isPriceBest;
	}

	public void setIsPriceBest(Boolean isPriceBest)
	{
		_isPriceBest = isPriceBest;
	}

	public Date getStartTime()
	{
		return _startTime;
	}

	public void setStartTime(Date startTime)
	{
		_startTime = startTime;
	}

	public Date getEndTime()
	{
		return _endTime;
	}

	public void setEndTime(Date endTime)
	{
		_endTime = endTime;
	}

	public Boolean getIsDonate()
	{
		return _isDonate;
	}

	public void setIsDonate(Boolean isDonate)
	{
		_isDonate = isDonate;
	}

	public Long getStartPrice()
	{
		return _startPrice;
	}

	public void setStartPrice(Long startPrice)
	{
		_startPrice = startPrice;
	}

	public Long getMinimumAmount()
	{
		return _minimumAmount;
	}

	public void setMinimumAmount(Long minimumAmount)
	{
		_minimumAmount = minimumAmount;
	}

	public Long getImmediatePurchase()
	{
		return _immediatePurchase;
	}

	public void setImmediatePurchase(Long immediatePurchase)
	{
		_immediatePurchase = immediatePurchase;
	}

	public Long getCurrentPrice()
	{
		return _currentPrice;
	}

	public void setCurrentPrice(Long currentPrice)
	{
		_currentPrice = currentPrice;
	}

	public Date getPurchaseTime()
	{
		return _purchaseTime;
	}

	public void setPurchaseTime(Date purchaseTime)
	{
		_purchaseTime = purchaseTime;
	}

	public Long getPurchasePrice()
	{
		return _purchasePrice;
	}

	public void setPurchasePrice(Long purchasePrice)
	{
		_purchasePrice = purchasePrice;
	}

	public Long getPurchaseUserid()
	{
		return _purchaseUserid;
	}

	public void setPurchaseUserid(Long purchaseUserid)
	{
		_purchaseUserid = purchaseUserid;
	}

	public String getPurchaseDisp()
	{
		return _purchaseDisp;
	}

	public void setPurchaseDisp(String purchaseDisp)
	{
		_purchaseDisp = purchaseDisp;
	}

	public Date getPurchaseComplete()
	{
		return _purchaseComplete;
	}

	public void setPurchaseComplete(Date purchaseComplete)
	{
		_purchaseComplete = purchaseComplete;
	}

	public Date getSalesComplete()
	{
		return _salesComplete;
	}

	public void setSalesComplete(Date salesComplete)
	{
		_salesComplete = salesComplete;
	}

	public String getPurchaseName()
	{
		return _purchaseName;
	}

	public void setPurchaseName(String purchaseName)
	{
		_purchaseName = purchaseName;
	}

	public Set<Bidding> getBiddings()
	{
		return _biddings;
	}

	public void setBiddings(Set<Bidding> biddings)
	{
		_biddings = biddings;
	}
}