package com.zhengwenbiao;

import java.util.ArrayList;
import java.util.List;

public class HashCodeDepot {
	//该类主要负责存储相应的花色，数字的指纹
	List<String> mList = new ArrayList<String>();

	HashCodeDepot() {
		mList.add("3f7f7ffffff3f3ff");
		mList.add("0cfcf7fffffbf3fc");
		mList.add("ffcf073787ffffff");
		mList.add("0047fbfffff7f7f7");
	}
}
