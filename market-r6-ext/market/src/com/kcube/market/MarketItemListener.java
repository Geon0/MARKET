package com.kcube.market;

public interface MarketItemListener
{
	/**
	 * 게시글이 등록된 후 호출 된다.
	 */
	void registered(MarketItem server) throws Exception;

	/**
	 * 게시글이 수정된 후 호출된다.
	 */
	void modified(MarketItem server) throws Exception;

	/**
	 * 게시글이 삭제된 후 호출된다.
	 */
	void deleted(MarketItem server) throws Exception;
}
