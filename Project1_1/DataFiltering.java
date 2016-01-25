// package org.cmu.cc15619;

// https://www.caveofprogramming.com/java/java-file-reading-and-writing-files-in-java.html
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
// http://www.tutorialspoint.com/java/java_regular_expressions.htm
import java.util.regex.*;

class WikiData implements Comparable<WikiData> {
	public String page;
	public long accesses;
	public WikiData(String page, long accesses) {
		this.page = page;
		this.accesses = accesses;
	}
	
	@Override
	public int compareTo(WikiData o) {
		return (int) (o.accesses - this.accesses);
	}
	
	public String toString() {
		return page + " " + Long.toString(accesses);
	}
}

public class DataFiltering {

	/*
	 * Data format
	 * [project name]	[page title]	[number of accesses]	[total data returned in bytes]
	 */
	
	static boolean debug = false;
	
	static String[] SPECIAL_PAGES = {
			"^Media:",
			"^Special:",
			"^Talk:",
			"^User:",
			"^User_talk:",
			"^Project:",
			"^Project_talk:",
			"^File:",
			"^File_talk:",
			"^MediaWiki:",
			"^MediaWiki_talk:",
			"^Template:",
			"^Template_talk:",
			"^Help:",
			"^Help_talk:",
			"^Category:",
			"^Category_talk:",
			"^Portal:",
			"^Wikipedia:",
			"^Wikipedia_talk:"
	};
	
	static String[] EXTENSIONS = {
			".jpg$", 
			".gif$", 
			".png$", 
			".JPG$", 
			".GIF$", 
			".PNG$", 
			".txt$", 
			".ico$"
	};
	
	static String[] BOILERPLATE_PAGES = {
			"404_error/",
			"Main_Page",
			"Hypertext_Transfer_Protocol",
			"Search"
	};
	
	/** 
	 * @param items
	 * @return whether this line should be filtered
	 */
	public static boolean filter(String[] items) {
		// 0. Some lines have only three (or fewer) elements.
		// You should filter out these lines
		if (items.length <= 3)
		{
			if (debug) {
				System.out.println("Violate rule 0");
			}
			return true;
		}
		// 1. Filter out all pages that are not English Wikipedia.
		// (This means that the log lines should start with en (case sensitive),
		// without any suffix attached).
		if (!items[0].equals("en"))
		{
			if (debug) {
				System.out.println("Violate rule 1");
			}
			return true;
		}
		// 2. There are many special pages in Wikipedia that do not need to be 
		// considered when trying to find trending topics. Exclude any pages 
		// whose title starts with the following strings (case sensitive)
		for (String special_page: SPECIAL_PAGES) {
			Pattern r = Pattern.compile(special_page);
			Matcher m = r.matcher(items[1]);
			if (m.find())
			{
				if (debug) {
					System.out.println("Violate rule 2");
				}
				return true;
			}
		}
		// 3. Wikipedia policy states that all English articles must start with
		// an uppercase character. Filter out all page titles that start with
		// lowercase English characters. You may notice that some page titles
		// start with non-English characters, you should choose to retain them
		// in the analysis.
		Pattern pattern3 = Pattern.compile("^[a-z]");
		Matcher matcher3 = pattern3.matcher(items[1]);
		if (matcher3.find())
		{
			if (debug) {
				System.out.println("Violate rule 3");
			}
			return true;
		}
		// 4. You may also get results which refer to image files, exclude any
		// page title that ends with the following extensions (Keep all other
		// extensions intact). (.jpg, .gif, .png, .JPG, .GIF, .PNG, .txt, .ico).
		// Do not use case-insensitive matching, remove exactly those file
		// extensions.
		for (String extension: EXTENSIONS) {
			Pattern r = Pattern.compile(extension);
			Matcher m = r.matcher(items[1]);
			if (m.find())
			{
				if (debug) {
					System.out.println("Violate rule 4");
				}
				return true;
			}
		}
		// 5. Finally, there are some boilerplate page titles, which should be
		// excluded as well. Page titles that are exactly (case sensitive) any
		// of the following strings should be excluded
		for (String page: BOILERPLATE_PAGES) {
			if (items[1].equals(page))
			{
				if (debug) {
					System.out.println("Violate rule 5");
				}
				return true;
			}
		}
		return false;
	}
	
	public static void testFilter() {
		String[] testCases = {
				"en 1 1",
				"au 1 1 1",
				"en Media:wowow 1 1",
				"en small 1 1",
				"en WHATEVER.jpg 1 1",
				"en Search 1 1",
				"en NicePage 1 1"
		};
		for (String testCase: testCases) {
			System.out.println(testCase);
			String[] items = testCase.split("\\s+");
			if (filter(items)) {
				System.out.println("This line will be filtered\n");
			} else {
				System.out.println("This line will not be filtered\n");
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		if (args.length != 2) {
			System.err.println("Usage: java -jar DataFiltering.jar input_file output_file");
			System.exit(1);
		}
		
		if (debug) {
			testFilter();
		}
		
		String input_path = args[0];
		String output_path = args[1];
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(input_path));
			BufferedWriter bw = new BufferedWriter(new FileWriter(output_path));
			
			String line;
			String[] items;
			ArrayList<WikiData> filteredData = new ArrayList<WikiData>();
			while ((line = br.readLine()) != null) {
				items = line.split("\\s+");
				if (!filter(items)) {
					System.out.println(line);
					filteredData.add(new WikiData(items[1], Long.parseLong(items[2])));
				}
			}
			
			// Sort
			Collections.sort(filteredData);
			
			for (WikiData data: filteredData) {
				bw.write(data.toString());
				bw.newLine();
			}
			
			br.close();
			bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		System.out.println("Ok!");
		
	}

}
