package com.kcube.marketBidding;

import java.util.Date;

import com.kcube.doc.Item;

public class MarketBiddingItem extends Item
{
	private static final long serialVersionUID = 3723483789790879366L;

	private Long itemId;
	private Date biddingTime;
	private Long biddingPrice;
	private Boolean isBiddingFinal;
	private Boolean isBiddingCancel;

	public Date getBiddingTime()
	{
		return biddingTime;
	}

	public void setBiddingTime(Date biddingTime)
	{
		this.biddingTime = biddingTime;
	}

	public Long getBiddingPrice()
	{
		return biddingPrice;
	}

	public void setBiddingPrice(Long biddingPrice)
	{
		this.biddingPrice = biddingPrice;
	}

	public Boolean getIsBiddingFinal()
	{
		return isBiddingFinal;
	}

	public void setIsBiddingFinal(Boolean isBiddingFinal)
	{
		this.isBiddingFinal = isBiddingFinal;
	}

	public Boolean getIsBiddingCancel()
	{
		return isBiddingCancel;
	}

	public void setIsBiddingCancel(Boolean isBiddingCancel)
	{
		this.isBiddingCancel = isBiddingCancel;
	}

	public Long getItemId()
	{
		return itemId;
	}

	public void setItemId(Long itemId)
	{
		this.itemId = itemId;
	}

}
