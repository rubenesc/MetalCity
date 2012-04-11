/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metallium.utils.youtube;


public class YouTubeMedia {
 
     private String location;
     private String type;
     private int duration;
     
 
     public YouTubeMedia(String location, String type, int duration) {
          super();
          this.location = location;
          this.type = type;
          this.duration = duration;
     }
 
     public String getLocation() {
          return location;
     }
     public void setLocation(String location) {
          this.location = location;
     }
 
     public String getType() {
          return type;
     }
     public void setType(String type) {
          this.type = type;
     }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
     
}