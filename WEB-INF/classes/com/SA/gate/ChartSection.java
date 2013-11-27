package com.SA.gate;

public class ChartSection {
	protected String keyword = null;
	protected String section = null;
	protected long count = 0;

    public ChartSection(String section, String keyword, long count) {
		this.section = section;
		this.keyword = keyword;
		this.count = count;
    }

	public String getSection() {
		return this.section;
	}

	public String getKeyword() {
		return this.keyword;
	}

	public long getCount() {
		return this.count;
	}

	public void setSection(String section) {
		this.section = section;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public void setCount(long count) {
		this.count = count;
	}
}
