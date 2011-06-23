/******************************************************************************
 *
 * CoreWall / Corelyzer - An Initial Core Description Tool
 * Copyright (C) 2004, 2005 Arun Gangadhar Gudur Rao, Julian Yu-Chung Chen
 * Electronic Visualization Laboratory, University of Illinois at Chicago
 *
 * This software is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either Version 2.1 of the License, or 
 * (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public 
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License along
 * with this software; if not, write to the Free Software Foundation, Inc., 
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Questions or comments about CoreWall should be directed to 
 * cavern@evl.uic.edu
 *
 *****************************************************************************/

package corelyzer.data;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;
import java.util.prefs.Preferences;

import corelyzer.graphics.SceneGraph;

/**
 * Meta data used to keep configurable preferences
 */
public class CRPreferences {
	public static boolean MAC_OS_X = System.getProperty("os.name").toLowerCase().startsWith("mac os x");

	// todo need to reduce to this
	private final Preferences prefs;
	private Hashtable<String, String> defaultValues;

	// ----
	private static float DEFAULT_BGCOLOR = 0.1f;

	// FIXME change default thumbnail URL to some place local
	public static String defaultThumbnailURL = "http://www.evl.uic.edu/cavern/corewall/iCores/feeds/thumbnail.png";

	public boolean isInited = false;

	// Keep track of directory open previously
	static String currentDir = System.getProperty("user.home") + System.getProperty("file.separator") + "Documents" + System.getProperty("file.separator")
			+ "Corelyzer";

	// Directories
	public String config_Directory;
	public String appStart_Directory;
	public String datastore_Directory;
	public String texBlock_Directory;
	public String download_Directory;
	public String cache_Directory;
	public String tmp_Directory;
	public String annotation_Directory;

	// UI Controls
	public boolean lockCoreSectionImage;

	// Misc. Visuals
	// Canvas Grid
	public boolean grid_show;
	public int grid_type;
	public float grid_size;
	public int grid_thickness;
	public float grid_r;
	public float grid_g;
	public float grid_b;

	// Display configurations
	public int numberOfRows;
	public int numberOfColumns;
	public int screenWidth;
	public int screenHeight;

	public float dpix;
	public float dpiy;
	public float borderLeft;
	public float borderRight;
	public float borderDown;
	public float borderUp;

	public int row_offset;
	public int column_offset;

	// UI Enhancement
	public boolean useQuaqua = false;
	public boolean autoCheckVersion = true;

	// session file History
	public static int maxHistoryEntries = 10;

	// For remembering last dir selected
	public static String getCurrentDir() {
		return currentDir;
	}

	public static void setCurrentDir(final String currentDir) {
		CRPreferences.currentDir = currentDir;
	}

	Vector<String> sessionHistory;

	// Canvas background color
	float[] bgcolor;

	// auto zoom when double clicked sections
	boolean autoZoom = true;

	// auto scale graph height
	boolean autoScaleGraph = true;

	// auto scale marker size
	boolean autoScaleMarker = true;

	int refreshInterval = 10;

