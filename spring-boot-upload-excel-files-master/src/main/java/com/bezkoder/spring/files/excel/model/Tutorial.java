package com.bezkoder.spring.files.excel.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "tutorials")
public class Tutorial {

  @Id
  @Column(name = "id")
  private long id;

  @Column(name = "title")
  @ApiModelProperty(notes = "Title of Course",name="title",required=true,value="test title")
  private String title;

  @Column(name = "description")
  @ApiModelProperty(notes = "description of course",name="description",required=true,value="test desc")
  private String description;

  @Column(name = "published")
  @ApiModelProperty(notes = "Date of publish",name="published",required=true,value="test publish")
  private boolean published;

  public Tutorial() {

  }

  public Tutorial(String title, String description, boolean published) {
    this.title = title;
    this.description = description;
    this.published = published;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean isPublished() {
    return published;
  }

  public void setPublished(boolean isPublished) {
    this.published = isPublished;
  }

  @Override
  public String toString() {
    return "Tutorial [id=" + id + ", title=" + title + ", desc=" + description + ", published=" + published + "]";
  }

}
