package com.cooing.www.dy.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.cooing.www.dy.dao.AlbumDAO;
import com.cooing.www.dy.vo.AlbumWriteVO;
import com.cooing.www.jinsu.object.Member;

@Controller
@RequestMapping(value = "albumEdit")
public class AlbumEditController {
	private static String strFilePath = "/FileSave/upload/";
	
	@Autowired
	AlbumDAO albumDAO;
	
	private static final Logger logger = LoggerFactory.getLogger(AlbumEditController.class);
	
	//앨범생성,편집
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String albumEdit(){
			
		return "albumEdit";
	}
	
	// 앨범 임시 저장
		@ResponseBody
		@RequestMapping(value = "/albumImageSave", method = RequestMethod.POST)
		public String pageSave(MultipartHttpServletRequest multi){
	        String newFileName = ""; 	        
	        File fpath = new File(strFilePath);
			if(!fpath.isDirectory()){
				fpath.mkdirs();			
			}	        
	        String ext="";
	        Map<String, MultipartFile> fileMap = multi.getFileMap();
	        logger.info(fileMap.toString());
	        for(MultipartFile multipartfile:fileMap.values()){
	        	int lastIndex = multipartfile.getOriginalFilename().lastIndexOf('.');
	    		if(lastIndex == -1)
	    			ext = "";		
	    		else{
	    			ext = "." + multipartfile.getOriginalFilename().substring(lastIndex + 1);
	    			newFileName = multipartfile.getOriginalFilename().substring(0,lastIndex);
	    		}
	    		newFileName += new Date().getTime();
	    		String[] strarray = {"%" , "," , "\\\\",  "\\" , "." , "?",  "&",  "*", "^"  ,"$" ,"#" , "@" , "!" ,"-" , "="  ,"/" };
	    		for(String s : strarray){
	    			if(newFileName.indexOf(s) != -1){
	    				newFileName = newFileName.replaceAll(s, "");
	    			}	    			
	    		}
	        	File serverFile = null;
	            while(true){
	    			serverFile = new File(strFilePath + newFileName + ext);
	    			if ( !serverFile.isFile()) break;
	    			newFileName = newFileName + new Date().getTime();
	    		}		
	             try {
	            	 multipartfile.transferTo(serverFile);
	             } catch (Exception e) {
	                 e.printStackTrace();
	                 return "fail";
	             }
	        }	        
	        return newFileName + ext;
		}
		
		@ResponseBody
		@RequestMapping(value = "/pageSave", method = RequestMethod.POST)
		public String pageSave(String html , String pagenum){
			logger.info(html);
			logger.info(pagenum);
			return "success";
		}
		
		//사진 좌표값
		@ResponseBody
		@RequestMapping(value = "/coordinate", method = RequestMethod.POST)
		public String coordinate(String array){
			
			
			
			
			/*
			Coordinate_Picture cp = null;
			
			String page = null;
			String div_num = null;
			String top = null;
			String left = null;
			String width = null;
			String height = null;
		
			int next = 0;
			int j = 0;
			logger.info(array);
			boolean flag = true;
			while(flag) {
				if(array.charAt(j)=='p') {
					page = null;
					page = array.substring(1, j);
					next = j;
				} else if(array.charAt(j)=='t'){
					div_num = null;
					div_num = array.substring(next+1, j);
					next = j;
				} else if(array.charAt(j)=='l'){
					top = null;
					top = array.substring(next+1, j);
					next = j;
				} else if(array.charAt(j)=='w'){
					left = null;
					left = array.substring(next+1, j);
					next = j;
				} else if(array.charAt(j)=='h'){
					width = null;
					width = array.substring(next+1, j);
					next = j;
					height = null;
					height = array.substring(next+1, array.length());
					flag = false;
				}
				j++;				
				cp = new Coordinate_Picture(Integer.parseInt(page), Integer.parseInt(div_num) ,Integer.parseInt(top), Integer.parseInt(left), Integer.parseInt(width), Integer.parseInt(height));
			}			
			albumDAO.insertAlbum(cp); //앨범 sql 저장
*/				
			return "success";
		}
		
		//앨범생성
		@RequestMapping(value = "/AlbumNameCreate", method = RequestMethod.GET)
		public String AlbumNameCreate(){
			
//			PageHtmlVO page = new PageHtmlVO(1, 27, 5, "testasdasgagahaha");
//			albumDAO.insertAlbum_Picture(page);
				
			return "Album/AlbumNameCreate";
		}
		
		//앨범 생성
		@RequestMapping(value = "/AlbumFirstCreate", method = RequestMethod.POST)
		public String AlbumFirstCreate(HttpSession session, String album_name, String album_contents,
				int album_party, int album_version, int album_category){
			
			String album_writer = null;
			album_writer = ((Member) session.getAttribute("Member")).getMember_id();
			
			AlbumWriteVO albumwrite = new AlbumWriteVO(album_writer, album_name, album_party, 0, album_contents, album_version, album_category);
			
			boolean create_confirmed = false;
			create_confirmed = albumDAO.createAlbum(albumwrite);
			
			
			return "albumEdit";
		}
		
		@RequestMapping(value = "img", method = RequestMethod.GET)
		public String img(HttpServletResponse response , HttpSession session , String filePath) {
			logger.info("img__jinsu");
			String fullpath = strFilePath + "/" + filePath;
			if( filePath.length() != 0){
				FileInputStream filein = null;
				ServletOutputStream fileout = null;
				try {
					filein = new FileInputStream(fullpath);
					fileout = response.getOutputStream();
					FileCopyUtils.copy(filein, fileout);			
					filein.close();
					fileout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return null;
		}
	
}
