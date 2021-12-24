package com.kcube.market;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.kcube.lib.sql.Sql;
import com.kcube.lib.sql.SqlChooser;
import com.kcube.lib.sql.SqlDuplexWriter;
import com.kcube.lib.sql.SqlPage;
import com.kcube.lib.sql.SqlSelect;
import com.kcube.lib.sql.SqlTable;
import com.kcube.lib.sql.SqlWriter;
import com.kcube.lib.tree.Node;
import com.kcube.lib.tree.NodeVisitor;
import com.kcube.lib.tree.Tree;
import com.kcube.map.Folder;
import com.kcube.map.FolderArraySql;
import com.kcube.map.FolderCache;
import com.kcube.map.FolderScrt;
import com.kcube.map.FolderSql;
import com.kcube.sys.conf.module.ModuleConfigService;
import com.kcube.sys.module.ModuleParam;
import com.kcube.sys.usr.UserService;

public class MarketItemSql
{

	private static final SqlTable MARKETITEM = new SqlTable("market_item", "i");
	private static final SqlTable MARKETITEMBIDDING = new SqlTable("market_item_bidding", "s");
	private static SqlTable KM_SCRT = new SqlTable("km_scrt");
	private List<Long> _excludeKmId = new LinkedList<Long>();
	private static final String INDEX = "XIE_MARKETITEM_";
	private static SqlWriter _writer = new SqlWriter().putAll(MARKETITEM);
	private static SqlWriter _biddingWriter = new SqlWriter().putAll(MARKETITEMBIDDING);
	private static SqlDuplexWriter _duplexWriter = new SqlDuplexWriter().putAll(MARKETITEM);

	private ModuleParam _mp;
	private Long _folderId;
	private Long[] _trs;
	private Long _moduleId;
	private Long _appId;
	private int _level;
	private String _ts;
	private boolean _moduleMenu;
	private boolean _rootInclude;
	private boolean _countVisible;
	private SqlPage _page;
	private boolean _useCtgr;
	private boolean _userCtgrReadScrt;
	private boolean _useAdminCtgr;

	public MarketItemSql(String ts)
	{
		_ts = ts;
	}

	public MarketItemSql(ModuleParam mp, Long folderId, String ts) throws Exception
	{
		this(mp, folderId, ts, true, true);
	}

	public MarketItemSql(ModuleParam mp) throws Exception
	{
		this(mp, null, null);
	}

	public MarketItemSql(ModuleParam mp, Long folderId, String ts, boolean countVisible) throws Exception
	{
		this(mp, folderId, ts, countVisible, true);
	}

	public MarketItemSql(ModuleParam mp, Long folderId, String ts, boolean countVisble, boolean rootInclude)
		throws Exception
	{
		_mp = mp;
		_folderId = folderId;
		Tree.Entry entry = FolderCache.getTree().getEntry(_folderId);
		_level = (entry != null) ? entry.getAncestorsOrSelfCount() : 0;

		_ts = ts;
		_moduleId = mp.getModuleId();
		_appId = mp.getAppId();
		_moduleMenu = mp.isModuleMenu();
		_page = new SqlPage(MARKETITEM.aliasToColumn(), _ts);

		_rootInclude = rootInclude;
		_countVisible = countVisble;

		_useCtgr = ModuleConfigService.useCtgr(UserService.getTenantId(), _moduleId, _appId);
		_useAdminCtgr = ModuleConfigService.useAdminCtgr(UserService.getTenantId(), _moduleId);

		makeExcludedList();
	}

