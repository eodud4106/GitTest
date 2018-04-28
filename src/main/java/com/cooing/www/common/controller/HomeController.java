package com.cooing.www.common.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cooing.www.album.dao.BookMarkDAO;
import com.cooing.www.album.dao.AlbumDAO;
import com.cooing.www.album.dao.LikesDAO;
import com.cooing.www.album.vo.LikesVO;
import com.cooing.www.album.vo.AlbumVO;
import com.cooing.www.album.vo.BookMark;
import com.cooing.www.album.vo.CategoryPop;
import com.cooing.www.album.vo.PageVO;
import com.cooing.www.common.dao.SearchDAO;
import com.cooing.www.common.vo.PageLimit;
import com.cooing.www.common.vo.Search;
import com.cooing.www.member.dao.MemberDAO;
import com.cooing.www.member.dao.RelationDAO;
import com.cooing.www.member.vo.Member;
import com.cooing.www.member.vo.Party;
import com.google.gson.Gson;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	
	@Autowired
	RelationDAO relationDAO;
	@Autowired
	AlbumDAO albumDAO;
	@Autowired
	MemberDAO memberDAO;
	@Autowired
	LikesDAO albumlikesDAO;
	@Autowired
	SearchDAO searchDAO;
	@Autowired
	BookMarkDAO albumbookmarkDAO;
	
	private Gson gson = new Gson();
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(String search , Model model, HttpSession session) {
		Member personal = (Member)session.getAttribute("Member");
		if(personal != null){
			ArrayList<Party> arraystrval = relationDAO.searchPartyByMemberid(personal.getMember_id());
			model.addAttribute("group", arraystrval);
			int totalpage = albumDAO.total_album_count("",personal.getMember_id(),"3");
			model.addAttribute("totalpage", (totalpage/10));
			if(search != null){
				searchDAO.insertSearch(new Search(0 , search , "0"));
				model.addAttribute("search" , search);
			}
		}
		return "home";
	}
	//다른곳에서 카테고리 눌렀을 경우 홈으로 이동 후 검색
	@RequestMapping(value = "/category_other", method = RequestMethod.GET)
	public String category_other_home(Model model, HttpSession session , int categorynum) {
		Member personal = (Member)session.getAttribute("Member");
		if(personal != null){
			searchDAO.insertCategoryPop(new CategoryPop(0 , categorynum , "0"));
			ArrayList<Party> arraystrval = relationDAO.searchPartyByMemberid(personal.getMember_id());
			model.addAttribute("group", arraystrval);
			int totalpage = albumDAO.CategoryAlbumCount(categorynum);
			model.addAttribute("totalpage", (totalpage/10));			
			//다른 곳에서 홈으로 검색을 통해 home으로 간다는 것을 알려준다.
			model.addAttribute("search_other", 1);
			//검색어
			model.addAttribute("categorynum" , categorynum);			
		}		
		return "home";
	}
	
	/**
	 * 앨범뷰... 앨범과 페이지 리스트를 갖고 albumView로 이동....
	 */
	@RequestMapping(value = "/albumView", method = RequestMethod.GET)
	public String albumPage(int album_num, Model model , HttpSession session) {
		Member member = (Member)session.getAttribute("Member");
		try {
			AlbumVO album = albumDAO.searchAlbumNum(album_num);
			if(album == null) return "redirect:/";
			ArrayList<PageVO> arr_page = albumDAO.select_pages_by_album_num(album_num);
			//페이지 사이즈가 없으면 2로 책갈피를 없애주고
			if(arr_page.size() > 0){
				if(albumbookmarkDAO.bookmark_check(new BookMark(0,album_num,member.getMember_id(),1))){
					//페이지가 있으면 1로 페이지가 있다고 알려주고
					model.addAttribute("check", 1);
				}else{
					model.addAttribute("check", 0);
				}
			}else{
				model.addAttribute("check", 2);
			}
			model.addAttribute("albumwrite", memberDAO.selectMember(album.getAlbum_writer()));
			model.addAttribute("album", album);
			model.addAttribute("arr_page", arr_page);
			model.addAttribute("likecount", albumlikesDAO.countLikes(album.getAlbum_num()));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "album/albumView";
	}
	
	@ResponseBody
	@RequestMapping(value = "/getMyAlbumRead", method = RequestMethod.POST)
	public ArrayList<PageVO> getMyAlbumRead(String num) {
		return  albumDAO.select_pages_by_album_num(Integer.parseInt(num));
	}

	//좋아요 체크
	@ResponseBody
	@RequestMapping(value = "/check_likes", method = RequestMethod.GET)
	public String albumTestPage(@RequestParam int likeit_albumnum,
			HttpSession session) {
		
		String likeit_memberid = ((Member) session.getAttribute("Member")).getMember_id();
	
		LikesVO vo = new LikesVO(likeit_albumnum, likeit_memberid);
		
		String check_likeMember = null;
		check_likeMember = albumlikesDAO.check_Likes(vo);
		
		return check_likeMember;
	}
	
	//마이페이지
	@RequestMapping(value = "/myPage", method = RequestMethod.GET)
	public String myPage(HttpSession session , Model model){
		Member member = (Member)session.getAttribute("Member");
		int totalpage = albumDAO.total_album_count("" , member.getMember_id(),"4");
		model.addAttribute("totalpage", (totalpage/10));		
		return "myPage";
	}
	
	//랭킹페이지
	@RequestMapping(value = "/LankingPage", method = RequestMethod.GET)
	public String LankingPage(){
		return "LankingPage";
	}
	
	//회원가입 페이지
	@RequestMapping(value="/member_get" , method = RequestMethod.GET)
	public String member_get(){		
		logger.info("member_get__jinsu");
		return "/member";
	}
	
	//친구페이지
	@RequestMapping(value="/friend_get" , method = RequestMethod.GET)
	public String friend_get(String id , Model model , HttpSession session){
		logger.info("friend_get__jinsu");
		Member personally = (Member)session.getAttribute("Member");
		Member friend= memberDAO.selectMember(id);
		model.addAttribute("friend_id", friend);
		ArrayList<String> arrfriend = relationDAO.selectFriend(personally.getMember_id());
		for(String s:arrfriend){
			if(s.equals(friend.getMember_id())){
				model.addAttribute("check" , true);
				break;
			}
		}
		int totalpage = albumDAO.friend_album_count("" , friend.getMember_id() , personally.getMember_id(),"5");
		model.addAttribute("totalpage", (totalpage/10));
		return "friendPage";
	}
}
