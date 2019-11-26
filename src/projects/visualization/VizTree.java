package projects.visualization;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class VizTree {
	public class VizTreeNode {

		private String key;
		private VizTreeNode[] children;

		public VizTreeNode(int p) {
			children = new VizTreeNode[p];
		}

		public VizTreeNode(int p, String key) {
			children = new VizTreeNode[p];
			this.key = key;
		}

		public void insert(String newkey) {
			if (newkey.charAt(0) != ' ') {
				// mine
				key = newkey;
			} else if (newkey.charAt(1) != ' ') {
				// I have a new child
				int p = children.length;
				for (int i = 0; i < p; i++) {
					VizTreeNode vizTreeNode = children[i];
					if (vizTreeNode == null) {
						children[i] = new VizTreeNode(p, newkey.trim());
						break;
					}

				}
			} else {
				// I have a new grand child from my last child
				int p = children.length;
				for (int i = p - 1; i >= 0; i--) {
					VizTreeNode vizTreeNode = children[i];
					if (vizTreeNode != null)
					{
						children[i].insert(newkey.substring(1));
						break;
					}
				}
			}
		}
	}


	private final int nodeSize = 40;

	private int nodeWidth;
	private int nodeHeight;
	private int minSiblingSpacing;

	private final Font node_font = new Font("Times New Roman", Font.PLAIN, 20);
	
	private int parentChildSpacing; // Think of 30 as margin
	private final BasicStroke edge_stroke = new BasicStroke(2);
	private final BasicStroke node_stroke = new BasicStroke(1);
	private final Color edge_color = Color.BLUE;
	private final Color node_color = Color.RED;
	private final String empty_child = "*";
	private int treeP;

	public VizTree()
	{
		nodeWidth = nodeSize + 100;
		nodeHeight = nodeSize + 100;
		minSiblingSpacing = 20;
		parentChildSpacing = nodeHeight + 30; // Think of 30 as margin
	}
	public VizTree(int nodeWidth,int nodeHeight,int siblingSpacing)
	{
		this.nodeWidth = nodeWidth;
		this.nodeHeight = nodeHeight;
		this.minSiblingSpacing = siblingSpacing;
		parentChildSpacing = nodeHeight + 30; // Think of 30 as margin
	}
	private Point drawTree(Graphics2D g, VizTreeNode curr, int width, int center_x, int center_y) {
		
		g.setColor(Color.RED);
		Point node_pos = new Point(center_x + (width - nodeWidth) / 2, center_y);
		drawTreeNode(g, curr, node_pos.x, node_pos.y);

		int child_idx=-1;
		for (VizTreeNode child_node : curr.children) {
			child_idx++;
			
			if (child_node == null || child_node.key.equals(empty_child))
				continue;
						
			Point child_pos = drawTree(g, child_node, width / curr.children.length,
					center_x + child_idx * ( width / curr.children.length), center_y + parentChildSpacing);
			drawEdge(g, node_pos, child_pos);
		}
		
		return node_pos;

	}

	private void drawEdge(Graphics2D g, Point node_pos, Point child_pos) {
		g.setColor(edge_color);
		g.setStroke(edge_stroke);
		g.drawLine(node_pos.x + nodeWidth / 2, node_pos.y + nodeHeight, child_pos.x + nodeWidth / 2, child_pos.y);
	}
	
	private void drawCenteredString(Graphics2D g, String text, Rectangle rect) {
		/**
		 * Credits : https://stackoverflow.com/questions/27706197/how-can-i-center-graphics-drawstring-in-java
		 */

	    // Get the FontMetrics
	    FontMetrics metrics = g.getFontMetrics(node_font);
	    String[] text_splits = text.split("\n");
	    // Determine the X coordinate for the text
	    int x = rect.x + (rect.width - metrics.stringWidth(text_splits[0])) / 2;
	    // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
	    int y = rect.y + ((rect.height - (metrics.getHeight()*text_splits.length)) / 2) ; //+ metrics.getAscent();
	    // Set the font
	    g.setFont(node_font);
	    // Draw the String
//	    g.drawString(text, x, y);

		for (String line : text_splits)
			g.drawString(line, x, y += g.getFontMetrics().getHeight());
	}

	private void drawTreeNode(Graphics2D g, VizTreeNode curr, int center_x, int center_y) {
		g.setColor(node_color);
		g.setStroke(node_stroke);
		g.drawRect(center_x, center_y, nodeWidth, nodeHeight);
		Rectangle rect = new Rectangle(center_x, center_y, nodeWidth, nodeHeight);
		drawCenteredString(g,curr.key,rect);
		
		//g.drawString(curr.key.toString(), center_x + node_width / 2, center_y + node_height / 2);
//		System.out.println(curr.key.toString());
	}

	private int countSpaces(int tree_depth) {
		int result = 0;
		// TODO: Maybe there is a mathematical formula to compute this
		for (int i = 0; i < tree_depth + 1; i++) {
			result += (int) Math.pow(2, i);
		}
		return result;
	}

	private void drawTree(VizTreeNode root, String filename,int max_depth) {
		try {

			int tree_depth = max_depth; // 1-based . A tree with a single node has depth = 1
			int leaf_count = (int) Math.pow(treeP, tree_depth - 1);
			
			int image_vertical_spacing = 20;
			
			int width = countSpaces(tree_depth) * minSiblingSpacing + leaf_count * nodeWidth;
			int betaMessage = 20;
			int height = parentChildSpacing * (tree_depth - 1) + tree_depth * nodeHeight + image_vertical_spacing + betaMessage;

			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = image.createGraphics();

			/**
			 * Improve drawing quality. Still no quite good !
			 * Credits : https://stackoverflow.com/questions/19483834/improving-java-graphics2d-quality
			 */
			g.setComposite(AlphaComposite.Src);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		    g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		    g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		    g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		    g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		    
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, width, height);

			drawTree(g, root, width, 0, 10+betaMessage);
			g.drawString("This is a beta release, please use at your own risk.",0,20);

			ImageWriter writer = ImageIO.getImageWritersByFormatName("png").next();
			writer.setOutput(ImageIO.createImageOutputStream(new File(filename + ".png")));
			writer.write(image);
		} catch (Exception e) {
			e.printStackTrace();
			// oops! ¯\_(ツ)_/¯
			System.err.println("Sorry, Something went wrong while drawing the tree");
		}

	}

	public void drawBinaryTreeToFile(ArrayList<String> vizTreeDescription, String filename) {
		treeP = 2;
		drawTreeToFile(vizTreeDescription, filename);
	}

	public void drawBTreeToFile(ArrayList<String> vizTreeDescription, int p, String filename) {
		treeP = p;
		drawTreeToFile(vizTreeDescription,filename);
	}


	private int countSpacesInString(String key)
	{
		int spaceCount = 1;
		for (char c : key.toCharArray()) {
		    if (c == ' ') {
		         spaceCount++;
		    }
		    else
		    	break;
		}
		return spaceCount;
	}
	private void drawTreeToFile(ArrayList<String> vizTreeDescription, String filename) {
		//System.out.println("========");
		Iterator<String> it = (Iterator<String>) vizTreeDescription.iterator();
		VizTreeNode root = new VizTreeNode(treeP);
		int max_depth = 0;
		while (it.hasNext()) {
			String newkey = (String) it.next();
			
			//System.out.println(newkey);
			root.insert(newkey);
			
//			if (newkey.contains(empty_child))
//				continue;
			
			max_depth = Math.max(max_depth, countSpacesInString(newkey));
		}
		drawTree(root, filename,max_depth);
	}
}
