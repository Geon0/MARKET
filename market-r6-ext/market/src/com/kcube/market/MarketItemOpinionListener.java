package com.kcube.market;

import com.kcube.ekp.bbs.BdItem.Opinion;

public interface MarketItemOpinionListener
{
	public void add(MarketItem item, Opinion opn) throws Exception;

	public void update(MarketItem item, Opinion opn) throws Exception;

	public void delete(MarketItem item, Opinion opn) throws Exception;
}
