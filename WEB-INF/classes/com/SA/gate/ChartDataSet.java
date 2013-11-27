package com.SA.gate;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ChartDataSet {
	protected ArrayList data = new ArrayList();

	public ChartDataSet(Map dataMap) {
		
		Set sectionSet = dataMap.keySet();
		Iterator iter = sectionSet.iterator();
		while(iter.hasNext())
	    {
			String section = (String)iter.next();
			HashMap dataValues = (HashMap)dataMap.get(section);
			Iterator keyValuePairs = dataValues.entrySet().iterator();
			while(keyValuePairs.hasNext())
		    {
			        Map.Entry entry= (Map.Entry) keyValuePairs.next();
			        String keyword=entry.getKey().toString();
			        Long count = Long.parseLong(entry.getValue().toString());
				data.add(new ChartSection(section, keyword, count));
	         }
	    }
    }
	
	public ArrayList getDataBySection() {
		ArrayList results = new ArrayList();
		HashMap sectionMap = new HashMap();
		Iterator iter = this.data.listIterator();
		int currentPosition = 0;
		while (iter.hasNext()) {
			ChartSection chartSection = (ChartSection)iter.next();
			Integer position = (Integer)sectionMap.get(chartSection.getSection());
			if (position == null) {
				results.add(chartSection);
				sectionMap.put(chartSection.getSection(), new Integer(currentPosition));
				currentPosition++;
			} else {
				ChartSection previousChartSection = (ChartSection)results.get(position.intValue());
				previousChartSection.setCount(previousChartSection.getCount() + chartSection.getCount());
			}
		}
		return results;
	}
	
	public ArrayList getDataByKeyword(String section) {
		ArrayList results = new ArrayList();
		HashMap keywordMap = new HashMap();
		Iterator iter = this.data.listIterator();
		int currentPosition = 0;
		while (iter.hasNext()) {
			ChartSection chartSection = (ChartSection)iter.next();
			if (section == null ? true : section.equals(chartSection.getSection())) {
				Integer position = (Integer)keywordMap.get(chartSection.getKeyword());
				if (position == null) {
					results.add(chartSection);
					keywordMap.put(chartSection.getKeyword(), new Integer(currentPosition));
					currentPosition++;
				} else {
					ChartSection previousChartSection = (ChartSection)results.get(position.intValue());
					previousChartSection.setCount(previousChartSection.getCount() + chartSection.getCount());
				}
			}
		}
		return results;
	}
	
	public ArrayList getKeyWords() {
		ArrayList list = new ArrayList();
		Iterator iter = this.data.listIterator();
		while (iter.hasNext()) {
			ChartSection chartSection = (ChartSection)iter.next();
			list.add(chartSection.getKeyword());
		}
	        return list;
    }
	
	public ArrayList getSections() {
		ArrayList list = new ArrayList();
		Iterator iter = this.data.listIterator();
		while (iter.hasNext()) {
			ChartSection chartSection = (ChartSection)iter.next();
			list.add(chartSection.getSection());
		}
	        return list;
    }
	
}