	/**
	 * User 목록 조회 <거래, 기부 체크> <추천, 인기입찰 체크>
	 */
	public SqlSelect getListByUser(int postType, Long[] checkBox) throws Exception
	{
		SqlSelect stmt = new SqlSelect();
		stmt.select(MARKETITEM, "list");
		stmt.where("i.isvisb = 1");
		stmt.where("i.moduleid = ?", _moduleId);
		if (!_moduleMenu)
			stmt.where("i.appid = ?", _appId);
		if (postType != -1)
		{
			stmt.where("i.isdonate = ?", postType);
		}
		if (checkBox != null && checkBox.length != 0)
		{
			if (checkBox[0] == 1)
			{
				stmt.where("i.isPriceBest = ?", 1);
			}

			if (checkBox[0] == 0)
			{
				stmt.where("i.isbest = ?", 1);
			}

			if (checkBox.length > 1)
			{
				stmt.where("i.isPriceBest = ?", 1);
				stmt.where("i.isbest = ?", 1);
			}
		}
		setSelectCondition(stmt);
		return stmt;
	}

	/**
	 * User 목록 갯수 조회 <거래, 기부 체크> <추천, 인기입찰 체크>
	 */
	public SqlSelect getCountByUser(int postType, Long[] checkBox) throws Exception
	{
		SqlSelect stmt = new SqlSelect();
		stmt.from(MARKETITEM);
		stmt.where("i.isvisb = 1");
		stmt.where("i.moduleid = ?", _moduleId);
		if (!_moduleMenu)
			stmt.where("i.appid = ?", _appId);
		if (postType != -1)
		{
			stmt.where("i.isdonate = ?", postType);
		}

		if (checkBox != null && checkBox.length != 0)
		{
			if (checkBox[0] == 1)
			{
				stmt.where("i.isPriceBest = ?", 1);
			}

			if (checkBox[0] == 0)
			{
				stmt.where("i.isbest = ?", 1);
			}

			if (checkBox.length > 1)
			{
				stmt.where("i.isPriceBest = ?", 1);
				stmt.where("i.isbest = ?", 1);
			}
		}
		return stmt;
	}

	/**
	 * 나의 구매리시트 목록을 가져온다.
	 */
	public SqlSelect getBuyList(int status, long id) throws Exception
	{
		SqlSelect stmt = new SqlSelect();
		setSelectCondition(stmt);
		stmt.select(MARKETITEM, "list");
		stmt.where("i.isvisb = 1");
		if (status != -1)
		{
			stmt.where("i.status =?", status);
		}
		stmt.where("i.moduleid = ?", _moduleId);
		stmt.where("i.purchase_userid = ?", id);
		if (!_moduleMenu)
			stmt.where("i.appid = ?", _appId);
		return stmt;
	}

	/**
	 * 나의 구매목록 갯수를 가져온다.
	 */
	public SqlSelect getBuyListCount(int status, long id) throws Exception
	{
		SqlSelect stmt = new SqlSelect();
		stmt.from(MARKETITEM);
		stmt.where("i.isvisb = 1");
		stmt.where("i.moduleid = ?", _moduleId);
		stmt.where("i.purchase_userid = ?", id);
		if (!_moduleMenu)
			stmt.where("i.appid = ?", _appId);
		if (status != -1)
		{
			stmt.where("i.status = ?", status);
		}
		setCountCondition(stmt);
		return stmt;
	}

	/**
	 * 나의 판매목록을 가져온다.
	 */
	public SqlSelect getOwnerList(int status, Long id)
	{
		SqlSelect stmt = new SqlSelect();
		stmt.select(MARKETITEM, "list");
		stmt.where("i.isvisb = 1");
		stmt.where("i.moduleid = ?", _moduleId);
		stmt.where("i.auth_userid = ?", id);
		if (status != -1)
		{
			stmt.where("i.status=?", status);
		}
		if (!_moduleMenu)
			stmt.where("i.appid = ?", _appId);
		setSelectCondition(stmt);
		return stmt;
	}

	/**
	 * 나의 판매목록 갯수를 가져온다.
	 */
	public SqlSelect getOwnerCount(int status, Long id)
	{
		SqlSelect stmt = new SqlSelect();
		stmt.from(MARKETITEM);
		stmt.where("i.isvisb = 1");
		stmt.where("i.moduleid = ?", _moduleId);
		stmt.where("i.auth_userid=?", id);
		if (status != -1)
		{
			stmt.where("i.status=?", status);
		}
		if (!_moduleMenu)
			stmt.where("i.appid = ?", _appId);
		return stmt;
	}