	public CRPreferences() {
		super();

		prefs = Preferences.userNodeForPackage(this.getClass());
		initDefaultValues();

		// init default preference values
		String sp = System.getProperty("file.separator");
		config_Directory = System.getProperty("user.home") + sp + ".corelyzer";

		try {
			// Get current working directory
			appStart_Directory = new File(".").getCanonicalPath();
		} catch (IOException e) {
			System.err.println("Cannot get app dir and datastore dir!");
		}

		if (MAC_OS_X || System.getProperty("os.name").equalsIgnoreCase("linux")) {
			datastore_Directory = System.getProperty("user.home") + sp + "Documents" + sp + "Corelyzer";
		} else {
			datastore_Directory = System.getProperty("user.home") + sp + "My Documents" + sp + "Corelyzer";
		}
		annotation_Directory = datastore_Directory + sp + "Annotations";

		cache_Directory = datastore_Directory + sp + "Caches";
		texBlock_Directory = cache_Directory + sp + "imgblocks";
		download_Directory = cache_Directory + sp + "downloads";
		tmp_Directory = cache_Directory + sp + "tmp";

		// UI preferences default
		lockCoreSectionImage = true;

		// Canvas Grid default
		grid_show = false;
		grid_type = 0;
		grid_size = 10.0f;
		grid_thickness = 1;
		grid_r = 0.8f;
		grid_g = 0.8f;
		grid_b = 0.8f;

		// Display preferences default
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		numberOfRows = 1;
		numberOfColumns = 1;
		screenWidth = dim.width;
		screenHeight = dim.height;
		dpix = 72.0f;
		dpiy = 72.0f;
		borderLeft = 1.0f;
		borderRight = 1.0f;
		borderDown = 1.0f;
		borderUp = 1.0f;
		row_offset = 0;
		column_offset = 0;

		// Session history
		sessionHistory = new Vector<String>();

		// Canvas background color
		this.bgcolor = new float[3];
		bgcolor[0] = bgcolor[1] = bgcolor[2] = DEFAULT_BGCOLOR;
	}

	public CRPreferences(final CRPreferences p) {
		this();
		isInited = p.isInited;

		config_Directory = p.config_Directory;
		appStart_Directory = p.appStart_Directory;
		datastore_Directory = p.datastore_Directory;
		texBlock_Directory = p.texBlock_Directory;
		download_Directory = p.download_Directory;
		datastore_Directory = p.datastore_Directory;
		annotation_Directory = p.annotation_Directory;

		lockCoreSectionImage = p.lockCoreSectionImage;
		useQuaqua = p.useQuaqua;
		autoCheckVersion = p.autoCheckVersion;

		grid_show = p.grid_show;
		grid_type = p.grid_type;
		grid_size = p.grid_size;
		grid_thickness = p.grid_thickness;
		grid_r = p.grid_r;
		grid_g = p.grid_g;
		grid_b = p.grid_b;

		numberOfRows = p.numberOfRows;
		numberOfColumns = p.numberOfColumns;
		screenWidth = p.screenWidth;
		screenHeight = p.screenHeight;

		dpix = p.dpix;
		dpiy = p.dpiy;
		borderLeft = p.borderLeft;
		borderRight = p.borderRight;
		borderDown = p.borderDown;
		borderUp = p.borderUp;
		row_offset = p.row_offset;
		column_offset = p.column_offset;

		// Session History
		sessionHistory = new Vector<String>();
		for (String s : p.sessionHistory) {
			sessionHistory.add(s);
		}

		// Canvas background color
		bgcolor = p.bgcolor;

		// autozoom
		this.autoZoom = p.isAutoZoom();

		this.autoScaleGraph = p.isAutoScaleGraph();
		this.autoScaleMarker = p.isAutoScaleMarker();
	}

	public void applyUIConfig() {
		if (this.grid_show) {
			SceneGraph.enableCanvasGrid(true);
			SceneGraph.setCanvasGridColor(this.grid_r, this.grid_g, this.grid_b);
			SceneGraph.setCanvasGridSize(this.grid_size);
			SceneGraph.setCanvasGridThickness(this.grid_thickness);
			SceneGraph.setCanvasGridType(this.grid_type);
		}

		SceneGraph.setBackgroundColor(bgcolor[0], bgcolor[1], bgcolor[2]);

		// auto scale
		SceneGraph.setGraphAutoScale(this.autoScaleGraph);
		SceneGraph.setMarkerAutoScale(this.autoScaleMarker);

		// show origin or section labels
		Boolean showOrigin = Boolean.parseBoolean(this.prefs.get("ui.showOrigin", "true"));
		Boolean showSectionLabel = Boolean.parseBoolean(this.prefs.get("ui.showSectionLabel", "true"));
		SceneGraph.setShowOrigin(showOrigin);
		SceneGraph.setShowSectionText(showSectionLabel);
	}

