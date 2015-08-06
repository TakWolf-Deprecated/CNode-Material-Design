package us.feras.mdv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import us.feras.mdv.util.HttpHelper;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.webkit.WebView;

import com.petebevin.markdown.MarkdownProcessor;

/**
 * @author Feras Alnatsheh
 */
public class MarkdownView extends WebView {

	public MarkdownView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MarkdownView(Context context) {
		super(context);
	}

	/**
	 * Loads the given Markdown text to the view as rich formatted HTML. The
	 * HTML output will be styled based on the given CSS file.
	 * 
	 * @param txt
	 *            - input in markdown format
	 * @param cssFileUrl
	 *            - a URL to css File. If the file located in the project assets
	 *            folder then the URL should start with "file:///android_asset/"
	 */
	public void loadMarkdown(String txt, String cssFileUrl) {
		loadMarkdownToView(txt, cssFileUrl);
	}

	/**
	 * Loads the given Markdown text to the view as rich formatted HTML.
	 * 
	 * @param txt
	 *            - input in Markdown format
	 */
	public void loadMarkdown(String txt) {
		loadMarkdown(txt, null);
	}

	/**
	 * Loads the given Markdown file to the view as rich formatted HTML. The HTML
	 * output will be styled based on the given CSS file.
	 * 
	 * @param url
	 *            - a URL to the Markdown file. If the file located in the
	 *            project assets folder then the URL should start with
	 *            "file:///android_asset/"
	 * @param cssFileUrl
	 *            - a URL to css File. If the file located in the project assets
	 *            folder then the URL should start with "file:///android_asset/"
	 */
	public void loadMarkdownFile(String url, String cssFileUrl) {
		new LoadMarkdownUrlTask().execute(url, cssFileUrl);
	}

	public void loadMarkdownFile(String url) {
		loadMarkdownFile(url, null);
	}

	private String readFileFromAsset(String url) throws IOException {
		BufferedReader input = null;
		StringBuilder contents = new StringBuilder();
		try {
			String assetFileName = url.substring(url.lastIndexOf('/') + 1,
					url.length());
			input = new BufferedReader(new InputStreamReader(getContext()
					.getAssets().open(assetFileName)));
			String line = null;
			while ((line = input.readLine()) != null) {
				contents.append(line);
				contents.append(System.getProperty("line.separator"));
			}
			return contents.toString();
		} finally {
			try {
				input.close();
			} catch (IOException e) {
			}
		}
	}

	private class LoadMarkdownUrlTask extends
			AsyncTask<String, Integer, String> {
		private String cssFileUrl;

		protected String doInBackground(String... params) {
			try {
				String txt = "";
				String url = params[0];
				this.cssFileUrl = params[1];
				if (url.startsWith("file:///android_asset")) {
					txt = readFileFromAsset(url);
				} else {
					txt = HttpHelper.get(url).getResponseMessage();
				}
				return txt;
			} catch (Exception ex) {
			}
			return null;
		}

		protected void onProgressUpdate(Integer... progress) {
			// no-op
		}

		protected void onPostExecute(String result) {
			if (result != null) {
				loadMarkdownToView(result, cssFileUrl);
			} else {
				loadUrl("about:blank");
			}
		}
	}

	private void loadMarkdownToView(String txt, String cssFileUrl) {
		MarkdownProcessor m = new MarkdownProcessor();
		String html = m.markdown(txt);
		if (cssFileUrl != null) {
			html = String.format(
					"<link rel=\"stylesheet\" type=\"text/css\" href=\"%s\" />"
							+ html, cssFileUrl);
		}
		loadDataWithBaseURL("fake://", html, "text/html", "UTF-8", null);
	}

}