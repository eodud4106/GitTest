package com.cooing.www.common.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cooing.www.album.dao.AlbumDAO;
import com.cooing.www.album.vo.AlbumVO;
import com.cooing.www.album.vo.CategoryPop;
import com.cooing.www.common.dao.SearchDAO;
import com.cooing.www.common.vo.PageLimit;
import com.cooing.www.member.dao.MemberDAO;
import com.cooing.www.member.vo.Member;


@Controller
public class SearchController {
	private static final Logger logger = LoggerFactory.getLogger(SearchController.class); 
	
	@Autowired
	SearchDAO searchDAO;
	
	@Autowired
	AlbumDAO albumDAO;
	
	@Autowired
	MemberDAO memberDAO; 
	
	// 개인 앨범 조회
	@ResponseBody
	@RequestMapping(value = "/searchTotalAlbumList", method= RequestMethod.POST)
	public ArrayList<AlbumVO> getMyAlbumList(int pagenum , String searchword  , String checknum, HttpSession session) {
		System.out.println(pagenum + "_page / " + searchword + "_search / " + checknum + "_check_list ljs");
		Member member = (Member)session.getAttribute("Member");
		int totalnum = albumDAO.total_album_count(searchword , member.getMember_id() , checknum);
		PageLimit pl = new PageLimit(10,5,pagenum,totalnum);
		return albumDAO.total_album_list(searchword , member.getMember_id() , checknum , pl.getStartBoard() , pl.getCountPage() );
	}
	
	@ResponseBody
	@RequestMapping(value="/searchTotalCount" , method = RequestMethod.POST)
	public int searchTotalCount(String searchword , String checknum , HttpSession session){
		logger.info("search_total_count__jinsu");
		//나누기를 하는 이유는 페이지 카운트로 들어갈 것이기 때문에 10개 씩 추가되기에 10으로 나눔
		Member member = (Member)session.getAttribute("Member");
		return albumDAO.total_album_count( searchword, member.getMember_id() , checknum) / 10;		
	}
	
	@ResponseBody
	@RequestMapping(value="/searchCategoryCount" , method = RequestMethod.POST)
	public int searchCategoryCount(String check , String searchtext , HttpSession session){
		logger.info(searchtext + "_search_category_count__jinsu");
		//나누기를 하는 이유는 페이지 카운트로 들어갈 것이기 때문에 10개 씩 추가되기에 10으로 나눔
		Member member = (Member)session.getAttribute("Member");
		return albumDAO.searchCategoryCount(check , member.getMember_id() , searchtext) / 10;		
	}
	
	@ResponseBody
	@RequestMapping(value="/searchCategory" , method = RequestMethod.POST)
	public ArrayList<AlbumVO> searchCategory(String check , String searchtext , int pagenum , HttpSession session){
		logger.info(searchtext + "_search_Category__jinsu");		
		Member member = (Member)session.getAttribute("Member");
		int totalnum = albumDAO.searchCategoryCount(check , member.getMember_id() , searchtext);
		PageLimit pl = new PageLimit(10,5,pagenum,totalnum);
		//저장
		searchDAO.insertCategoryPop(new CategoryPop(0 , Integer.parseInt(searchtext), "0"));
		//카테고리 번호로 찾아온다.
		ArrayList<AlbumVO> arrayalbum = albumDAO.searchCategory(check , member.getMember_id() , searchtext , pl.getStartBoard() , pl.getCountPage());
		return arrayalbum;		
	}
	
	@ResponseBody
	@RequestMapping(value="/searchInformation" , method = RequestMethod.POST)
	public ArrayList<Map<String , Object>> searchInfomation(String searchdate){
		logger.info("searchInformation__jinsu");		
		return searchDAO.selectDaySearch(searchdate);	
	}
	
	@ResponseBody
	@RequestMapping(value="/searchLike" , method = RequestMethod.POST)
	public ArrayList<Map<String , Object>> searchLike(String searchdate){
		logger.info("searchLike__jinsu");
		ArrayList<Map<String , Object>> map = searchDAO.selectDayLike(searchdate);
		ArrayList<Map<String , Object>> map2 = new ArrayList<Map<String , Object>>();

		for(Map<String , Object> m : map){
			//밖에서 생성하면 maap이 map2에 들어간 상태로 계속적으로 바뀌기 때문에 새로 만듬
			Map<String , Object> maap = new HashMap<String, Object>();
			//맵에 있는 아이는 바로 넘겨서 보내면 에러가 나기에 걸렀다 감
			String albumnum = String.valueOf(m.get("LIKEIT_ALBUMNUM"));
			maap.put("COUNT", m.get("COUNT"));
			maap.put("LIKEIT_ALBUMNUM", albumDAO.searchAlbumNum(Integer.parseInt(albumnum)).getAlbum_name());
			map2.add(maap);
		}
		return map2;				
	}
	
	@ResponseBody
	@RequestMapping(value="/searchAlbumName" , method = RequestMethod.POST , produces="application/json; charset=utf8")
	public String searchAlbum(String num){
		logger.info("searchAlbumName__jinsu");
		return albumDAO.searchAlbumNum(Integer.parseInt(num)).getAlbum_name();				
	}
	
	@ResponseBody
	@RequestMapping(value="/searchCategorypop" , method = RequestMethod.POST)
	public ArrayList<Map<String , Object>> searchCategorypop(String searchdate){
		logger.info("searchCategorypop__jinsu");	
		ArrayList<Map<String , Object>> map = searchDAO.selectDayCategory(searchdate);
		for(Map<String,Object> m : map){
			logger.info(m.toString());
		}
		return map;
	}
	
	@ResponseBody
	@RequestMapping(value="/search_id_check" , method = RequestMethod.POST)
	public ArrayList<String> search_id_check(String text){
		logger.info("search_id_check_groupview__jinsu");		
		return  searchDAO.search_id_check(text);
	}
	
	@RequestMapping(value = "/information", method = RequestMethod.GET)
	public String infomation() {
		logger.info("information__jinsu");
		return "information";
	}
}
