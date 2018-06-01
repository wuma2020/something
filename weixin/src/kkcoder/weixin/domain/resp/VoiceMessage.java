package kkcoder.weixin.domain.resp;
/**语音消息*/
public class VoiceMessage  extends BaseMessage{

	private Voice voice;

	public Voice getVoice() {
		return voice;
	}

	public void setVoice(Voice voice) {
		this.voice = voice;
	}
	
}