	/**
	 * 입찰정보를 가져온다 (관리자 입찰자 정보 SELECT)
	 */
	public SqlSelect getBid(long id) throws Exception
	{
		SqlSelect stmt = new SqlSelect();
		stmt.select(MARKETITEMBIDDING, "list");
		stmt.where("s.itemid = ?", id);
		return stmt;
	}

	public SqlSelect bidCount(Long id) throws Exception
	{
		SqlSelect stmt = new SqlSelect();
		stmt.from(MARKETITEMBIDDING);
		stmt.where("s.itemid = ?", id);
		return stmt;
	}

	/**
	 * 가장 최신의 입찰정보 하나를 가져온다
	 */
	public SqlSelect getBidLast(long id) throws Exception
	{
		SqlSelect sub = new SqlSelect();
		sub.select(MARKETITEMBIDDING, "list");
		sub.where("s.itemid = ?", id);
		sub.order("s.bidding_price desc");

		SqlSelect stmt = new SqlSelect();
		stmt.select("i.*");
		stmt.from(sub, "i");
		stmt.rownum(1);
		return stmt;
	}

	/**
	 * 본인을 제외한 입찰자 정보를 가져온다 <입찰알람 기능>
	 */
	public SqlSelect getBidInfo(long id, long userid) throws Exception
	{
		SqlSelect sub = new SqlSelect();
		sub.select(MARKETITEMBIDDING, "list");
		sub.where("s.itemid = ?", id);

		SqlSelect stmt = new SqlSelect();
		stmt.select("i.*");
		stmt.from(sub, "i");
		stmt.where("i.userid=?", userid);
		return stmt;
	}

	/**
	 * 입찰 횟수를 return 한다.
	 */
	public int getBidCount(long id) throws Exception
	{
		SqlSelect stmt = new SqlSelect();
		stmt.select("count(*) cnt");
		stmt.from("market_item_bidding");
		stmt.where("itemid = ?", id);
		ResultSet rs = stmt.query();
		rs.next();
		return rs.getInt(1);
	}

	/**
	 * REGISTERED_STATUS 리스트만 가져옴
	 */
	public SqlSelect getSelectList(int menu) throws Exception
	{
		SqlSelect stmt = new SqlSelect();
		stmt.select(MARKETITEM, "list");
		stmt.where("TENANTID = ?", UserService.getTenantId());
		stmt.where("STATUS = ?", MarketItem.REGISTERED_STATUS);
		stmt.where("ISVISB = ?", true);
		return stmt;
	}

	/**
	 * 특정 상태의 리스트 SqlStatement를 돌려준다.
	 */
	public SqlSelect getStatusSelect(int status) throws Exception
	{
		SqlSelect stmt = new SqlSelect();
		stmt.select(MARKETITEM, "list");
		stmt.where("i.status = ?", status);
		stmt.where("i.moduleid = ?", _moduleId);
		if (!_moduleMenu)
			stmt.where("i.appid = ?", _appId);
		if (_useCtgr && _folderId != null)
			stmt.where(FolderSql.level("i.kmid", "i.level", _folderId, _rootInclude));

		return stmt;
	}

	/**
	 * 특정 상태의 카운트 SqlStatement를 돌려준다.
	 */
	public SqlSelect getStatusCount(int status) throws Exception
	{
		SqlSelect stmt = new SqlSelect();
		stmt.from(MARKETITEM);
		stmt.where("i.status = ?", status);
		stmt.where("i.moduleid = ?", _moduleId);
		if (!_moduleMenu)
			stmt.where("i.appid = ?", _appId);
		if (_useCtgr && _folderId != null)
			stmt.where(FolderSql.level("i.kmid", "i.level", _folderId, _rootInclude));

		return stmt;
	}

