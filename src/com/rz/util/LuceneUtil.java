package com.rz.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class LuceneUtil
{
	static Analyzer analyzer = new StandardAnalyzer();
	static IndexWriter writer;

	public static void indexFile(String indexDir, File file)
	{
		try
		{
			IndexWriter writer = getIndexWriter(indexDir);
			Document document = new Document();
			//document.add(new TextField("content", new FileReader(file)));
			//这个问题困扰了我半天,请注意编码问题
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			document.add(new TextField("content", br));
			document.add(new TextField("filename", file.getName(), Field.Store.YES));
			document.add(new TextField("filepath", file.getCanonicalPath(), Field.Store.YES));
			writer.addDocument(document);
			writer.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void indexDir(String indexDir, String dir)
	{
		try
		{
			File[] files = new File(dir).listFiles();
			IndexWriter writer = getIndexWriter(indexDir);
			for (File file : files)
			{
				Document document = new Document();
				document.add(new TextField("content", new FileReader(file)));
				document.add(new TextField("filename", file.getName(), Field.Store.YES));
				document.add(new TextField("filepath", file.getCanonicalPath(), Field.Store.YES));
				writer.addDocument(document);
			}
			writer.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void updateIndex(String indexDir, File file)
	{
		try
		{
			Term term = new Term("filepath", file.getCanonicalPath());
			Document doc = new Document();
			Document document = new Document();
			document.add(new TextField("content", new FileReader(file)));
			document.add(new TextField("filename", file.getName(), Field.Store.YES));
			document.add(new TextField("filepath", file.getCanonicalPath(), Field.Store.YES));
			writer.updateDocument(term, doc);
			writer.commit();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void deleteIndex(String field, String text)
	{
		try
		{
			Term term = new Term(field, text);
			writer.deleteDocuments(new Term[] { term });
			writer.commit();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private static IndexWriter getIndexWriter(String indexDir)
	{
		IndexWriter writer = null;
		IndexWriterConfig writerConfig = new IndexWriterConfig(analyzer);
		try
		{
			Directory directory = FSDirectory.open(Paths.get(indexDir));
			writer = new IndexWriter(directory, writerConfig);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return writer;
	}

	public static List<String> search(String indexDir, String q)
	{
		List<String> list = new LinkedList<String>();
		IndexReader reader = null;
		try
		{
			long t0 = System.currentTimeMillis();
			Directory dir = FSDirectory.open(Paths.get(indexDir));
			reader = DirectoryReader.open(dir);
			IndexSearcher is = new IndexSearcher(reader);
			QueryParser parser = new QueryParser("content", analyzer);
			Query query = parser.parse(q);
			TopDocs hits = is.search(query, Integer.MAX_VALUE);
			for (ScoreDoc scoreDoc : hits.scoreDocs)
			{
				Document doc = is.doc(scoreDoc.doc);
				// String filepath = doc.get("filepath");
				// String filename = doc.get("filename");
				InputStream in = new FileInputStream(doc.get("filepath"));
				String c = IOUtils.toString(in, "UTF-8");
				String h = getHighlighterString(query, c);
				IOUtils.closeQuietly(in);
				list.add(h);
			}
			long t1 = System.currentTimeMillis();
			System.out.println(t1 - t0);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{

			try
			{
				reader.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return list;
	}

	public static String getHighlighterString(Query query, String context)
	{
		QueryScorer scorer = new QueryScorer(query);
		Formatter simpleHTMLFormatter = new SimpleHTMLFormatter("<font color=\"red\">", "</font>");
		Highlighter highlighter = new Highlighter(simpleHTMLFormatter, scorer);
		Fragmenter fragmenter = new SimpleFragmenter(200);
		highlighter.setTextFragmenter(fragmenter);
		try
		{
			return highlighter.getBestFragment(analyzer, "context", context);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] ages)
	{
		String indexDir = "d:/Lindex2";
		// String dataDir = "d:/upload";
		// String fn = "a.txt";
		// File f = new File(dataDir, fn);
		// indexFile(indexDir, f);
		List<String> list = search(indexDir, "管理");
		System.out.println(list.size());
		for (String string : list)
		{
			System.out.println(string);
		}
	}
}
