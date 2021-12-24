package com.kcube.market;

import com.kcube.marketBidding.MarketBiddingItem;
import com.kcube.sys.usr.PermissionDeniedException;

public class MarketBiddingItemPermission
{
	/**
	 * 입찰이 가능한 상태인지 체크한다.
	 */
	public static void canBidding(MarketItem item, MarketBiddingItem client) throws Exception
	{
		int status = item.getStatus();
		if (status != MarketItem.SELLING_STATUS || client.getBiddingPrice() <= item.getCurrentPrice())
		{
			throw new PermissionDeniedException();
		}
	}
}