	/**
	 * 입찰자의 상세정보를 가져온다.
	 */
	public SqlSelect getUserId(long id) throws Exception
	{
		SqlSelect stmt = new SqlSelect();
		stmt.select("userid");
		stmt.from("market_item_bidding");
		stmt.order("bidding_time desc");
		stmt.where("ROWNUM = 1");
		return stmt;
	}

	/**
	 * 관리자의 권한으로 목록을 가져온다.
	 */
	public SqlSelect getAdminVisibleSelect(boolean isAdmin, int status)
	{
		SqlSelect stmt = new SqlSelect();
		setSelectCondition(stmt);
		stmt.select(MARKETITEM, "list");
		stmt.where("i.isvisb = 1");
		stmt.where("i.moduleid = ?", _moduleId);
		if (status != -1)
		{
			stmt.where("i.status = ?", status);
		}
		if (!_moduleMenu)
			stmt.where("i.appid = ?", _appId);

		if (_useCtgr)
		{
			if (_folderId != null)
				stmt.where(FolderSql.level("i.kmid", "i.level", _folderId, _rootInclude));
			if (_trs != null)
				FolderSql.or(stmt, "i.kmid", "i.level", _trs);
			if (_userCtgrReadScrt && !isAdmin)
				stmt.where(FolderArraySql.NotInlevel("kmid", "level", _excludeKmId));
		}
		return stmt;
	}

	/**
	 * 관리자의 권한으로 목록의 갯수를 가져온다.
	 */
	public SqlSelect getAdminVisibleCount(boolean isAdmin, int status)
	{
		SqlSelect stmt = new SqlSelect();
		stmt.from(MARKETITEM);
		stmt.where("i.isvisb = 1");
		stmt.where("i.moduleid = ?", _moduleId);
		if (status != -1)
		{
			stmt.where("i.status = ?", status);
		}
		if (!_moduleMenu)
			stmt.where("i.appid = ?", _appId);
		if (_useCtgr)
		{
			if (_folderId != null)
				stmt.where(FolderSql.level("i.kmid", "i.level", _folderId, _rootInclude));
			if (_trs != null)
				FolderSql.or(stmt, "i.kmid", "i.level", _trs);
			if (_userCtgrReadScrt && !isAdmin)
				stmt.where(FolderArraySql.NotInlevel("kmid", "level", _excludeKmId));
		}
		setCountCondition(stmt);
		return stmt;
	}

	/**
	 * 관리자의 권한으로 추천게시물을 가져온다
	 */
	public SqlSelect getAdminRecommendList(boolean isAdmin, int status)
	{
		SqlSelect stmt = new SqlSelect();
		setSelectCondition(stmt);
		stmt.select(MARKETITEM, "list");
		stmt.where("i.isvisb = 1");
		stmt.where("i.moduleid = ?", _moduleId);
		stmt.where("i.isbest = ?", 1);
		if (status != -1)
		{
			stmt.where("i.status = ?", status);
		}
		if (!_moduleMenu)
			stmt.where("i.appid = ?", _appId);

		if (_useCtgr)
		{
			if (_folderId != null)
				stmt.where(FolderSql.level("i.kmid", "i.level", _folderId, _rootInclude));
			if (_trs != null)
				FolderSql.or(stmt, "i.kmid", "i.level", _trs);
			if (_userCtgrReadScrt && !isAdmin)
				stmt.where(FolderArraySql.NotInlevel("kmid", "level", _excludeKmId));
		}
		return stmt;
	}

	/**
	 * 관리자의 권한으로 추천게시물의 갯수를 가져온다
	 */
	public SqlSelect getAdminRecommendCount(boolean isAdmin, int status)
	{
		SqlSelect stmt = new SqlSelect();
		stmt.from(MARKETITEM);
		stmt.where("i.isvisb = 1");
		stmt.where("i.moduleid = ?", _moduleId);
		stmt.where("i.isbest = ?", 1);
		if (status != -1)
		{
			stmt.where("i.status = ?", status);
		}
		if (!_moduleMenu)
			stmt.where("i.appid = ?", _appId);
		if (_useCtgr)
		{
			if (_folderId != null)
				stmt.where(FolderSql.level("i.kmid", "i.level", _folderId, _rootInclude));
			if (_trs != null)
				FolderSql.or(stmt, "i.kmid", "i.level", _trs);
			if (_userCtgrReadScrt && !isAdmin)
				stmt.where(FolderArraySql.NotInlevel("kmid", "level", _excludeKmId));
		}
		setCountCondition(stmt);
		return stmt;
	}

