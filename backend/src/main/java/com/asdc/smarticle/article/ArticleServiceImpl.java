package com.asdc.smarticle.article;

import com.asdc.smarticle.articletag.Tag;
import com.asdc.smarticle.articletag.TagRepository;
import com.asdc.smarticle.comutil.ApiError;
import com.asdc.smarticle.comutil.AppConstant;
import com.asdc.smarticle.comutil.ApplicationUrlPath;
import com.asdc.smarticle.user.User;
import com.asdc.smarticle.user.UserRepository;
import com.asdc.smarticle.user.exception.ArticleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.json.DataObjectFactory;

import java.util.*;

@Service
public class ArticleServiceImpl implements ArticleService {

	@Autowired
	ArticleRepository articleRepository;

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	TagRepository tagRepository;

	@Override
	public Article saveArticle(Article article, String userName) throws ArticleException {

		User user = userRepository.findByUserName(userName);
		article.setUserId(user);
		System.out.println(article.getTagId());

		if (isContentEmpty(article)
				|| isHeadingEmpty(article)) {
			throw new ArticleException(ApiError.ARTICLE_FIELD_NOT_NULL);
		}

		return articleRepository.save(article);
	}
	
	/**
	 * @author Vivekkumar Patel 
	 * This method checks that header of the article is empty or not.
	 * @param article instance containng details such as content,heading,visibility etc.
	 * @return true if header of the article is empty else false.
	 */
	private boolean isHeadingEmpty(Article article) {
		return article.getContent().isEmpty() || article.getContent() == null;
	}

	/**
	 * @author Vivekkumar Patel 
	 * This method checks that content of the article is empty or not.
	 * @param article instance containng details such as content,heading,visibility etc.
	 * @return true if content of the article is empty else false.
	 */

	private boolean isContentEmpty(Article article) {
		return article.getHeading() == null || article.getHeading().isEmpty();
	}

	@Override
	public List<Article> getArticle(String visibility) throws ArticleException {
		List<Article> articleList = null;
		try {

			if (visibility.equalsIgnoreCase(ApplicationUrlPath.ALL_ARTICLE)) {
				articleList = articleRepository.findAll();
			} else {
				articleList = articleRepository.findByVisibility(visibility.equals("1") ? true : false);
			}

		} catch (Exception e) {
			throw new ArticleException(ApiError.ARTICLE_NOT_PRESENT);
		}
		return articleList;
	}

	@Override
	public Article getArticleById(Long id) {
		Optional<Article> article = articleRepository.findById(id);
		if (article.isPresent()) {
			return article.get();
		}
		return null;
	}

	@Override
	public Page<Article> getArticleByUser(String userName, int page, int totalPage) {
		Pageable pagination = PageRequest.of(page, totalPage,Sort.by("creationDate"));
		Page<Article> listArticle = null;
		User user = userRepository.findByUserName(userName);
		listArticle = articleRepository.findByUserId(user,pagination);
		return listArticle;
	}

	@Override
	public List<Map<String,Object>> getTwitterCountOfArticleTags(Long id){
		Article article = getArticleById(id);
		Set<Tag> tags = article.getTagId();
		if(tags.size()==0){
			return new ArrayList<Map<String,Object>>();
		}
		List<String> tagNames = new ArrayList<>();
		Map<String,String> responseTweetTextAndURL = new HashMap<>();
		List<Map<String,Object>> responseTweetData = new ArrayList<>();
		String query = "lang:en (";
		for(Tag tag : tags){
			query += tag.getTagName() +" OR ";
			tagNames.add(tag.getTagName());
		}

		String searchQuery = query.substring(0, query.length()-4);
		searchQuery += ")";
		Twitter twitter = authentication();

		Query search = new Query(searchQuery);
		search.count(AppConstant.MAX_TWEET);
		QueryResult tweetData;
		try {
			tweetData = twitter.search(search);

			for (Status tweet : tweetData.getTweets()) {
				//List<Object> tweetDataList = new ArrayList<>();
				Map<String,Object> tweetDataMap = new HashMap();
				String tweetLink = "https://twitter.com/" + tweet.getUser().getScreenName() + "/status/" + tweet.getId();
				String removeURL = tweet.getText().replaceAll("((https?|http):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)", "");
				String transformedTweetText = removeURL.replaceAll("[^\\w\\s]", "");
				String authorName = tweet.getUser().getName();
				Date creationDate = tweet.getUser().getCreatedAt();
				int retweetCount = tweet.getRetweetCount();
				String userImage = tweet.getUser().getProfileImageURL();
				//System.out.println(" "+tweetLink+" "+authorName+" "+creationDate+" "+retweetCount+" "+userImage);
				tweetDataMap.put("userImageURL",userImage);
				tweetDataMap.put("authorName",authorName);
				tweetDataMap.put("tweetLink",tweetLink);
				tweetDataMap.put("tweetText",transformedTweetText);
				tweetDataMap.put("creationDate",creationDate);
				tweetDataMap.put("retweetCount",retweetCount);
				responseTweetData.add(tweetDataMap);
			}

		} catch (TwitterException e) {
			e.printStackTrace();
		}

		return responseTweetData;
	}

	//Reference: https://www.tabnine.com/code/java/methods/twitter4j.conf.ConfigurationBuilder/setOAuthConsumerKey
	public static Twitter authentication() {
		ConfigurationBuilder confBuild = new ConfigurationBuilder();
		confBuild.setDebugEnabled(true);
		confBuild.setJSONStoreEnabled(true);
		confBuild.setOAuthConsumerKey("P9xd2SeGKmM75MPcu1e9h5Lhw");
		confBuild.setOAuthConsumerSecret("xEvpgofniw1O8wwtsmz0hxmhVCZz8xv8ifmPxQhfdE77lHSHBt");
		confBuild.setOAuthAccessToken("1499412296568877060-xrfuEHFBue9CDmRFz1zMeqrKvVPiWz");
		confBuild.setOAuthAccessTokenSecret("UPv1OsBFCIuaGOuL8Eo6qfaU3TuRNjIPToJ6kK3k8p2gu");
		return new TwitterFactory(confBuild.build()).getInstance();
	}

}
