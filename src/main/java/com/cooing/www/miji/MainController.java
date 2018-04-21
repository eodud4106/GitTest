package com.cooing.www.miji;

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

import com.cooing.www.dy.dao.AlbumDAO;
import com.cooing.www.dy.vo.AlbumWriteVO;
import com.cooing.www.dy.vo.PageHtmlVO;
import com.cooing.www.jinsu.dao.MemberDAO;
import com.cooing.www.jinsu.dao.RelationDAO;
import com.cooing.www.jinsu.object.Member;
import com.cooing.www.jinsu.object.PageLimit;
import com.cooing.www.joon.dao.AlbumLikesDAO;
import com.cooing.www.joon.vo.AlbumLikesVO;

/**
 * Handles requests for the application home page.
 */
@Controller
public class MainController {
	
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
	
	@Autowired
	RelationDAO relationDAO;
	@Autowired
	AlbumDAO albumDAO;
	@Autowired
	MemberDAO memberDAO;
	@Autowired
	AlbumLikesDAO albumlikesDAO;


	/**
	 * 앨범뷰... 앨범과 페이지 리스트를 갖고 albumView로 이동....
	 */
	@RequestMapping(value = "/albumView", method = RequestMethod.GET)
	public String albumPage(int album_num, Model model) {
		
		try {
			AlbumWriteVO album = albumDAO.searchAlbumNum(album_num);
			if(album == null) return "redirect:/";
			ArrayList<PageHtmlVO> arr_page = albumDAO.select_pages_by_album_num(album_num);
			
			model.addAttribute("album", album);
			model.addAttribute("arr_page", arr_page);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "Album/albumView";
	}
	
	@ResponseBody
	@RequestMapping(value = "/getMyAlbumRead", method = RequestMethod.POST)
	public ArrayList<PageHtmlVO> getMyAlbumRead(String num) {
		return  albumDAO.select_pages_by_album_num(Integer.parseInt(num));
	}
	
	@RequestMapping(value = "/albumTestView", method = RequestMethod.GET)
	public String albumTestPage(Model model, HttpSession session) {
		
		return "albumTestView";
	}
	//좋아요 체크
	@ResponseBody
	@RequestMapping(value = "/check_likes", method = RequestMethod.GET)
	public String albumTestPage(@RequestParam int likeit_albumnum,
			HttpSession session) {
		
		String likeit_memberid = ((Member) session.getAttribute("Member")).getMember_id();
		System.out.println(likeit_albumnum);
		AlbumLikesVO vo = new AlbumLikesVO(likeit_albumnum, likeit_memberid);
		String check_isLike = null;
		check_isLike = albumlikesDAO.confirm_Likes(vo);
		
		return check_isLike;
	}
	
	//친구페이지
	@RequestMapping(value = "/friendPage", method = RequestMethod.GET)
	public String friendPage(String id , Model model , HttpSession session){
		logger.info("friend_get__jinsu");
		Member personally = (Member)session.getAttribute("Member");
		Member friend= memberDAO.selectMember(id);
		model.addAttribute("friend", friend);
		ArrayList<String> arrfriend = relationDAO.selectFriend(personally.getMember_id());
		for(String s:arrfriend){
			if(s.equals(friend.getMember_id())){
				model.addAttribute("check" , true);
				break;
			}
		}
		return "friendPage";
	}
	
	//마이페이지
	@RequestMapping(value = "/myPage", method = RequestMethod.GET)
	public String myPage(HttpSession session , Model model){
		Member member = (Member)session.getAttribute("Member");
		int totalpage = albumDAO.IDAlbumCount(member.getMember_id());
		model.addAttribute("totalpage", (totalpage/10));		
		return "myPage";
	}
	
	// 책 목록 조회
	@ResponseBody
	@RequestMapping(value = "/getMyAlbumList", method= RequestMethod.POST)
	public ArrayList<AlbumWriteVO> getMyAlbumList(HttpSession session , int pagenum) {
		logger.info("myalbumlist_homecontroller_ljs");
		String album_writer = ((Member) session.getAttribute("Member")).getMember_id();
		int totalnum = albumDAO.IDAlbumCount(album_writer);
		PageLimit pl = new PageLimit(10,5,pagenum,totalnum);
		return albumDAO.MyAlbumList(album_writer , pl.getStartBoard() , pl.getCountPage());
	}
	
	//랭킹페이지
	@RequestMapping(value = "/LankingPage", method = RequestMethod.GET)
	public String LankingPage(){
		return "LankingPage";
	}
	
}
