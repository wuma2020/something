package kkcoder.weixin.domain.resp;

import java.util.List;

/**
 * 多图文消息
 */
public class NewsMessage {
	/**图文消息的个数<10个以下>*/
	private int ArticleCount;
	/**多条图文消息，，默认第一个item为大图*/
	private List<Article> Articles;
	public int getArticleCount() {
		return ArticleCount;
	}
	public void setArticleCount(int articleCount) {
		ArticleCount = articleCount;
	}
	public List<Article> getArticles() {
		return Articles;
	}
	public void setArticles(List<Article> articles) {
		Articles = articles;
	}
	
}
