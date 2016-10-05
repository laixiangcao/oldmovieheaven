package com.jiumeng.movieheaven.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class MovieList implements Serializable{
	private ArrayList<MovieDao> list=new ArrayList<>();
	public void addObject(MovieDao movieDao) {
		list.add(movieDao);
	}
	public ArrayList<MovieDao> getList() {
		return list;
	}
}
