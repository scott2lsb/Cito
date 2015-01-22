package com.app.tomore.beans;

import java.io.Serializable;


public class EventsModel  implements Serializable{

	public String getEventID() {
		return EventID;
	}
	public void setEventID(String eventID) {
		EventID = eventID;
	}
	public String getEventName() {
		return EventName;
	}
	public void setEventName(String eventName) {
		EventName = eventName;
	}
	public String getStartTime() {
		return StartTime;
	}
	public void setStartTime(String startTime) {
		StartTime = startTime;
	}
	public String getEndTime() {
		return EndTime;
	}
	public void setEndTime(String endTime) {
		EndTime = endTime;
	}
	public String getEventDes() {
		return EventDes;
	}
	public void setEventDes(String eventDes) {
		EventDes = eventDes;
	}
	public String getEventImage() {
		return EventImage;
	}
	public void setEventImage(String eventImage) {
		EventImage = eventImage;
	}
	private String EventID;
	private String EventName;
	private String StartTime;
	private String EndTime;
	private String EventDes;
	private String EventImage;
}
