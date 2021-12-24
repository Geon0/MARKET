package com.kcube.market;

import com.kcube.doc.InvalidStatusException;
import com.kcube.doc.ItemPermission;
import com.kcube.doc.reply.ReplyExistsException;
import com.kcube.doc.reply.ReplyManager;
import com.kcube.ekp.bbs.BdItemPermission.ReadDeniedException;
import com.kcube.sys.AppException;
import com.kcube.sys.module.ModuleParam;
import com.kcube.sys.usr.PermissionDeniedException;
import com.kcube.sys.usr.UserPermission;

public class MarketItemPermission
{

	private static ReplyManager _reply = new ReplyManager(MarketItem.class);

	/**
	 * 현재 사용자가 첨부 조회권한이 있는지 확인한다.
	 */
	public static void checkAttachUser(MarketItem item, ModuleParam mp) throws Exception
	{
		if (!ItemPermission.isAttachUser(item, mp))
		{
			throw new ReadDeniedException();
		}
	}

	/**
	 * 현재 사용자가 게시글 조회권한이 있는지 확인한다.
	 */
	public static void checkUser(MarketItem item, ModuleParam mp) throws Exception
	{
		if (!ItemPermission.isUser(item, mp))
		{
			throw new ReadDeniedException();
		}
	}

	/**
	 * 게시글이 삭제 상태일 때 조회시 발생한다.
	 */
	public static class ItemDeletedStatus extends AppException
	{
		private static final long serialVersionUID = -5204140864969498207L;
	}

	/**
	 * 소유자가 문서를 수정할 수 있는 상태인지 확인한다.
	 */
	public static void update(MarketItem item) throws Exception
	{
		int status = item.getStatus();
		if (status != MarketItem.SELLING_STATUS && status != MarketItem.REGISTERED_STATUS)
		{
			throw new InvalidStatusException();
		}
	}

	/**
	 * 소유자가 게시글을 삭제 할 수 있는 상태인지 확인한다.
	 */
	public static void deleteByOwner(MarketItem item) throws Exception
	{
		if (_reply.hasReplyExclOfStatus(item))
		{
			throw new ReplyExistsException();
		}
		ItemPermission.delete(item);
	}

	/**
	 * 등록자 / 작성자 인지 확인한다.
	 */
	public static void checkOwner(MarketItem.Opinion opn) throws Exception
	{
		if (!isRgstUser(opn))
		{
			throw new PermissionDeniedException();
		}
	}

	/**
	 * 의견 등록 여부을 확인한다.
	 */
	public static boolean isRgstUser(MarketItem.Opinion opn)
	{
		return (opn.isCurrentOwner() || UserPermission.isAdmin());
	}

	/**
	 * 관리자가 게시글을 삭제 할 수 있는 상태인지 확인한다.
	 */
	public static void deleteCheckReply(MarketItem server) throws Exception
	{
		if (_reply.hasReplyExclOfStatus(server))
		{
			throw new ReplyExistsException();
		}
	}
}
