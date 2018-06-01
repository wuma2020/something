package kkcoder.weixin.domain.req;

public class LocationMessage extends BaseMessage {

	private String Location_x;
	private String Location_y;
	private String Scale;
	private String Label;
	public String getLocation_x() {
		return Location_x;
	}
	public void setLocation_x(String location_x) {
		Location_x = location_x;
	}
	public String getLocation_y() {
		return Location_y;
	}
	public void setLocation_y(String location_y) {
		Location_y = location_y;
	}
	public String getScale() {
		return Scale;
	}
	public void setScale(String scale) {
		Scale = scale;
	}
	public String getLabel() {
		return Label;
	}
	public void setLabel(String label) {
		Label = label;
	}
	
	
	
}
