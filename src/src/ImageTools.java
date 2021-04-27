package src;

import javax.swing.ImageIcon;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 * Tools that can be used to modify an image
 * 
 * @author Riley Power
 * @version April 26 2021
 *
 */
public class ImageTools {
	/**
	 * Rotates the baseImage rotate degrees and returns it
	 * 
	 * @param baseImage The image to rotate
	 * @param rotate    The amount to rotate the base image in degrees
	 * @return The rotated image
	 */
	public static Image rotateImage(Image baseImage, int rotate) {
		// Convert to bufferedImage
		BufferedImage image = toBufferedImage(baseImage);
		// If the image isn't loaded the transform will cause errors
		if (image == null) {
			return null;
		}
		// Convert degrees to radians
		double rotationRequired = Math.toRadians(rotate);
		// Center of image
		double locationX = image.getWidth() / 2;
		double locationY = image.getHeight() / 2;
		// Rotate image the desired amount of degrees around the center
		AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
		// Apply filtering to make it look nice
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		// Return that
		return (Image) op.filter(image, null);
	}// rotateImage

	public static BufferedImage toBufferedImage(Image img) {
		// Do nothing if its already a BufferedImage
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}
		try {
			// Create a buffered image with transparency (and the current frame if its a
			// gif)
			BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null),
					BufferedImage.TYPE_INT_ARGB);
			// Draw the image to the BufferedImage
			Graphics2D bGr = bimage.createGraphics();
			bGr.drawImage(img, 0, 0, null);
			bGr.dispose();
			// Return
			return bimage;
		} catch (Exception e) {
			// If image hasnt loaded from file yet
			return null;
		}

	}// toBufferedImage
}// ImageTools
