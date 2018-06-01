package kkcoder.weixin.domain.resp;
/**视频消息体*/
public class Video {
	/**vedio 的id*/
	private String MediaId;
	/**vedio名称*/
	private String Title;
	/**vedio描述*/
	private String  description;
	public String getMediaId() {
		return MediaId;
	}
	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