	/**
	 * 본인 제외 바로 전 입찰자의 ID를 가져온다
	 */
	public Long getBeforeBid(Long id) throws Exception
	{
		Long userId = null;
		SqlSelect stmt = new SqlSelect();
		stmt.select("userid");
		stmt.from("market_item_bidding");
		stmt.where("itemid = ?", id);
		stmt.where("userid != ?", UserService.getUserId());
		stmt.where("ROWNUM = 1");
		stmt.order("bidding_time desc");
		ResultSet rs = stmt.query();
		while (rs.next())
		{
			userId = rs.getLong("userid");
		}
		return userId;
	}

	/**
	 * 해당 컨텐츠의 이전글, 다음글을 출력한다.
	 */
	public SqlSelect getVisibleSelect(boolean isAdmin)
	{
		SqlSelect stmt = new SqlSelect();
		setSelectCondition(stmt);
		stmt.select(MARKETITEM, "list");
		stmt.where("i.isvisb = 1");
		stmt.where("i.moduleid = ?", _moduleId);
		if (!_moduleMenu)
			stmt.where("i.appid = ?", _appId);
		if (_useCtgr)
		{
			if (_folderId != null)
				stmt.where(FolderSql.level("i.kmid", "i.level", _folderId, _rootInclude));
			if (_trs != null)
				FolderSql.or(stmt, "i.kmid", "i.level", _trs);
			if (_userCtgrReadScrt && !isAdmin)
				stmt.where(FolderArraySql.NotInlevel("kmid", "level", _excludeKmId));
		}
		return stmt;
	}

	/**
	 * 게시판 맵 조회시 읽기 권한이 없는 사용자를 제외한다.
	 * @throws Exception
	 */
	private void makeExcludedList() throws Exception
	{
		if (_useCtgr && _userCtgrReadScrt)
		{
			Tree tree = FolderCache.getTree();

			SqlSelect stmt = new SqlSelect();
			stmt.select(KM_SCRT, "s");
			stmt.where(FolderSql.level("kmid", "level", _folderId, true));
			stmt.where("moduleid = ?", _moduleId);
			if (_useAdminCtgr)
				stmt.where("appid is null");
			else
				stmt.where("appid = ?", _appId);

			SqlSelect scrt = new SqlSelect(stmt);
			scrt.where("scrt_code = ?", Folder.SCRT_CODE[0]);

			SqlSelect include = new SqlSelect(stmt);
			include.where(new Sql.InLongArray("scrt_code", FolderScrt.getScrtCodes(_mp)));
			include.where(UserService.getUserState().getXidsFragment());

			SqlSelect minus = new SqlSelect();
			minus.select("s.kmid");
			minus.from(scrt, "s");
			minus.leftOuter(include, "i", "s.kmid = i.kmid");
			minus.where("i.kmid IS NULL");

			ResultSet rs = minus.query();
			while (rs.next())
			{
				Tree.Entry entry = tree.getEntry(new Long(rs.getLong(1)));
				setExcludeNodeId(entry);
			}
			if (_excludeKmId.size() < 1)
				_excludeKmId.add(new Long(-1));
		}
	}

	/**
	 * 읽기권한이 없는 맵의 kmid를 추가한다.
	 * @param entry
	 * @throws Exception
	 */
	private void setExcludeNodeId(Tree.Entry entry) throws Exception
	{
		entry.visitSelfOrDescendants(new NodeVisitor()
		{
			public void visit(Node node)
			{
				_excludeKmId.add(node.getId());
			}
		});
	}

