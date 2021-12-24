package com.kcube.market;

import com.kcube.lib.action.ActionService;
import com.kcube.sys.AppBoot;

/**
 * 게시판 Action 등록 Class
 */
public class MarketItemBoot implements AppBoot
{
	/**
	 * 게시판에서 사용하는 액션을 추가한다.
	 */
	public void init() throws Exception
	{
		ActionService.addAction(new MarketItemUser());
		ActionService.addAction(new MarketItemOwner());
		ActionService.addAction(new MarketItemOpinion());
		ActionService.addAction(new MarketItemAdmin());
		ActionService.addAction(new MarketJob());
		ActionService.addAction(new MarketItemViewer());

	}

	public void destroy()
	{
	}

}
