package com.example.zr.gxapplication.net.model;

import java.io.Serializable;

public class AgentDetailRequestBody implements Serializable {
  public Long id;
  public Long cert_id;
  public String latitude;
  public String longitude;

  public AgentDetailRequestBody(Long id, Long cert_id, String lat, String lng) {
    this.id = id;
    this.cert_id = cert_id;
    this.latitude = lat;
    this.longitude = lng;
  }

}
