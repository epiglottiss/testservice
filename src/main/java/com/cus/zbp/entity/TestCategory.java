package com.cus.zbp.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Entity
@Builder
public class TestCategory extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private String name;
  private String abbreviation;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "software_id")
  private Software software;

  public void update(String name, String abbreviation) {
    this.name = name;
    this.abbreviation = abbreviation;
  }
}
