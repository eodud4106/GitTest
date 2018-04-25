package com.cooing.www.jinsu.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cooing.www.dy.dao.AlbumDAO;
import com.cooing.www.dy.vo.AlbumWriteVO;
import com.cooing.www.jinsu.dao.MemberDAO;
import com.cooing.www.jinsu.dao.SearchDAO;
import com.cooing.www.jinsu.object.CategoryPop;
import com.cooing.www.jinsu.object.HashTag;
import com.cooing.www.jinsu.object.Member;
import com.cooing.www.jinsu.object.PageLimit;
import com.cooing.www.jinsu.object.Search;


@Controller
public class SearchController {
	private static final Logger logger = LoggerFactory.getLogger(SearchController.class); 
	
	@Autowired
	SearchDAO searchDAO;
	
	@Autowired
	AlbumDAO albumDAO;
	
	@Autowired
	MemberDAO memberDAO; 
	
	@ResponseBody
	@RequestMapping(value="/searchWord" , method = RequestMethod.POST)
	public ArrayList<AlbumWriteVO> searchWord(String searchtext , int pagenum){
		logger.info(searchtext + "_search_word__jinsu");
		//저장
		searchDAO.insertSearch(new Search(0 , searchtext , "0"));
		
		int totalnum = albumDAO.SearchAlbumCount(searchtext);
		PageLimit pl = new PageLimit(10,5,pagenum,totalnum);
		
		//검색어가 해쉬 태그 , 앨범 이름, 설명 , 앨범 만든 사람
		ArrayList<AlbumWriteVO> arrayalbum = searchDAO.searchAllAlbum(searchtext , pl.getStartBoard() , pl.getCountPage());
		
		return arrayalbum;		
	}
	
	@ResponseBody
	@RequestMapping(value="/searchWordCount" , method = RequestMethod.POST)
	public int searchWordCount(String searchtext){
		logger.info(searchtext + "_search_word_count__jinsu");
		//나누기를 하는 이유는 페이지 카운트로 들어갈 것이기 때문에 10개 씩 추가되기에 10으로 나눔
		return searchDAO.searchAllAlbumCount(searchtext) / 10;		
	}
	
	@ResponseBody
	@RequestMapping(value="/searchCategoryCount" , method = RequestMethod.POST)
	public int searchCategoryCount(int searchtext){
		logger.info(searchtext + "_search_category_count__jinsu");
		//나누기를 하는 이유는 페이지 카운트로 들어갈 것이기 때문에 10개 씩 추가되기에 10으로 나눔
		return albumDAO.CategoryAlbumCount(searchtext) / 10;		
	}
	
	@ResponseBody
	@RequestMapping(value="/searchTotalCount" , method = RequestMethod.POST)
	public int searchTotalCount(HttpSession session){
		logger.info("search_total_count__jinsu");
		//나누기를 하는 이유는 페이지 카운트로 들어갈 것이기 때문에 10개 씩 추가되기에 10으로 나눔
		Member member = (Member)session.getAttribute("Member");
		return albumDAO.TotalAlbumCount(member.getMember_id()) / 10;		
	}
	
	@ResponseBody
	@RequestMapping(value="/searchLikeCount" , method = RequestMethod.POST)
	public int searchLikeCount(HttpSession session){
		logger.info("search_like_count__jinsu");
		//나누기를 하는 이유는 페이지 카운트로 들어갈 것이기 때문에 10개 씩 추가되기에 10으로 나눔
		Member member = (Member)session.getAttribute("Member");
		int num = albumDAO.LikeAlbumCount(member.getMember_id());
		logger.info(num + "_haha");
		return num / 10;		
	}
	
	@ResponseBody
	@RequestMapping(value="/searchCategory" , method = RequestMethod.POST)
	public ArrayList<AlbumWriteVO> searchCategory(int searchtext , int pagenum ){
		logger.info(searchtext + "_search_Category__jinsu");		
		
		int totalnum = albumDAO.CategoryAlbumCount(searchtext);
		PageLimit pl = new PageLimit(10,5,pagenum,totalnum);
		//저장
		searchDAO.insertCategoryPop(new CategoryPop(0 , searchtext , "0"));
		//카테고리 번호로 찾아온다.
		ArrayList<AlbumWriteVO> arrayalbum = albumDAO.searchCategory(searchtext , pl.getStartBoard() , pl.getCountPage());
		return arrayalbum;		
	}
	
	@ResponseBody
	@RequestMapping(value="/searchInformation" , method = RequestMethod.POST)
	public ArrayList<Map<String , Object>> searchInfomation(String searchdate){
		logger.info("searchInformation__jinsu");
		ArrayList<Map<String , Object>> map = searchDAO.selectDaySearch(searchdate);		
		return map;	
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
		return map;				
	}
	
	@RequestMapping(value = "/information", method = RequestMethod.GET)
	public String infomation() {
		logger.info("information__jinsu");
		return "information";
	}
	
	@RequestMapping(value = "/searchHashTag", method = RequestMethod.GET)
	public String searchHashTag(Model model, String hashTag) {
		logger.info("searchHashTag__jinsu");
		model.addAttribute("searchWord", hashTag);
		//저장
		hashTag = hashTag.substring(1, hashTag.length());
		searchDAO.insertSearch(new Search(0 , hashTag , "0"));
		//검색어가 해쉬 태그 , 앨범 이름, 설명 , 앨범 만든 사람
		ArrayList<AlbumWriteVO> arrayalbum = albumDAO.searchAlbum(hashTag);		
		ArrayList<HashTag> arraytag = searchDAO.selectHashTag(hashTag);
		for(int i = 0; i < arraytag.size();i++){
			arrayalbum.add(albumDAO.searchAlbumNum(arraytag.get(i).getTag_albumnum()));
		}
		
		model.addAttribute("listalbum", arrayalbum.toString());	
		
		return "home";
	}
}