	public CRPreferences copy() throws CloneNotSupportedException {
		return (CRPreferences) this.clone();
	}

	public boolean getAutoCheckVersion() {
		return this.autoCheckVersion;
	}

	public String getLocalRepositoryPath() {
		String repoPath = datastore_Directory + System.getProperty("file.separator") + "Core Repository";

		File repo = new File(repoPath);
		if (!repo.exists()) {
			repo.mkdir();
		}

		return repoPath;
	}

	public String getProperty(final String key) {
		String defaultValue = defaultValues.get(key) == null ? "" : defaultValues.get(key);

		return prefs.get(key, defaultValue);
	}

	public int getRefreshInterval() {
		return refreshInterval;
	}

	public Vector<String> getSessionHistory() {
		return sessionHistory;
	}

	public boolean getUseQuaqua() {
		return this.useQuaqua;
	}

	private void initDefaultValues() {
		this.defaultValues = new Hashtable<String, String>();
		defaultValues.put("sessionSharing.serverAddress", "corewalldb.evl.uic.edu");
		defaultValues.put("sessionSharing.serverPort", "16688");

		defaultValues.put("ui.canvas.alwaysBelow", "true");
		defaultValues.put("ui.verticalDepthScroll", "false");
	}

	public boolean isAutoScaleGraph() {
		return autoScaleGraph;
	}

	public boolean isAutoScaleMarker() {
		return autoScaleMarker;
	}

	public boolean isAutoZoom() {
		return autoZoom;
	}

