package kkcoder.weixin.domain.resp;

/**图片消息*/
public class ImageMessage  extends BaseMessage{

	private Image image;

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}
}
