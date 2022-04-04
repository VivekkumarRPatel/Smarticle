package com.asdc.smarticle;

import com.asdc.smarticle.article.Article;
import com.asdc.smarticle.article.ArticleRepository;
import com.asdc.smarticle.article.ArticleService;
import com.asdc.smarticle.article.ArticleServiceImpl;
import com.asdc.smarticle.article.FilterPojo;
import com.asdc.smarticle.articletag.Tag;
import com.asdc.smarticle.user.User;
import com.asdc.smarticle.user.UserRepository;
import com.asdc.smarticle.user.exception.ArticleException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ArticleServiceUnitTest {
	 	 
 

	@MockBean
	private ArticleRepository articleRepo;// Dependency is mocked.

	@MockBean
	private UserRepository userRepository;
	
	@Autowired
	private ArticleServiceImpl articleServiceImpl;
	
	@Autowired
	private ArticleService articleService;
	 
	
	@MockBean
	private Article article;
	
	@MockBean
	private User user;
	
	@MockBean
	private FilterPojo filterPojo;
	 
	
	@Test 
	void testSaveArticle() throws ArticleException {
		
		Mockito.when(userRepository.findByUserName("alen")).thenReturn(user);
		
		article.setUserId(user);
		Mockito.when(article.getContent()).thenReturn(null);
		Mockito.when(article.getHeading()).thenReturn(null);
		Assert.assertThrows(ArticleException.class, ()->articleService.saveArticle(article, "alen"));
		
		Mockito.when(userRepository.findByUserName("alen")).thenReturn(user);
		Mockito.when(article.getContent()).thenReturn("");
		Mockito.when(article.getHeading()).thenReturn("");
		Assert.assertThrows(ArticleException.class, ()->articleService.saveArticle(article, "alen"));
		
		Mockito.when(userRepository.findByUserName("alen")).thenReturn(user);
		Mockito.when(article.getContent()).thenReturn("ARTIFICAL INTELLIGENCE");
		Mockito.when(article.getHeading()).thenReturn(null);
		Assert.assertThrows(ArticleException.class, ()->articleService.saveArticle(article, "alen"));

		Mockito.when(userRepository.findByUserName("alen")).thenReturn(user);
		Mockito.when(article.getContent()).thenReturn(null);
		Mockito.when(article.getHeading()).thenReturn("AI");
		Assert.assertThrows(ArticleException.class, ()->articleService.saveArticle(article, "alen"));
		
		Mockito.when(userRepository.findByUserName("alen")).thenReturn(user);
		Mockito.when(article.getContent()).thenReturn("ARTIFICAL INTELLIGENCE");
		Mockito.when(article.getHeading()).thenReturn("");
		Assert.assertThrows(ArticleException.class, ()->articleService.saveArticle(article, "alen"));

		Mockito.when(userRepository.findByUserName("alen")).thenReturn(user);
		Mockito.when(article.getContent()).thenReturn("");
		Mockito.when(article.getHeading()).thenReturn("AI");
		Assert.assertThrows(ArticleException.class, ()->articleService.saveArticle(article, "alen"));
		
		Mockito.when(userRepository.findByUserName("alen")).thenReturn(user);
		Mockito.when(article.getContent()).thenReturn(null);
		Mockito.when(article.getHeading()).thenReturn("ARTIFICAL INTELLIGENCE");
		Assert.assertThrows(ArticleException.class, ()->articleService.saveArticle(article, "alen"));

		Mockito.when(userRepository.findByUserName("alen")).thenReturn(user);
		Mockito.when(article.getContent()).thenReturn("AI");
		Mockito.when(article.getHeading()).thenReturn(null);
		Assert.assertThrows(ArticleException.class, ()->articleService.saveArticle(article, "alen"));
		
		Mockito.when(userRepository.findByUserName("alen")).thenReturn(user);
		Mockito.when(article.getContent()).thenReturn("");
		Mockito.when(article.getHeading()).thenReturn("ARTIFICAL INTELLIGENCE");
		Assert.assertThrows(ArticleException.class, ()->articleService.saveArticle(article, "alen"));

		Mockito.when(userRepository.findByUserName("alen")).thenReturn(user);
		Mockito.when(article.getContent()).thenReturn("AI");
		Mockito.when(article.getHeading()).thenReturn("");
		Assert.assertThrows(ArticleException.class, ()->articleService.saveArticle(article, "alen"));		
		
		Mockito.when(userRepository.findByUserName("alen")).thenReturn(user);
		Mockito.when(article.getContent()).thenReturn("ARTIFICAL INTELLIGENCE");
		Mockito.when(article.getHeading()).thenReturn("AI");
		Mockito.when(articleRepo.save(article)).thenReturn(article);
		Assert.assertEquals(article, articleService.saveArticle(article, "alen"));
	} 
	  
	@Test 
	void testIsHeadingAndContentEmpty() {
		
		Mockito.when(article.getContent()).thenReturn("");
		Mockito.when(article.getHeading()).thenReturn("");
		Assert.assertTrue(articleServiceImpl.isContentEmpty(article));
		Assert.assertTrue(articleServiceImpl.isHeadingEmpty(article));
		
		Mockito.when(article.getContent()).thenReturn(null);
		Mockito.when(article.getHeading()).thenReturn(null);
		
		Assert.assertTrue(articleServiceImpl.isContentEmpty(article));
		Assert.assertTrue(articleServiceImpl.isHeadingEmpty(article)); 
		
		
		Mockito.when(article.getContent()).thenReturn("BLOCKCHAIN"); 
		Mockito.when(article.getHeading()).thenReturn("AI");

		Assert.assertFalse(articleServiceImpl.isContentEmpty(article));
		Assert.assertFalse(articleServiceImpl.isHeadingEmpty(article)); 
		
	}
	 
	 
	@Test
	void testSetLike() {

		Set<User> users = new HashSet<>();

		Mockito.when(userRepository.findByUserName("alen")).thenReturn(user);
		Mockito.when(article.getId()).thenReturn((long) 1);
		Mockito.when(articleRepo.getById(article.getId())).thenReturn(article);
		Mockito.when(article.getLike()).thenReturn(users);

		article.setLike(article.getLike());
		Mockito.when(articleRepo. save(article)).thenReturn(article);
		articleService.setLike(article, "alen");
		
		users.add(user);
		Mockito.when(userRepository.findByUserName("alen")).thenReturn(user);
		Mockito.when(article.getId()).thenReturn((long) 1);
		Mockito.when(articleRepo.getById(article.getId())).thenReturn(article);
		Mockito.when(article.getLike()).thenReturn(users);
		article.setLike(article.getLike()); 
		Mockito.when(articleRepo. save(article)).thenReturn(article);
		articleService.setLike(article, "alen");
		
	}
	
	@Test
	void TestIsTagListEmpty() { 
		
		Tag tag=new Tag();
		tag.setId((long)1);
		tag.setTagName("BLOCKCHAIN");
		Set<Tag> tagSet=new HashSet<>();
		
		Mockito.when(filterPojo.getTagList()).thenReturn(null);
		Assert.assertTrue(articleService.isUserListEmpty(filterPojo));
		Mockito.when(filterPojo.getTagList()).thenReturn(tagSet);
		Assert.assertTrue(articleService.isUserListEmpty(filterPojo));
		tagSet.add(tag);
		Mockito.when(filterPojo.getTagList()).thenReturn(tagSet);
		Assert.assertFalse(articleService.isTagListEmpty(filterPojo));
		
	}   
	
	@Test
	void TestIsUserListEmpty() {

		List<Long> userIdList = new ArrayList<>();

		Mockito.when(filterPojo.getUserIdList()).thenReturn(null);
		Assert.assertFalse(articleService.isUserListEmpty(filterPojo));
		Mockito.when(filterPojo.getUserIdList()).thenReturn(userIdList);
		Assert.assertTrue(articleService.isUserListEmpty(filterPojo));
		userIdList.add((long) 1);
		Mockito.when(filterPojo.getUserIdList()).thenReturn(userIdList);
		Assert.assertFalse(articleService.isUserListEmpty(filterPojo));
	}
}
 
 