	public boolean readDirectoryConfig(final File aFile) {
		String sp = System.getProperty("file.separator");

		try {
			FileReader fr = new FileReader(aFile);
			BufferedReader br = new BufferedReader(fr);

			String path = br.readLine(); // 1st line: text block dir
			File f = new File(path);
			if (f.exists()) {
				String abspath = f.getCanonicalPath();

				if (!abspath.endsWith(sp)) {
					abspath += sp;
				}

				this.texBlock_Directory = abspath;
				SceneGraph.setTexBlockDirectory(abspath);
			} else {
				br.close();
				fr.close();
				return false; // hasDirs = false;
			}

			path = br.readLine(); // 2nd line: download dir
			f = new File(path);
			if (f.exists()) {
				String abspath = f.getCanonicalPath();

				if (!abspath.endsWith(sp)) {
					abspath += sp;
				}

				this.download_Directory = abspath;
			} else {
				br.close();
				fr.close();
				return false; // hasDirs = false;
			}

			br.close();
			fr.close();

			// Upgrade from previous configs, add 'tmp' dir
			File cache_dir = new File(this.cache_Directory);
			File tmp_dir = new File(this.tmp_Directory);

			if (!cache_dir.exists()) {
				System.out.println("-- [INFO] Create cache dir: " + cache_dir);
				cache_dir.mkdir();
			}

			if (!tmp_dir.exists()) {
				System.out.println("-- [INFO] Create temp dir: " + tmp_dir);
				tmp_dir.mkdir();
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public boolean readDisplayConfig(final File aFile) {
		// read the file and loadin setups
		try {
			FileReader fr = new FileReader(aFile);
			BufferedReader br = new BufferedReader(fr);
			String line;

			line = br.readLine();
			this.screenWidth = new Integer(line);

			line = br.readLine();
			this.screenHeight = new Integer(line);

			line = br.readLine();
			this.numberOfRows = new Integer(line);

			line = br.readLine();
			this.numberOfColumns = new Integer(line);

			line = br.readLine();
			this.dpix = new Float(line);

			line = br.readLine();
			this.dpiy = new Float(line);

			line = br.readLine();
			this.borderUp = new Float(line);

			line = br.readLine();
			this.borderDown = new Float(line);

			line = br.readLine();
			this.borderLeft = new Float(line);

			line = br.readLine();
			this.borderRight = new Float(line);

			try {
				line = br.readLine();
				this.column_offset = new Integer(line);

				line = br.readLine();
				this.row_offset = new Integer(line);
			} catch (NumberFormatException e) {
				System.out.println("[CRPreferences] Ignore rest display.conf");
			}

			br.close();
			fr.close();

			return true;
		} catch (Exception e) {
			System.out.println("ERROR Reading previous display settings");
			e.printStackTrace();

			return false;
		}

	}

	public boolean readProperties(final File aFile) {
		try {
			FileReader fr = new FileReader(aFile);
			BufferedReader br = new BufferedReader(fr);

			String line;
			while ((line = br.readLine()) != null) {
				String[] toks = line.split("=");

				String key = toks[0].trim();
				String value = toks[1].trim();
				this.setProperty(key, value);
			}

			br.close();
			fr.close();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean readUIConfig(final File aFile) {
		try {
			this.sessionHistory.clear();

			FileReader fr = new FileReader(aFile);
			BufferedReader br = new BufferedReader(fr);

			String line;
			while ((line = br.readLine()) != null) {
				String[] toks = line.split("=");

				// lockCoreSectionImage
				if (toks[0].trim().equalsIgnoreCase("lockCoreSectionImage")) {
					this.lockCoreSectionImage = toks[1].trim().equalsIgnoreCase("true");
				} else if (toks[0].trim().equalsIgnoreCase("usequaqua")) {
					this.useQuaqua = toks[1].trim().equalsIgnoreCase("true");
				} else if (toks[0].trim().equalsIgnoreCase("autocheckversion")) {
					this.autoCheckVersion = toks[1].trim().equalsIgnoreCase("true");
				} else if (toks[0].trim().equalsIgnoreCase("canvas_bgcolor_r")) {
					bgcolor[0] = Float.parseFloat(toks[1].trim());
				} else if (toks[0].trim().equalsIgnoreCase("canvas_bgcolor_g")) {
					bgcolor[1] = Float.parseFloat(toks[1].trim());
				} else if (toks[0].trim().equalsIgnoreCase("canvas_bgcolor_b")) {
					bgcolor[2] = Float.parseFloat(toks[1].trim());
				} else if (toks[0].trim().equalsIgnoreCase("autozoom")) {
					this.setAutoZoom(Boolean.parseBoolean(toks[1].trim()));
				} else if (toks[0].trim().equalsIgnoreCase("autoscalegraph")) {
					this.setAutoScaleGraph(Boolean.parseBoolean(toks[1].trim()));
				} else if (toks[0].trim().equalsIgnoreCase("autoscalemarker")) {
					this.setAutoScaleMarker(Boolean.parseBoolean(toks[1].trim()));
				} else if (toks[0].trim().equalsIgnoreCase("enableGrid")) {
					this.grid_show = toks[1].trim().equalsIgnoreCase("true");
					// now let's read values for grid configuration.
					line = br.readLine();
					this.grid_type = new Integer(line);
					line = br.readLine();
					this.grid_size = new Float(line);
					line = br.readLine();
					this.grid_thickness = new Integer(line);
					line = br.readLine();
					this.grid_r = new Float(line);
					line = br.readLine();
					this.grid_g = new Float(line);
					line = br.readLine();
					this.grid_b = new Float(line);
				} else if (toks[0].trim().toLowerCase().startsWith("sessionhst")) {
					String path = toks[1].trim();
					this.sessionHistory.add(path);
				} else {
					System.out.println("---> Ignore unknown UI config option");
				}
			}

			br.close();
			fr.close();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean save() {
		File conf_dir = new File(this.config_Directory);

		if (conf_dir.exists()) {
			saveDisplayConfig();
			saveDirectoryConfig();
			saveUIConfig();
		}

		if (this.prefs != null) {
			prefs.putBoolean("corelyzer.preferences.Migrated", true);
		}

		return true;
	}

	private void saveDirectoryConfig() {
		try {
			// Store off the default tex block directory
			File f = new File(this.config_Directory + "/directories.txt");
			FileWriter fw = new FileWriter(f);
			// String tbdir =
			// corelyzer.helper.SceneGraph.getTexBlockDirectory();
			// fw.write(tbdir, 0, tbdir.length());
			fw.write(texBlock_Directory, 0, texBlock_Directory.length());
			fw.write('\n');
			fw.write(download_Directory, 0, download_Directory.length());
			fw.write('\n');
			fw.flush();
			fw.close();
		} catch (Exception exp) {
			exp.printStackTrace();
		}

		// Migrate to Java Preferences too
		if (this.prefs != null) {
			prefs.put("dir.texBlock", texBlock_Directory);
			prefs.put("dir.download", download_Directory);
		}
	}

	private void saveDisplayConfig() {
		try {
			File f = new File(this.config_Directory + "/display.conf");
			FileWriter fw = new FileWriter(f);
			String line;

			line = screenWidth + "\n";
			fw.write(line, 0, line.length());

			line = screenHeight + "\n";
			fw.write(line, 0, line.length());

			line = numberOfRows + "\n";
			fw.write(line, 0, line.length());

			line = numberOfColumns + "\n";
			fw.write(line, 0, line.length());

			line = dpix + "\n";
			fw.write(line, 0, line.length());

			line = dpiy + "\n";
			fw.write(line, 0, line.length());

			line = borderUp + "\n";
			fw.write(line, 0, line.length());

			line = borderDown + "\n";
			fw.write(line, 0, line.length());

			line = borderLeft + "\n";
			fw.write(line, 0, line.length());

			line = borderRight + "\n";
			fw.write(line, 0, line.length());

			line = column_offset + "\n";
			fw.write(line, 0, line.length());

			line = row_offset + "\n";
			fw.write(line, 0, line.length());

			fw.flush();
			fw.close();
		} catch (Exception e) {
			System.out.println("ERROR Saving Display settings");
			e.printStackTrace();
		}

		// Migrate to Java Preferences too
		if (this.prefs != null) {
			prefs.putInt("display.numberOfRows", numberOfRows);
			prefs.putInt("display.numberOfColumns", numberOfColumns);
			prefs.putInt("display.screenWidth", screenWidth);
			prefs.putInt("display.screenHeight", screenHeight);
			prefs.putFloat("display.dpix", dpix);
			prefs.putFloat("display.dpiy", dpiy);
			prefs.putFloat("display.borderLeft", borderLeft);
			prefs.putFloat("display.borderRight", borderRight);
			prefs.putFloat("display.borderDown", borderDown);
			prefs.putFloat("display.borderUp", borderUp);
			prefs.putInt("display.row_offset", row_offset);
			prefs.putInt("display.column_offset", column_offset);
		}
	}

	private void saveUIConfig() {
		try {
			File f = new File(config_Directory + "/ui.conf");
			FileWriter fw = new FileWriter(f);
			String line;

			line = "lockCoreSectionImage = " + this.lockCoreSectionImage + "\n";
			fw.write(line, 0, line.length());

			line = "useQuaqua = " + this.useQuaqua + "\n";
			fw.write(line, 0, line.length());

			line = "autoCheckVersion = " + this.autoCheckVersion + "\n";
			fw.write(line, 0, line.length());

			// Canvas background color
			line = "canvas_bgcolor_r = " + bgcolor[0] + "\n";
			fw.write(line, 0, line.length());

			line = "canvas_bgcolor_g = " + bgcolor[1] + "\n";
			fw.write(line, 0, line.length());

			line = "canvas_bgcolor_b = " + bgcolor[2] + "\n";
			fw.write(line, 0, line.length());

			line = "autoZoom = " + this.autoZoom + "\n";
			fw.write(line, 0, line.length());

			line = "autoScaleGraph = " + this.autoScaleGraph + "\n";
			fw.write(line, 0, line.length());

			line = "autoScaleMarker = " + this.autoScaleMarker + "\n";
			fw.write(line, 0, line.length());

			// grid stuff
			line = "enableGrid = " + this.grid_show + "\n";
			fw.write(line, 0, line.length());
			line = this.grid_type + "\n";
			fw.write(line, 0, line.length());
			line = this.grid_size + "\n";
			fw.write(line, 0, line.length());
			line = this.grid_thickness + "\n";
			fw.write(line, 0, line.length());
			line = this.grid_r + "\n";
			fw.write(line, 0, line.length());
			line = this.grid_g + "\n";
			fw.write(line, 0, line.length());
			line = this.grid_b + "\n";
			fw.write(line, 0, line.length());

			// session history
			if (sessionHistory != null && sessionHistory.size() > 0) {
				int startIndex = sessionHistory.size() > maxHistoryEntries ? sessionHistory.size() - maxHistoryEntries : 0;

				for (int i = startIndex; i < sessionHistory.size(); i++) {
					String path = sessionHistory.get(i);
					line = "sessionHst" + (i - startIndex) + " = " + path + "\n";
					fw.write(line, 0, line.length());
				}
			}

			fw.close();
		} catch (Exception e) {
			System.err.println("Error Saveing UI settings");
			e.printStackTrace();
		}

		// Migrate to Java Preferences too
		if (this.prefs != null) {
			prefs.putBoolean("ui.lockSection", this.lockCoreSectionImage);
			prefs.putBoolean("ui.useQuaqua", this.useQuaqua);
			prefs.putBoolean("ui.autoCheckVersion", this.autoCheckVersion);

			prefs.putFloat("ui.canvas_bgcolor_r", this.bgcolor[0]);
			prefs.putFloat("ui.canvas_bgcolor_g", this.bgcolor[1]);
			prefs.putFloat("ui.canvas_bgcolor_b", this.bgcolor[2]);

			prefs.putBoolean("ui.autoZoom", this.autoZoom);
			prefs.putBoolean("ui.autoScaleGraph", this.autoScaleGraph);
			prefs.putBoolean("ui.autoScaleMarker", this.autoScaleMarker);

			prefs.putBoolean("ui.enableGrid", this.grid_show);
			prefs.putInt("ui.grid.type", this.grid_type);
			prefs.putInt("ui.grid.thickness", this.grid_thickness);
			prefs.putFloat("ui.grid.size", this.grid_size);
			prefs.putFloat("ui.grid.color_r", this.grid_r);
			prefs.putFloat("ui.grid.color_g", this.grid_g);
			prefs.putFloat("ui.grid.color_b", this.grid_b);

			int numOfEntries = sessionHistory.size() < maxHistoryEntries ? sessionHistory.size() : maxHistoryEntries;
			for (int i = 0; i < numOfEntries; ++i) {
				String key = "ui.sessionHistory." + i;
				prefs.put(key, this.sessionHistory.elementAt(i));
			}
		}
	}

	public void setAutoCheckVersion(final boolean b) {
		autoCheckVersion = b;
	}

	public void setAutoScaleGraph(final boolean autoScaleGraph) {
		this.autoScaleGraph = autoScaleGraph;
	}

	public void setAutoScaleMarker(final boolean autoScaleMarker) {
		this.autoScaleMarker = autoScaleMarker;
	}

	public void setAutoZoom(final boolean autoZoom) {
		this.autoZoom = autoZoom;
	}

	public void setProperty(final String key, final String value) {
		this.prefs.put(key, value);
	}

	public void setRefreshInterval(final int refreshInterval) {
		this.refreshInterval = refreshInterval;
	}

	public void setSessionHistory(final Vector<String> aHistory) {
		this.sessionHistory = aHistory;
	}

	public void setUseQuaqua(final boolean b) {
		useQuaqua = b;
	}
}