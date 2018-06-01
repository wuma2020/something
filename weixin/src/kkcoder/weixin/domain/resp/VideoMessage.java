package kkcoder.weixin.domain.resp;
/**video 消息*/
public class VideoMessage extends BaseMessage{
	/**video*/
	private Video video;

	public Video getVideo() {
		return video;
	}

	public void setVideo(Video video) {
		this.video = video;
	}
	
}