	/**
	 * SELECT문의 LEVEL INDEX 조건을 설정한다.
	 * <p>
	 * 검색, 정렬 조건을 사용하지 않는 경우에만 설정된다.
	 */
	public void setSelectCondition(SqlSelect stmt)
	{
		if (isUseHint())
		{
			StringBuffer hint = new StringBuffer("/*+ INDEX(i " + INDEX);
			switch (_level)
			{
				case 1 :
					hint.append("LEVEL1");
					break;
				case 2 :
					hint.append("LEVEL2");
					break;
				case 3 :
					hint.append("LEVEL3");
					break;
				case 4 :
					hint.append("LEVEL4");
					break;
				default:
					hint.append("ISVISB");
			}
			hint.append(") */ ");

			Map<String, String> hintMap = new HashMap<String, String>();
			hintMap.put(SqlChooser.ORACLE, hint.toString());
			stmt.indexHint(hintMap);
		}
	}

	/**
	 * LEVEL INDEX의 사용여부를 돌려준다.
	 */
	private boolean isUseHint()
	{
		return !(_page.isSearch() || _page.isSort());
	}

	/**
	 * 총건수 불필요할 경우 페이징을 위한 카운트만 실시한다.
	 */
	public void setCountCondition(SqlSelect stmt)
	{
		if (!isCountCondition())
		{
			stmt.rownum(_page.getVisibleCount());
		}
	}

	/**
	 * 총건수를 표시하는지 여부를 돌려준다.
	 * <p>
	 * true : 총건수 표시 false : 총건수 표시하지 않음
	 */
	public boolean isCountCondition()
	{
		return (_countVisible || _page.isSearch() || _level > 1);
	}

	/**
	 * SELECT문을 실행한 결과와 총 건수를 JSON으로 출력한다.
	 */
	public void writeJson(PrintWriter writer, SqlSelect select, SqlSelect count) throws Exception
	{
		_writer.setOrder("i.rgst_date desc");
		_writer.setTable("market_item");
		_writer.page(writer, select, count, _ts);
	}

	/**
	 * SELECT문을 실행한 결과를 JSON으로 출력한다.(정렬, paging처리 제외)
	 */
	public void writeListJson(PrintWriter writer, SqlSelect select, boolean count) throws Exception
	{
		if (count)
			_writer.list(writer, select, new SqlSelect(select));
		else
			_writer.list(writer, select);
	}

	public void writeWebzineJson(PrintWriter writer, SqlSelect select) throws Exception
	{
		_writer.setTable("marekt_item");
		_writer.webzinePage(writer, select, _ts);
	}

	/**
	 * COUNT 문을 실행하지 않고 paging 조건을 추가하여 SELECT 문을 실행한 결과를 JSON으로 출력한다.
	 * <p>
	 * 포틀릿 페이지와 같이 총 건수를 필요로 하지 않는 페이지에서 사용한다.
	 */
	public void writeJson(PrintWriter writer, SqlSelect select) throws Exception
	{
		_writer.setOrder("i.step");
		_writer.listArray(writer, select);
	}

	public void writeJsonBidding(PrintWriter writer, SqlSelect select, SqlSelect count) throws Exception
	{
		_biddingWriter.setOrder("s.bidding_price desc");
		_biddingWriter.setTable("market_item_bidding");
		_biddingWriter.page(writer, select, count, _ts);
	}

	/**
	 * 정렬조건과 검색조건(ts)를 전달하여 이전글, 다음글을 XML로 출력한다.
	 * <p>
	 * Default 정렬 조건은 SqlWriter의 정렬 조건과 같게 한다.
	 */
	public void writeDuplexJson(PrintWriter writer, SqlSelect select, Long id) throws Exception
	{
		_duplexWriter.setOrder("i.rgst_date desc");
		_duplexWriter.setTable("market_item");
		_duplexWriter.item(writer, select, _ts, id);
	}
}